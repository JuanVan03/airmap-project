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

// Solo el administrador entra aqui: SecurityConfig exige el rol ADMIN para todo /api/admin/**
@RestController
@RequestMapping("/api/admin/auditoria")
@CrossOrigin(origins = { "http://localhost:4200" })
@SecurityRequirement(name = "bearerAuth")
public class AuditoriaController {

	@Autowired
	private AuditoriaService auditoriaServ;

	public AuditoriaController() {
	}

	// Lista paginada, de lo mas nuevo a lo mas viejo, con filtros opcionales: GET /api/admin/auditoria
	@GetMapping
	ResponseEntity<PaginaAuditoriaDTO> listar(@RequestParam(defaultValue = "0") int pagina,
			@RequestParam(defaultValue = "20") int tamano, @RequestParam(defaultValue = "") String accion,
			@RequestParam(defaultValue = "") String resultado, @RequestParam(defaultValue = "") String usuario,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
		return new ResponseEntity<>(auditoriaServ.consultar(pagina, tamano, accion, resultado, usuario, desde, hasta),
				HttpStatus.OK);
	}

	// Acciones que ya hay en el registro, para llenar el filtro: GET /api/admin/auditoria/acciones
	@GetMapping("/acciones")
	ResponseEntity<List<String>> acciones() {
		return new ResponseEntity<>(auditoriaServ.acciones(), HttpStatus.OK);
	}

}
