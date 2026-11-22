package co.edu.unbosque.airmap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.airmap.dto.PreferenciasDTO;
import co.edu.unbosque.airmap.service.PreferenciasService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

/**
 * Controlador REST de la apariencia de la página (tema, letra, tamaño, color e ícono del avión)
 * que eligió cada usuario. Ruta base: /api/preferencias.
 *
 * @author SakiYoshida
 * @version 1.0
 */
@RestController
@RequestMapping("/api/preferencias")
@CrossOrigin(origins = { "http://localhost:4200" })
@SecurityRequirement(name = "bearerAuth")
public class PreferenciasController {

	@Autowired
	private PreferenciasService preferenciasServ;

	/**
	 * Constructor vacío. Spring crea la instancia e inyecta las dependencias.
	 */
	public PreferenciasController() {
	}

	/**
	 * Devuelve la apariencia guardada del usuario.
	 *
	 * @param jwt token del usuario que tiene la sesión abierta
	 * @return preferencias del usuario (o las de por defecto)
	 */
	@GetMapping
	ResponseEntity<PreferenciasDTO> obtener(@AuthenticationPrincipal Jwt jwt) {
		return new ResponseEntity<>(preferenciasServ.obtener(jwt.getSubject()), HttpStatus.OK);
	}

	/**
	 * Guarda la apariencia. Para restablecer, el front envía los valores por defecto.
	 *
	 * @param jwt token del usuario que tiene la sesión abierta
	 * @param datos tema, letra, tamaño, color e ícono del avión
	 * @return preferencias guardadas
	 */
	@PutMapping
	ResponseEntity<PreferenciasDTO> guardar(@AuthenticationPrincipal Jwt jwt,
			@Valid @RequestBody PreferenciasDTO datos) {
		return new ResponseEntity<>(preferenciasServ.guardar(jwt.getSubject(), datos), HttpStatus.OK);
	}

}
