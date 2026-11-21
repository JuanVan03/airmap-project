package co.edu.unbosque.airmap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.airmap.dto.AsistentePreguntaDTO;
import co.edu.unbosque.airmap.dto.AsistenteRespuestaDTO;
import co.edu.unbosque.airmap.service.AsistenteService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/asistente")
@CrossOrigin(origins = { "http://localhost:4200" })
@SecurityRequirement(name = "bearerAuth")
public class AsistenteController {

	@Autowired
	private AsistenteService asistenteServ;

	public AsistenteController() {
	}

	// Pregunta al asistente de IA (solo usuarios con sesion): POST /api/asistente
	@PostMapping
	ResponseEntity<AsistenteRespuestaDTO> preguntar(@Valid @RequestBody AsistentePreguntaDTO datos) {
		return new ResponseEntity<>(asistenteServ.preguntar(datos), HttpStatus.OK);
	}

}
