package co.edu.unbosque.airmap.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.airmap.dto.PaginaAuditoriaDTO;
import co.edu.unbosque.airmap.service.AuditoriaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * Controlador REST de la auditoría. Solo lo puede usar el administrador, porque SecurityConfig
 * exige el rol ADMIN para todo /api/admin/**.
 *
 * @author SakiYoshida
 * @version 1.0
 */
@RestController
@RequestMapping("/api/admin/auditoria")
@CrossOrigin(origins = { "http://localhost:4200" })
@SecurityRequirement(name = "bearerAuth")
public class AuditoriaController {

	@Autowired
	private AuditoriaService auditoriaServ;

	/**
	 * Constructor vacío. Spring crea la instancia e inyecta las dependencias.
	 */
	public AuditoriaController() {
	}

	/**
	 * Lista los registros de auditoría del más nuevo al más viejo, con filtros opcionales.
	 *
	 * @param pagina número de página (empieza en 0)
	 * @param tamano registros por página
	 * @param accion filtra por acción (vacío para todas)
	 * @param resultado filtra por EXITO o FALLO (vacío para ambos)
	 * @param usuario filtra por correo del usuario
	 * @param desde fecha inicial (opcional)
	 * @param hasta fecha final, incluye todo ese día (opcional)
	 * @return página de registros
	 */
	@GetMapping
	ResponseEntity<PaginaAuditoriaDTO> listar(@RequestParam(defaultValue = "0") int pagina,
			@RequestParam(defaultValue = "20") int tamano, @RequestParam(defaultValue = "") String accion,
			@RequestParam(defaultValue = "") String resultado, @RequestParam(defaultValue = "") String usuario,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
		return new ResponseEntity<>(auditoriaServ.consultar(pagina, tamano, accion, resultado, usuario, desde, hasta),
				HttpStatus.OK);
	}

	/**
	 * Devuelve las acciones que ya existen en la auditoría, para llenar el filtro del front.
	 *
	 * @return lista de acciones sin repetir
	 */
	@GetMapping("/acciones")
	ResponseEntity<List<String>> acciones() {
		return new ResponseEntity<>(auditoriaServ.acciones(), HttpStatus.OK);
	}

}
