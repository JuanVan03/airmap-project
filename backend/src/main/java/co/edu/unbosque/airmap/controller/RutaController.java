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
import co.edu.unbosque.airmap.service.AuditoriaService;
import co.edu.unbosque.airmap.service.RutaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/rutas")
@CrossOrigin(origins = { "http://localhost:4200" })
@SecurityRequirement(name = "bearerAuth")
public class RutaController {

	@Autowired
	private RutaService rutaServ;

	@Autowired
	private AuditoriaService auditoria;

	public RutaController() {
	}

	// Ruta terrestre entre dos puntos (solo usuarios con sesion). modo: auto, pie o bici
	@GetMapping
	ResponseEntity<RutaDTO> calcular(@RequestParam double latOrigen, @RequestParam double lonOrigen,
			@RequestParam double latDestino, @RequestParam double lonDestino,
			@RequestParam(defaultValue = "auto") String modo) {
		RutaDTO ruta = rutaServ.calcular(latOrigen, lonOrigen, latDestino, lonDestino, modo);
		// No se guardan las coordenadas: son la ubicacion de la persona
		auditoria.registrar("RUTA_CALCULADA", AuditoriaService.EXITO, "Ruta terrestre en modo " + modo);
		return new ResponseEntity<>(ruta, HttpStatus.OK);
	}

}
