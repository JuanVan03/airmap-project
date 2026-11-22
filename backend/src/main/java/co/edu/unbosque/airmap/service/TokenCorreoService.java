package co.edu.unbosque.airmap.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.airmap.entity.TokenCorreo;
import co.edu.unbosque.airmap.entity.Usuario;
import co.edu.unbosque.airmap.exception.NegocioException;
import co.edu.unbosque.airmap.repository.TokenCorreoRepository;

/**
 * Servicio que crea y valida los enlaces de un solo uso que se mandan por correo. En la base
 * de datos solo se guarda el hash del token.
 *
 * @author SakiYoshida
 * @version 1.0
 */
@Service
public class TokenCorreoService {

	// Segundos que hay que esperar para pedir otro correo del mismo tipo
	private static final int ESPERA_SEGUNDOS = 60;

	@Autowired
	private TokenCorreoRepository tokenRepo;

	private final SecureRandom random = new SecureRandom();

	/**
	 * Constructor vacío. Spring crea la instancia e inyecta las dependencias.
	 */
	public TokenCorreoService() {
	}

	/**
	 * Crea un token nuevo. Solo aquí existe completo: en la base queda su hash. Los tokens
	 * anteriores del mismo tipo se borran.
	 *
	 * @param usuario dueño del token
	 * @param tipo VERIFICACION o RESTABLECER
	 * @param minutosDeVida minutos que dura el enlace
	 * @return token para poner en el enlace
	 * @throws NegocioException si pidió otro correo hace menos de un minuto
	 */
	@Transactional
	public String crear(Usuario usuario, String tipo, int minutosDeVida) {
		tokenRepo.findFirstByUsuarioAndTipoOrderByCreadoDesc(usuario, tipo).ifPresent(anterior -> {
			if (anterior.getCreado().plusSeconds(ESPERA_SEGUNDOS).isAfter(LocalDateTime.now())) {
				throw new NegocioException("Espera un minuto antes de pedir otro correo.", 429, "CORREO_MUY_SEGUIDO");
			}
		});

		// Solo queda vigente el enlace mas reciente
		tokenRepo.deleteByUsuarioAndTipo(usuario, tipo);

		byte[] bytes = new byte[32];
		random.nextBytes(bytes);
		String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

		tokenRepo.save(new TokenCorreo(hash(token), tipo, usuario, LocalDateTime.now().plusMinutes(minutosDeVida)));
		return token;
	}

	/**
	 * Valida el token, lo marca como usado y devuelve el usuario al que pertenece.
	 *
	 * @param token token que llegó en el enlace
	 * @param tipo tipo que se espera
	 * @return usuario dueño del token
	 * @throws NegocioException si el enlace no sirve, ya se usó o venció
	 */
	@Transactional
	public Usuario consumir(String token, String tipo) {
		NegocioException invalido = new NegocioException("El enlace no es válido o ya fue usado.", 400,
				"TOKEN_INVALIDO");
		TokenCorreo guardado = tokenRepo.findByTokenHash(hash(token.trim())).orElseThrow(() -> invalido);

		if (!guardado.getTipo().equals(tipo) || guardado.isUsado()) {
			throw invalido;
		}
		if (guardado.getExpira().isBefore(LocalDateTime.now())) {
			throw new NegocioException("El enlace venció. Solicita uno nuevo.", 400, "TOKEN_VENCIDO");
		}

		guardado.setUsado(true);
		return guardado.getUsuario();
	}

	private String hash(String token) {
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-256");
			return HexFormat.of().formatHex(sha.digest(token.getBytes(StandardCharsets.UTF_8)));
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("SHA-256 no esta disponible", e);
		}
	}

}
