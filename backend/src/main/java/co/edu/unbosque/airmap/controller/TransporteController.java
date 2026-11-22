package co.edu.unbosque.airmap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.airmap.dto.TiempoRealDTO;
import co.edu.unbosque.airmap.dto.TransporteDTO;
import co.edu.unbosque.airmap.service.AuditoriaService;
import co.edu.unbosque.airmap.service.TransporteService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * Controlador REST para llegar al aeropuerto El Dorado en TransMilenio: paradero más cercano,
 * rutas directas y próximos buses. Ruta base: /api/transporte.
 *
 * @author Juan David Vanegas Peña
 * @version 1.0
 */
@RestController
@RequestMapping("/api/transporte")
@CrossOrigin(origins = { "http://localhost:4200" })
@SecurityRequirement(name = "bearerAuth")
public class TransporteController {

	@Autowired
	private TransporteService transporteServ;

	@Autowired
	private AuditoriaService auditoria;

	/**
	 * Constructor vacío. Spring crea la instancia e inyecta las dependencias.
	 */
	public TransporteController() {
	}

	/**
	 * Busca el paradero más cercano, las rutas de TransMilenio al aeropuerto El Dorado y los próximos buses.
	 *
	 * @param lat latitud del usuario
	 * @param lon longitud del usuario
	 * @return paradero, caminata, rutas y buses
	 */
	@GetMapping
	ResponseEntity<TransporteDTO> planear(@RequestParam double lat, @RequestParam double lon) {
		TransporteDTO transporte = transporteServ.planear(lat, lon);
		// No se guardan las coordenadas: son la ubicacion de la persona
		auditoria.registrar("TRANSPORTE_CONSULTADO", AuditoriaService.EXITO,
				"TransMilenio al aeropuerto desde el paradero " + transporte.getParadero().getNombre());
		return new ResponseEntity<>(transporte, HttpStatus.OK);
	}

	/**
	 * Próximos buses de un paradero. No se audita porque el front lo pide cada 30 segundos.
	 *
	 * @param paradero id del paradero
	 * @return próximos buses y su posición
	 */
	@GetMapping("/tiempo-real")
	ResponseEntity<TiempoRealDTO> tiempoReal(@RequestParam String paradero) {
		return new ResponseEntity<>(transporteServ.tiempoReal(paradero), HttpStatus.OK);
	}

}
