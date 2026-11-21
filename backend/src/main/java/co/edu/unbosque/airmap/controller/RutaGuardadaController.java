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

import co.edu.unbosque.airmap.dto.RutaGuardadaDTO;
import co.edu.unbosque.airmap.service.RutaGuardadaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/rutas-guardadas")
@CrossOrigin(origins = { "http://localhost:4200" })
@SecurityRequirement(name = "bearerAuth")
public class RutaGuardadaController {

	@Autowired
	private RutaGuardadaService rutaServ;

	public RutaGuardadaController() {
	}

	// Rutas del usuario del token: GET /api/rutas-guardadas
	@GetMapping
	ResponseEntity<List<RutaGuardadaDTO>> listar(@AuthenticationPrincipal Jwt jwt) {
		return new ResponseEntity<>(rutaServ.listar(jwt.getSubject()), HttpStatus.OK);
	}

	// Guarda una ruta: POST /api/rutas-guardadas
	@PostMapping
	ResponseEntity<RutaGuardadaDTO> guardar(@AuthenticationPrincipal Jwt jwt,
			@Valid @RequestBody RutaGuardadaDTO datos) {
		return new ResponseEntity<>(rutaServ.guardar(jwt.getSubject(), datos), HttpStatus.CREATED);
	}

	// Borra una ruta propia: DELETE /api/rutas-guardadas/{id}
	@DeleteMapping("/{id}")
	ResponseEntity<Void> eliminar(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
		rutaServ.eliminar(jwt.getSubject(), id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
