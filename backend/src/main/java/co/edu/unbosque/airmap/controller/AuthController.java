package co.edu.unbosque.airmap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.airmap.dto.ActualizarPerfilDTO;
import co.edu.unbosque.airmap.dto.AuthRespuestaDTO;
import co.edu.unbosque.airmap.dto.GoogleLoginDTO;
import co.edu.unbosque.airmap.dto.LoginDTO;
import co.edu.unbosque.airmap.dto.MensajeDTO;
import co.edu.unbosque.airmap.dto.RecuperarDTO;
import co.edu.unbosque.airmap.dto.ReenviarCorreoDTO;
import co.edu.unbosque.airmap.dto.RegistroDTO;
import co.edu.unbosque.airmap.dto.RestablecerDTO;
import co.edu.unbosque.airmap.dto.UsuarioDTO;
import co.edu.unbosque.airmap.dto.VerificarCorreoDTO;
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

	// Crea la cuenta y envia el correo de confirmacion: POST /api/auth/registro
	@PostMapping("/registro")
	ResponseEntity<MensajeDTO> registro(@Valid @RequestBody RegistroDTO datos) {
		return new ResponseEntity<>(authServ.registrar(datos), HttpStatus.CREATED);
	}

	// Confirma el correo con el token del enlace: POST /api/auth/verificar
	@PostMapping("/verificar")
	ResponseEntity<MensajeDTO> verificar(@Valid @RequestBody VerificarCorreoDTO datos) {
		return new ResponseEntity<>(authServ.verificarCorreo(datos.getToken()), HttpStatus.OK);
	}

	// Vuelve a enviar el correo de confirmacion: POST /api/auth/reenviar-verificacion
	@PostMapping("/reenviar-verificacion")
	ResponseEntity<MensajeDTO> reenviar(@Valid @RequestBody ReenviarCorreoDTO datos) {
		return new ResponseEntity<>(authServ.reenviarVerificacion(datos.getCorreo()), HttpStatus.OK);
	}

	// Envia el correo para cambiar la contrasena: POST /api/auth/recuperar
	@PostMapping("/recuperar")
	ResponseEntity<MensajeDTO> recuperar(@Valid @RequestBody RecuperarDTO datos) {
		return new ResponseEntity<>(authServ.solicitarRestablecer(datos.getCorreo()), HttpStatus.OK);
	}

	// Guarda la contrasena nueva con el token del enlace: POST /api/auth/restablecer
	@PostMapping("/restablecer")
	ResponseEntity<MensajeDTO> restablecer(@Valid @RequestBody RestablecerDTO datos) {
		return new ResponseEntity<>(authServ.restablecerContrasena(datos), HttpStatus.OK);
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

	// Cambia el nombre del usuario del token: PUT /api/auth/perfil
	@PutMapping("/perfil")
	@SecurityRequirement(name = "bearerAuth")
	ResponseEntity<UsuarioDTO> actualizarPerfil(@AuthenticationPrincipal Jwt jwt,
			@Valid @RequestBody ActualizarPerfilDTO datos) {
		return new ResponseEntity<>(authServ.actualizarPerfil(jwt.getSubject(), datos), HttpStatus.OK);
	}

	// Deja constancia de que el usuario cerro sesion: POST /api/auth/logout
	@PostMapping("/logout")
	@SecurityRequirement(name = "bearerAuth")
	ResponseEntity<Void> logout() {
		authServ.registrarCierreSesion();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// Datos del usuario del token (necesita el encabezado Authorization: Bearer)
	@GetMapping("/me")
	@SecurityRequirement(name = "bearerAuth")
	ResponseEntity<UsuarioDTO> me(@AuthenticationPrincipal Jwt jwt) {
		return new ResponseEntity<>(authServ.buscarPorCorreo(jwt.getSubject()), HttpStatus.OK);
	}

}
