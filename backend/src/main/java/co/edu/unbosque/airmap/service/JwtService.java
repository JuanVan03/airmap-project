package co.edu.unbosque.airmap.service;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import co.edu.unbosque.airmap.entity.Usuario;

/**
 * Servicio que genera los tokens JWT cuando el usuario ingresa.
 *
 * @author Juan
 * @version 1.0
 */
@Service
public class JwtService {

	@Autowired
	private JwtEncoder jwtEncoder;

	// Duracion del token en milisegundos (jwt.expiration, 1 hora)
	@Value("${jwt.expiration}")
	private long expiracion;

	/**
	 * Constructor vacío. Spring crea la instancia e inyecta las dependencias.
	 */
	public JwtService() {
	}

	/**
	 * Genera el token JWT del usuario. Lleva el correo, el nombre, el proveedor y el rol, y dura 1 hora.
	 *
	 * @param usuario usuario que ingresó
	 * @return token firmado
	 */
	public String generar(Usuario usuario) {
		Instant ahora = Instant.now();
		JwtClaimsSet datos = JwtClaimsSet.builder().issuer("airmap").subject(usuario.getCorreo()).issuedAt(ahora)
				.expiresAt(ahora.plusMillis(expiracion)).claim("nombre", usuario.getNombre())
				.claim("proveedor", usuario.getProveedor()).claim("roles", List.of(usuario.getRol())).build();
		JwsHeader encabezado = JwsHeader.with(MacAlgorithm.HS256).build();
		return jwtEncoder.encode(JwtEncoderParameters.from(encabezado, datos)).getTokenValue();
	}

}
