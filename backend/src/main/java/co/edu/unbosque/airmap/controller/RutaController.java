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

/**
 * Controlador REST de las rutas terrestres hasta el aeropuerto (en auto, a pie o en bici).
 * Ruta base: /api/rutas.
 *
 * @author Juan David Vanegas Peña
 * @version 1.0
 */
@RestController
@RequestMapping("/api/rutas")
@CrossOrigin(origins = { "http://localhost:4200" })
@SecurityRequirement(name = "bearerAuth")
public class RutaController {

	@Autowired
	private RutaService rutaServ;

	@Autowired
	private AuditoriaService auditoria;

	/**
	 * Constructor vacío. Spring crea la instancia e inyecta las dependencias.
	 */
	public RutaController() {
	}

	/**
	 * Calcula la ruta terrestre entre dos puntos. Solo para usuarios con sesión.
	 *
	 * @param latOrigen latitud de salida
	 * @param lonOrigen longitud de salida
	 * @param latDestino latitud del aeropuerto
	 * @param lonDestino longitud del aeropuerto
	 * @param modo auto, pie o bici
	 * @return distancia, duración y puntos de la ruta
	 */
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
