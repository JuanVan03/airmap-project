package co.edu.unbosque.airmap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.airmap.dto.HistorialDTO;
import co.edu.unbosque.airmap.service.HistorialService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

/**
 * Controlador REST del historial de vuelos que ha abierto el usuario.
 * Ruta base: /api/historial.
 *
 * @author Juan David Vanegas Peña
 * @version 1.0
 */
@RestController
@RequestMapping("/api/historial")
@CrossOrigin(origins = { "http://localhost:4200" })
@SecurityRequirement(name = "bearerAuth")
public class HistorialController {

	@Autowired
	private HistorialService historialServ;

	/**
	 * Constructor vacío. Spring crea la instancia e inyecta las dependencias.
	 */
	public HistorialController() {
	}

	/**
	 * Lista los vuelos que abrió el usuario, del más reciente al más viejo.
	 *
	 * @param jwt token del usuario que tiene la sesión abierta
	 * @return historial del usuario
	 */
	@GetMapping
	ResponseEntity<List<HistorialDTO>> listar(@AuthenticationPrincipal Jwt jwt) {
		return new ResponseEntity<>(historialServ.listar(jwt.getSubject()), HttpStatus.OK);
	}

	/**
	 * Anota que el usuario abrió un vuelo.
	 *
	 * @param jwt token del usuario que tiene la sesión abierta
	 * @param datos id y número del vuelo
	 * @return respuesta vacía (204)
	 */
	@PostMapping
	ResponseEntity<Void> registrar(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody HistorialDTO datos) {
		historialServ.registrar(jwt.getSubject(), datos);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Quita un vuelo del historial.
	 *
	 * @param jwt token del usuario que tiene la sesión abierta
	 * @param vueloId id del vuelo que se quita
	 * @return respuesta vacía (204)
	 */
	@DeleteMapping("/{vueloId}")
	ResponseEntity<Void> quitar(@AuthenticationPrincipal Jwt jwt, @PathVariable String vueloId) {
		historialServ.quitar(jwt.getSubject(), vueloId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Borra todo el historial del usuario.
	 *
	 * @param jwt token del usuario que tiene la sesión abierta
	 * @return respuesta vacía (204)
	 */
	@DeleteMapping
	ResponseEntity<Void> vaciar(@AuthenticationPrincipal Jwt jwt) {
		historialServ.vaciar(jwt.getSubject());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
