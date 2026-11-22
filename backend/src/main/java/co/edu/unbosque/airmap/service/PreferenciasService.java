package co.edu.unbosque.airmap.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.airmap.dto.PreferenciasDTO;
import co.edu.unbosque.airmap.entity.Preferencias;
import co.edu.unbosque.airmap.exception.NegocioException;
import co.edu.unbosque.airmap.repository.PreferenciasRepository;
import co.edu.unbosque.airmap.repository.UsuarioRepository;

/**
 * Servicio de la apariencia de la página que elige cada usuario.
 *
 * @author Juan
 * @version 1.0
 */
@Service
public class PreferenciasService {

	@Autowired
	private PreferenciasRepository preferenciasRepo;

	@Autowired
	private UsuarioRepository usuarioRepo;

	@Autowired
	private AuditoriaService auditoria;

	/**
	 * Constructor vacío. Spring crea la instancia e inyecta las dependencias.
	 */
	public PreferenciasService() {
	}

	/**
	 * Devuelve la apariencia del usuario. Si nunca la ha cambiado, devuelve el diseño original.
	 *
	 * @param correo correo del usuario (sale del token JWT)
	 * @return preferencias del usuario
	 */
	public PreferenciasDTO obtener(String correo) {
		Long usuarioId = buscarUsuarioId(correo);
		return preferenciasRepo.findByUsuarioId(usuarioId).map(this::aDTO).orElse(porDefecto());
	}

	/**
	 * Guarda la apariencia del usuario (la crea la primera vez) y lo registra en la auditoría.
	 *
	 * @param correo correo del usuario (sale del token JWT)
	 * @param datos tema, letra, tamaño, color e ícono del avión
	 * @return preferencias guardadas
	 */
	public PreferenciasDTO guardar(String correo, PreferenciasDTO datos) {
		Long usuarioId = buscarUsuarioId(correo);

		// Se actualiza la fila del usuario, o se crea la primera vez
		Preferencias preferencias = preferenciasRepo.findByUsuarioId(usuarioId).orElse(new Preferencias(usuarioId));
		preferencias.setTema(datos.getTema());
		preferencias.setLetra(datos.getLetra());
		preferencias.setTamano(datos.getTamano());
		preferencias.setColor(datos.getColor());
		preferencias.setAvion(datos.getAvion());
		preferencias.setFechaActualizado(LocalDateTime.now());
		PreferenciasDTO guardadas = aDTO(preferenciasRepo.save(preferencias));

		auditoria.registrar("APARIENCIA_ACTUALIZADA", correo, AuditoriaService.EXITO,
				"Tema " + datos.getTema() + ", letra " + datos.getLetra() + ", tamaño " + datos.getTamano()
						+ ", color " + datos.getColor() + ", avión " + datos.getAvion());
		return guardadas;
	}

	/**
	 * Diseño original de AirMap.
	 *
	 * @return OSCURO, INTER, NORMAL, AZUL y AVION
	 */
	public PreferenciasDTO porDefecto() {
		return new PreferenciasDTO("OSCURO", "INTER", "NORMAL", "AZUL", "AVION");
	}

	// El correo sale del token JWT, asi cada usuario solo cambia lo suyo
	private Long buscarUsuarioId(String correo) {
		return usuarioRepo.findByCorreo(correo)
				.orElseThrow(() -> new NegocioException("El usuario ya no existe.", 401, "USUARIO_NO_ENCONTRADO"))
				.getId();
	}

	private PreferenciasDTO aDTO(Preferencias p) {
		return new PreferenciasDTO(p.getTema(), p.getLetra(), p.getTamano(), p.getColor(), p.getAvion());
	}

}
