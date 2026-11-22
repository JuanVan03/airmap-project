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

@RestController
@RequestMapping("/api/transporte")
@CrossOrigin(origins = { "http://localhost:4200" })
@SecurityRequirement(name = "bearerAuth")
public class TransporteController {

	@Autowired
	private TransporteService transporteServ;

	@Autowired
	private AuditoriaService auditoria;

	public TransporteController() {
	}

	// Paradero mas cercano, rutas al aeropuerto El Dorado y proximos buses (solo usuarios con sesion)
	@GetMapping
	ResponseEntity<TransporteDTO> planear(@RequestParam double lat, @RequestParam double lon) {
		TransporteDTO transporte = transporteServ.planear(lat, lon);
		// No se guardan las coordenadas: son la ubicacion de la persona
		auditoria.registrar("TRANSPORTE_CONSULTADO", AuditoriaService.EXITO,
				"TransMilenio al aeropuerto desde el paradero " + transporte.getParadero().getNombre());
		return new ResponseEntity<>(transporte, HttpStatus.OK);
	}

	// Proximos buses de un paradero. No se audita porque el front lo pide cada 30 segundos
	@GetMapping("/tiempo-real")
	ResponseEntity<TiempoRealDTO> tiempoReal(@RequestParam String paradero) {
		return new ResponseEntity<>(transporteServ.tiempoReal(paradero), HttpStatus.OK);
	}

}
