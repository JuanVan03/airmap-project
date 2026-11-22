package co.edu.unbosque.airmap.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.airmap.dto.HistorialDTO;
import co.edu.unbosque.airmap.entity.HistorialVuelo;
import co.edu.unbosque.airmap.exception.NegocioException;
import co.edu.unbosque.airmap.repository.HistorialVueloRepository;
import co.edu.unbosque.airmap.repository.UsuarioRepository;

/**
 * Servicio del historial de vuelos que abre cada usuario.
 *
 * @author SakiYoshida
 * @version 1.0
 */
@Service
public class HistorialService {

	// Maximo de vuelos en el historial de cada usuario: al pasarse se borran los mas viejos
	private static final int MAXIMO_HISTORIAL = 50;

	@Autowired
	private HistorialVueloRepository historialRepo;

	@Autowired
	private UsuarioRepository usuarioRepo;

	@Autowired
	private AuditoriaService auditoria;

	/**
	 * Constructor vacío. Spring crea la instancia e inyecta las dependencias.
	 */
	public HistorialService() {
	}

	/**
	 * Lista el historial del usuario, del más reciente al más viejo.
	 *
	 * @param correo correo del usuario (sale del token JWT)
	 * @return vuelos del historial
	 */
	public List<HistorialDTO> listar(String correo) {
		Long usuarioId = buscarUsuarioId(correo);
		return historialRepo.findByUsuarioIdOrderByFechaVistaDesc(usuarioId).stream().map(this::aDTO).toList();
	}

	/**
	 * Anota que el usuario abrió un vuelo. Si ya estaba solo actualiza la fecha, y si se pasa
	 * del máximo borra los más viejos.
	 *
	 * @param correo correo del usuario (sale del token JWT)
	 * @param datos id y número del vuelo
	 */
	@Transactional
	public void registrar(String correo, HistorialDTO datos) {
		Long usuarioId = buscarUsuarioId(correo);

		HistorialVuelo vuelo = historialRepo.findFirstByUsuarioIdAndVueloId(usuarioId, datos.getVueloId())
				.orElseGet(() -> new HistorialVuelo(usuarioId, datos.getVueloId(), datos.getNumeroVuelo()));
		vuelo.setFechaVista(LocalDateTime.now());
		if (datos.getNumeroVuelo() != null) {
			vuelo.setNumeroVuelo(datos.getNumeroVuelo());
		}
		historialRepo.save(vuelo);

		// Si se paso del maximo, se borran los mas viejos
		List<HistorialVuelo> todos = historialRepo.findByUsuarioIdOrderByFechaVistaDesc(usuarioId);
		if (todos.size() > MAXIMO_HISTORIAL) {
			historialRepo.deleteAll(todos.subList(MAXIMO_HISTORIAL, todos.size()));
		}
	}

	/**
	 * Quita un vuelo del historial.
	 *
	 * @param correo correo del usuario (sale del token JWT)
	 * @param vueloId id del vuelo
	 */
	@Transactional
	public void quitar(String correo, String vueloId) {
		Long usuarioId = buscarUsuarioId(correo);
		historialRepo.deleteByUsuarioIdAndVueloId(usuarioId, vueloId);
		auditoria.registrar("HISTORIAL_ELIMINADO", correo, AuditoriaService.EXITO, "Quitó un vuelo de su historial");
	}

	/**
	 * Borra todo el historial del usuario.
	 *
	 * @param correo correo del usuario (sale del token JWT)
	 */
	@Transactional
	public void vaciar(String correo) {
		Long usuarioId = buscarUsuarioId(correo);
		historialRepo.deleteByUsuarioId(usuarioId);
		auditoria.registrar("HISTORIAL_VACIADO", correo, AuditoriaService.EXITO, "Borró todo su historial de vuelos");
	}

	// El correo sale del token JWT, asi cada usuario solo ve lo suyo
	private Long buscarUsuarioId(String correo) {
		return usuarioRepo.findByCorreo(correo)
				.orElseThrow(() -> new NegocioException("El usuario ya no existe.", 401, "USUARIO_NO_ENCONTRADO"))
				.getId();
	}

	private HistorialDTO aDTO(HistorialVuelo vuelo) {
		return new HistorialDTO(vuelo.getVueloId(), vuelo.getNumeroVuelo(),
				vuelo.getFechaVista().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
	}

}
