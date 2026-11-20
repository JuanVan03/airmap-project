package co.edu.unbosque.airmap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.airmap.dto.RespuestaVuelosDTO;
import co.edu.unbosque.airmap.dto.VueloDTO;
import co.edu.unbosque.airmap.service.VueloService;

@RestController
@RequestMapping("/api/vuelos")
@CrossOrigin(origins = { "http://localhost:4200" })
public class VueloController {

	@Autowired
	private VueloService vueloServ;

	public VueloController() {
	}

	// Vuelos para dibujar en el mapa
	@GetMapping
	ResponseEntity<RespuestaVuelosDTO> getAll() {
		return new ResponseEntity<>(vueloServ.getVuelosActivos(), HttpStatus.OK);
	}

	// Busca vuelos por indicativo: /api/vuelos?q=AVA010
	@GetMapping(params = "q")
	ResponseEntity<List<VueloDTO>> buscar(@RequestParam String q) {
		return new ResponseEntity<>(vueloServ.buscar(q), HttpStatus.OK);
	}

	// Informacion completa de un vuelo
	@GetMapping("/{id}")
	ResponseEntity<VueloDTO> getById(@PathVariable String id) {
		return new ResponseEntity<>(vueloServ.getVuelo(id), HttpStatus.OK);
	}

}
