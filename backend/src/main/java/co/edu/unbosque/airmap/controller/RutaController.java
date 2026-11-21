package co.edu.unbosque.airmap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.airmap.dto.RutaDTO;
import co.edu.unbosque.airmap.service.RutaService;

@RestController
@RequestMapping("/api/rutas")
@CrossOrigin(origins = { "http://localhost:4200" })
public class RutaController {

	@Autowired
	private RutaService rutaServ;

	public RutaController() {
	}

	// Ruta terrestre entre dos puntos. modo: auto, pie o bici
	@GetMapping
	ResponseEntity<RutaDTO> calcular(@RequestParam double latOrigen, @RequestParam double lonOrigen,
			@RequestParam double latDestino, @RequestParam double lonDestino,
			@RequestParam(defaultValue = "auto") String modo) {
		return new ResponseEntity<>(rutaServ.calcular(latOrigen, lonOrigen, latDestino, lonDestino, modo),
				HttpStatus.OK);
	}

}
