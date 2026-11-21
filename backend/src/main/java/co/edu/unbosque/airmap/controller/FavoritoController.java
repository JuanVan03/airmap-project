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

import co.edu.unbosque.airmap.dto.FavoritoDTO;
import co.edu.unbosque.airmap.service.FavoritoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/favoritos")
@CrossOrigin(origins = { "http://localhost:4200" })
@SecurityRequirement(name = "bearerAuth")
public class FavoritoController {

	@Autowired
	private FavoritoService favoritoServ;

	public FavoritoController() {
	}

	// Favoritos del usuario del token: GET /api/favoritos
	@GetMapping
	ResponseEntity<List<FavoritoDTO>> listar(@AuthenticationPrincipal Jwt jwt) {
		return new ResponseEntity<>(favoritoServ.listar(jwt.getSubject()), HttpStatus.OK);
	}

	// Marca un vuelo como favorito: POST /api/favoritos
	@PostMapping
	ResponseEntity<FavoritoDTO> agregar(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody FavoritoDTO datos) {
		return new ResponseEntity<>(favoritoServ.agregar(jwt.getSubject(), datos), HttpStatus.CREATED);
	}

	// Quita el favorito por el id del vuelo: DELETE /api/favoritos/{vueloId}
	@DeleteMapping("/{vueloId}")
	ResponseEntity<Void> quitar(@AuthenticationPrincipal Jwt jwt, @PathVariable String vueloId) {
		favoritoServ.quitar(jwt.getSubject(), vueloId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
