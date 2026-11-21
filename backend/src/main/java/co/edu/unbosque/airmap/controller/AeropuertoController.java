package co.edu.unbosque.airmap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.airmap.dto.AeropuertoDTO;
import co.edu.unbosque.airmap.service.AeropuertoService;

@RestController
@RequestMapping("/api/aeropuertos")
@CrossOrigin(origins = { "http://localhost:4200" })
public class AeropuertoController {

	@Autowired
	private AeropuertoService aeropuertoServ;

	public AeropuertoController() {
	}

	// Busca por codigo, nombre o ciudad: /api/aeropuertos?q=bogota
	@GetMapping
	ResponseEntity<List<AeropuertoDTO>> buscar(@RequestParam String q) {
		return new ResponseEntity<>(aeropuertoServ.buscar(q), HttpStatus.OK);
	}

}
