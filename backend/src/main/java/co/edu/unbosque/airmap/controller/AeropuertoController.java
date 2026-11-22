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
import co.edu.unbosque.airmap.service.AuditoriaService;

/**
 * Controlador REST para buscar aeropuertos por código, nombre o ciudad.
 * Ruta base: /api/aeropuertos (pública).
 *
 * @author SakiYoshida
 * @version 1.0
 */
@RestController
@RequestMapping("/api/aeropuertos")
@CrossOrigin(origins = { "http://localhost:4200" })
public class AeropuertoController {

	@Autowired
	private AeropuertoService aeropuertoServ;

	@Autowired
	private AuditoriaService auditoria;

	/**
	 * Constructor vacío. Spring crea la instancia e inyecta las dependencias.
	 */
	public AeropuertoController() {
	}

	/**
	 * Busca aeropuertos por código, nombre o ciudad. Ejemplo: /api/aeropuertos?q=bogota
	 *
	 * @param q texto que escribió el usuario
	 * @return lista de aeropuertos encontrados (máximo 8)
	 */
	@GetMapping
	ResponseEntity<List<AeropuertoDTO>> buscar(@RequestParam String q) {
		List<AeropuertoDTO> encontrados = aeropuertoServ.buscar(q);
		auditoria.registrar("BUSQUEDA_AEROPUERTO", AuditoriaService.EXITO,
				"Buscó \"" + q + "\" (" + encontrados.size() + " resultados)");
		return new ResponseEntity<>(encontrados, HttpStatus.OK);
	}

}
