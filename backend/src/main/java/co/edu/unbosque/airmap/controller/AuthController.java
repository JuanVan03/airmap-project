package co.edu.unbosque.airmap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.airmap.dto.AuthRespuestaDTO;
import co.edu.unbosque.airmap.dto.GoogleLoginDTO;
import co.edu.unbosque.airmap.dto.LoginDTO;
import co.edu.unbosque.airmap.dto.RegistroDTO;
import co.edu.unbosque.airmap.dto.UsuarioDTO;
import co.edu.unbosque.airmap.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = { "http://localhost:4200" })
public class AuthController {

	@Autowired
	private AuthService authServ;

	public AuthController() {
	}

	// Crea la cuenta y devuelve el token: POST /api/auth/registro
	@PostMapping("/registro")
	ResponseEntity<AuthRespuestaDTO> registro(@Valid @RequestBody RegistroDTO datos) {
		return new ResponseEntity<>(authServ.registrar(datos), HttpStatus.CREATED);
	}

	// Ingreso con correo y contrasena: POST /api/auth/login
	@PostMapping("/login")
	ResponseEntity<AuthRespuestaDTO> login(@Valid @RequestBody LoginDTO datos) {
		return new ResponseEntity<>(authServ.ingresar(datos), HttpStatus.OK);
	}

	// Ingreso o registro con Google: POST /api/auth/google
	@PostMapping("/google")
	ResponseEntity<AuthRespuestaDTO> google(@Valid @RequestBody GoogleLoginDTO datos) {
		return new ResponseEntity<>(authServ.ingresarConGoogle(datos), HttpStatus.OK);
	}

	// Datos del usuario del token (necesita el encabezado Authorization: Bearer)
	@GetMapping("/me")
	@SecurityRequirement(name = "bearerAuth")
	ResponseEntity<UsuarioDTO> me(@AuthenticationPrincipal Jwt jwt) {
		return new ResponseEntity<>(authServ.buscarPorCorreo(jwt.getSubject()), HttpStatus.OK);
	}

}
