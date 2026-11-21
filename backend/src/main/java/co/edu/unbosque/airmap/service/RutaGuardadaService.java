package co.edu.unbosque.airmap.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.airmap.dto.RutaGuardadaDTO;
import co.edu.unbosque.airmap.entity.RutaGuardada;
import co.edu.unbosque.airmap.exception.NegocioException;
import co.edu.unbosque.airmap.repository.RutaGuardadaRepository;
import co.edu.unbosque.airmap.repository.UsuarioRepository;

@Service
public class RutaGuardadaService {

	// Maximo de rutas guardadas por usuario
	private static final int MAXIMO_RUTAS = 30;

	@Autowired
	private RutaGuardadaRepository rutaRepo;

	@Autowired
	private UsuarioRepository usuarioRepo;

	public RutaGuardadaService() {
	}

	public List<RutaGuardadaDTO> listar(String correo) {
		Long usuarioId = buscarUsuarioId(correo);
		return rutaRepo.findByUsuarioIdOrderByFechaGuardadoDesc(usuarioId).stream().map(this::aDTO).toList();
	}

	public RutaGuardadaDTO guardar(String correo, RutaGuardadaDTO datos) {
		Long usuarioId = buscarUsuarioId(correo);
		if (rutaRepo.countByUsuarioId(usuarioId) >= MAXIMO_RUTAS) {
			throw new NegocioException("Llegaste al límite de " + MAXIMO_RUTAS + " rutas guardadas.", 400,
					"LIMITE_RUTAS");
		}
		RutaGuardada ruta = new RutaGuardada(usuarioId, datos.getNombre().trim(), datos.getOrigenLat(),
				datos.getOrigenLon(), datos.getDestinoCodigo(), datos.getDestinoNombre(), datos.getDestinoLat(),
				datos.getDestinoLon(), datos.getModo(), datos.getDistanciaKm(), datos.getDuracionMin());
		return aDTO(rutaRepo.save(ruta));
	}

	public void eliminar(String correo, Long id) {
		Long usuarioId = buscarUsuarioId(correo);
		// Se busca por id Y por dueno: nadie puede borrar rutas de otro usuario
		RutaGuardada ruta = rutaRepo.findByIdAndUsuarioId(id, usuarioId)
				.orElseThrow(() -> new NegocioException("La ruta no existe.", 404, "RUTA_NO_ENCONTRADA"));
		rutaRepo.delete(ruta);
	}

	// El correo sale del token JWT, asi cada usuario solo ve lo suyo
	private Long buscarUsuarioId(String correo) {
		return usuarioRepo.findByCorreo(correo)
				.orElseThrow(() -> new NegocioException("El usuario ya no existe.", 401, "USUARIO_NO_ENCONTRADO"))
				.getId();
	}

	private RutaGuardadaDTO aDTO(RutaGuardada ruta) {
		return new RutaGuardadaDTO(ruta.getId(), ruta.getNombre(), ruta.getOrigenLat(), ruta.getOrigenLon(),
				ruta.getDestinoCodigo(), ruta.getDestinoNombre(), ruta.getDestinoLat(), ruta.getDestinoLon(),
				ruta.getModo(), ruta.getDistanciaKm(), ruta.getDuracionMin());
	}

}
