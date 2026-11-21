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

	public HistorialService() {
	}

	// Del mas reciente al mas viejo
	public List<HistorialDTO> listar(String correo) {
		Long usuarioId = buscarUsuarioId(correo);
		return historialRepo.findByUsuarioIdOrderByFechaVistaDesc(usuarioId).stream().map(this::aDTO).toList();
	}

	// Anota que el usuario abrio un vuelo. Si ya estaba, solo actualiza la fecha (no se repite)
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

	@Transactional
	public void quitar(String correo, String vueloId) {
		Long usuarioId = buscarUsuarioId(correo);
		historialRepo.deleteByUsuarioIdAndVueloId(usuarioId, vueloId);
		auditoria.registrar("HISTORIAL_ELIMINADO", correo, AuditoriaService.EXITO, "Quitó un vuelo de su historial");
	}

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
