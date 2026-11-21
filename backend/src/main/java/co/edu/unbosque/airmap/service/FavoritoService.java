package co.edu.unbosque.airmap.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.airmap.dto.FavoritoDTO;
import co.edu.unbosque.airmap.entity.Favorito;
import co.edu.unbosque.airmap.exception.NegocioException;
import co.edu.unbosque.airmap.repository.FavoritoRepository;
import co.edu.unbosque.airmap.repository.UsuarioRepository;

@Service
public class FavoritoService {

	// Maximo de vuelos favoritos por usuario
	private static final int MAXIMO_FAVORITOS = 30;

	@Autowired
	private FavoritoRepository favoritoRepo;

	@Autowired
	private UsuarioRepository usuarioRepo;

	@Autowired
	private AuditoriaService auditoria;

	public FavoritoService() {
	}

	public List<FavoritoDTO> listar(String correo) {
		Long usuarioId = buscarUsuarioId(correo);
		return favoritoRepo.findByUsuarioIdOrderByFechaGuardadoDesc(usuarioId).stream().map(this::aDTO).toList();
	}

	public FavoritoDTO agregar(String correo, FavoritoDTO datos) {
		Long usuarioId = buscarUsuarioId(correo);

		// Si ya era favorito se devuelve el mismo, sin repetirlo
		Optional<Favorito> existente = favoritoRepo.findByUsuarioIdAndVueloId(usuarioId, datos.getVueloId());
		if (existente.isPresent()) {
			return aDTO(existente.get());
		}
		if (favoritoRepo.countByUsuarioId(usuarioId) >= MAXIMO_FAVORITOS) {
			throw new NegocioException("Llegaste al límite de " + MAXIMO_FAVORITOS + " vuelos favoritos.", 400,
					"LIMITE_FAVORITOS");
		}
		Favorito favorito = new Favorito(usuarioId, datos.getVueloId(), datos.getNumeroVuelo());
		FavoritoDTO guardado = aDTO(favoritoRepo.save(favorito));
		auditoria.registrar("FAVORITO_AGREGADO", correo, AuditoriaService.EXITO, "Vuelo " + datos.getNumeroVuelo());
		return guardado;
	}

	public void quitar(String correo, String vueloId) {
		Long usuarioId = buscarUsuarioId(correo);
		Favorito favorito = favoritoRepo.findByUsuarioIdAndVueloId(usuarioId, vueloId).orElseThrow(
				() -> new NegocioException("Ese vuelo no está en tus favoritos.", 404, "FAVORITO_NO_ENCONTRADO"));
		favoritoRepo.delete(favorito);
		auditoria.registrar("FAVORITO_ELIMINADO", correo, AuditoriaService.EXITO, "Vuelo " + favorito.getNumeroVuelo());
	}

	// El correo sale del token JWT, asi cada usuario solo ve lo suyo
	private Long buscarUsuarioId(String correo) {
		return usuarioRepo.findByCorreo(correo)
				.orElseThrow(() -> new NegocioException("El usuario ya no existe.", 401, "USUARIO_NO_ENCONTRADO"))
				.getId();
	}

	private FavoritoDTO aDTO(Favorito favorito) {
		return new FavoritoDTO(favorito.getId(), favorito.getVueloId(), favorito.getNumeroVuelo());
	}

}
