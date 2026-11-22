package co.edu.unbosque.airmap.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.airmap.dto.RutaGuardadaDTO;
import co.edu.unbosque.airmap.entity.RutaGuardada;
import co.edu.unbosque.airmap.exception.NegocioException;
import co.edu.unbosque.airmap.repository.RutaGuardadaRepository;
import co.edu.unbosque.airmap.repository.UsuarioRepository;

/**
 * Servicio de las rutas terrestres que guarda cada usuario.
 *
 * @author SakiYoshida
 * @version 1.0
 */
@Service
public class RutaGuardadaService {

	// Maximo de rutas guardadas por usuario
	private static final int MAXIMO_RUTAS = 30;

	@Autowired
	private RutaGuardadaRepository rutaRepo;

	@Autowired
	private UsuarioRepository usuarioRepo;

	@Autowired
	private AuditoriaService auditoria;

	/**
	 * Constructor vacío. Spring crea la instancia e inyecta las dependencias.
	 */
	public RutaGuardadaService() {
	}

	/**
	 * Lista las rutas guardadas del usuario.
	 *
	 * @param correo correo del usuario (sale del token JWT)
	 * @return rutas de la más nueva a la más vieja
	 */
	public List<RutaGuardadaDTO> listar(String correo) {
		Long usuarioId = buscarUsuarioId(correo);
		return rutaRepo.findByUsuarioIdOrderByFechaGuardadoDesc(usuarioId).stream().map(this::aDTO).toList();
	}

	/**
	 * Guarda una ruta nueva del usuario.
	 *
	 * @param correo correo del usuario (sale del token JWT)
	 * @param datos datos de la ruta
	 * @return ruta guardada
	 * @throws NegocioException si llegó al límite de rutas
	 */
	public RutaGuardadaDTO guardar(String correo, RutaGuardadaDTO datos) {
		Long usuarioId = buscarUsuarioId(correo);
		if (rutaRepo.countByUsuarioId(usuarioId) >= MAXIMO_RUTAS) {
			throw new NegocioException("Llegaste al límite de " + MAXIMO_RUTAS + " rutas guardadas.", 400,
					"LIMITE_RUTAS");
		}
		RutaGuardada ruta = new RutaGuardada(usuarioId, datos.getNombre().trim(), datos.getOrigenLat(),
				datos.getOrigenLon(), datos.getDestinoCodigo(), datos.getDestinoNombre(), datos.getDestinoLat(),
				datos.getDestinoLon(), datos.getModo(), datos.getDistanciaKm(), datos.getDuracionMin());
		RutaGuardadaDTO guardada = aDTO(rutaRepo.save(ruta));
		auditoria.registrar("RUTA_GUARDADA", correo, AuditoriaService.EXITO,
				"Ruta \"" + ruta.getNombre() + "\" hacia " + ruta.getDestinoCodigo());
		return guardada;
	}

	/**
	 * Borra una ruta. Se busca por id y por dueño, así nadie puede borrar rutas de otro usuario.
	 *
	 * @param correo correo del usuario (sale del token JWT)
	 * @param id id de la ruta
	 * @throws NegocioException si la ruta no existe o no es del usuario
	 */
	public void eliminar(String correo, Long id) {
		Long usuarioId = buscarUsuarioId(correo);
		// Se busca por id Y por dueno: nadie puede borrar rutas de otro usuario
		RutaGuardada ruta = rutaRepo.findByIdAndUsuarioId(id, usuarioId)
				.orElseThrow(() -> new NegocioException("La ruta no existe.", 404, "RUTA_NO_ENCONTRADA"));
		rutaRepo.delete(ruta);
		auditoria.registrar("RUTA_ELIMINADA", correo, AuditoriaService.EXITO, "Ruta \"" + ruta.getNombre() + "\"");
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
