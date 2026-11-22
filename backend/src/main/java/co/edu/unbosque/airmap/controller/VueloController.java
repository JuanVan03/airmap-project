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
import co.edu.unbosque.airmap.service.AuditoriaService;
import co.edu.unbosque.airmap.service.VueloService;

/**
 * Controlador REST de los vuelos. Entrega los vuelos que se dibujan en el mapa, la búsqueda por
 * indicativo y el detalle de un vuelo. Ruta base: /api/vuelos (pública).
 *
 * @author SakiYoshida
 * @version 1.0
 */
@RestController
@RequestMapping("/api/vuelos")
@CrossOrigin(origins = { "http://localhost:4200" })
public class VueloController {

	@Autowired
	private VueloService vueloServ;

	@Autowired
	private AuditoriaService auditoria;

	/**
	 * Constructor vacío. Spring crea la instancia e inyecta las dependencias.
	 */
	public VueloController() {
	}

	/**
	 * Devuelve los vuelos que se dibujan en el mapa.
	 *
	 * @return vuelos activos y hora de la consulta
	 */
	@GetMapping
	ResponseEntity<RespuestaVuelosDTO> getAll() {
		return new ResponseEntity<>(vueloServ.getVuelosActivos(), HttpStatus.OK);
	}

	/**
	 * Busca vuelos por indicativo. Ejemplo: /api/vuelos?q=AVA010
	 *
	 * @param q indicativo o parte de él
	 * @return vuelos encontrados (máximo 20)
	 */
	@GetMapping(params = "q")
	ResponseEntity<List<VueloDTO>> buscar(@RequestParam String q) {
		List<VueloDTO> encontrados = vueloServ.buscar(q);
		auditoria.registrar("BUSQUEDA_VUELO", AuditoriaService.EXITO,
				"Buscó \"" + q + "\" (" + encontrados.size() + " resultados)");
		return new ResponseEntity<>(encontrados, HttpStatus.OK);
	}

	/**
	 * Devuelve la información completa de un vuelo.
	 *
	 * @param id id del vuelo
	 * @return detalle del vuelo
	 */
	@GetMapping("/{id}")
	ResponseEntity<VueloDTO> getById(@PathVariable String id) {
		VueloDTO vuelo = vueloServ.getVuelo(id);
		auditoria.registrar("VUELO_CONSULTADO", AuditoriaService.EXITO, "Vuelo " + vuelo.getNumeroVuelo());
		return new ResponseEntity<>(vuelo, HttpStatus.OK);
	}

}
