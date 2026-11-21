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

@RestController
@RequestMapping("/api/historial")
@CrossOrigin(origins = { "http://localhost:4200" })
@SecurityRequirement(name = "bearerAuth")
public class HistorialController {

	@Autowired
	private HistorialService historialServ;

	public HistorialController() {
	}

	// Vuelos que abrio el usuario del token, del mas reciente al mas viejo: GET /api/historial
	@GetMapping
	ResponseEntity<List<HistorialDTO>> listar(@AuthenticationPrincipal Jwt jwt) {
		return new ResponseEntity<>(historialServ.listar(jwt.getSubject()), HttpStatus.OK);
	}

	// Anota que abrio un vuelo: POST /api/historial
	@PostMapping
	ResponseEntity<Void> registrar(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody HistorialDTO datos) {
		historialServ.registrar(jwt.getSubject(), datos);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// Quita un vuelo del historial: DELETE /api/historial/{vueloId}
	@DeleteMapping("/{vueloId}")
	ResponseEntity<Void> quitar(@AuthenticationPrincipal Jwt jwt, @PathVariable String vueloId) {
		historialServ.quitar(jwt.getSubject(), vueloId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// Borra todo el historial: DELETE /api/historial
	@DeleteMapping
	ResponseEntity<Void> vaciar(@AuthenticationPrincipal Jwt jwt) {
		historialServ.vaciar(jwt.getSubject());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
