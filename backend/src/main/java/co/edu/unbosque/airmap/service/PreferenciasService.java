package co.edu.unbosque.airmap.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.airmap.dto.PreferenciasDTO;
import co.edu.unbosque.airmap.entity.Preferencias;
import co.edu.unbosque.airmap.exception.NegocioException;
import co.edu.unbosque.airmap.repository.PreferenciasRepository;
import co.edu.unbosque.airmap.repository.UsuarioRepository;

@Service
public class PreferenciasService {

	@Autowired
	private PreferenciasRepository preferenciasRepo;

	@Autowired
	private UsuarioRepository usuarioRepo;

	@Autowired
	private AuditoriaService auditoria;

	public PreferenciasService() {
	}

	// Si el usuario nunca ha cambiado nada, se devuelve el diseño original de AirMap
	public PreferenciasDTO obtener(String correo) {
		Long usuarioId = buscarUsuarioId(correo);
		return preferenciasRepo.findByUsuarioId(usuarioId).map(this::aDTO).orElse(porDefecto());
	}

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
