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

/**
 * Controlador REST de la cuenta del usuario: registro, confirmación del correo, ingreso con
 * contraseña o con Google, cambio de contraseña y datos del perfil. Ruta base: /api/auth.
 *
 * @author Juan David Vanegas Peña
 * @version 1.0
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = { "http://localhost:4200" })
public class AuthController {

	@Autowired
	private AuthService authServ;

	/**
	 * Constructor vacío. Spring crea la instancia e inyecta las dependencias.
	 */
	public AuthController() {
	}

	/**
	 * Crea una cuenta nueva y envía el correo de confirmación.
	 *
	 * @param datos nombre, correo y contraseña
	 * @return mensaje para revisar el correo
	 */
	@PostMapping("/registro")
	ResponseEntity<MensajeDTO> registro(@Valid @RequestBody RegistroDTO datos) {
		return new ResponseEntity<>(authServ.registrar(datos), HttpStatus.CREATED);
	}

	/**
	 * Confirma el correo con el token del enlace.
	 *
	 * @param datos token que llegó en el correo
	 * @return mensaje de cuenta confirmada
	 */
	@PostMapping("/verificar")
	ResponseEntity<MensajeDTO> verificar(@Valid @RequestBody VerificarCorreoDTO datos) {
		return new ResponseEntity<>(authServ.verificarCorreo(datos.getToken()), HttpStatus.OK);
	}

	/**
	 * Vuelve a enviar el enlace de confirmación.
	 *
	 * @param datos correo de la cuenta
	 * @return mensaje genérico (no dice si el correo existe)
	 */
	@PostMapping("/reenviar-verificacion")
	ResponseEntity<MensajeDTO> reenviar(@Valid @RequestBody ReenviarCorreoDTO datos) {
		return new ResponseEntity<>(authServ.reenviarVerificacion(datos.getCorreo()), HttpStatus.OK);
	}

	/**
	 * Envía el enlace para restablecer la contraseña.
	 *
	 * @param datos correo de la cuenta
	 * @return mensaje genérico (no dice si el correo existe)
	 */
	@PostMapping("/recuperar")
	ResponseEntity<MensajeDTO> recuperar(@Valid @RequestBody RecuperarDTO datos) {
		return new ResponseEntity<>(authServ.solicitarRestablecer(datos.getCorreo()), HttpStatus.OK);
	}

	/**
	 * Guarda la contraseña nueva usando el token del enlace.
	 *
	 * @param datos token y contraseña nueva
	 * @return mensaje de contraseña cambiada
	 */
	@PostMapping("/restablecer")
	ResponseEntity<MensajeDTO> restablecer(@Valid @RequestBody RestablecerDTO datos) {
		return new ResponseEntity<>(authServ.restablecerContrasena(datos), HttpStatus.OK);
	}

	/**
	 * Ingresa con correo y contraseña.
	 *
	 * @param datos correo y contraseña
	 * @return token JWT y datos del usuario
	 */
	@PostMapping("/login")
	ResponseEntity<AuthRespuestaDTO> login(@Valid @RequestBody LoginDTO datos) {
		return new ResponseEntity<>(authServ.ingresar(datos), HttpStatus.OK);
	}

	/**
	 * Ingresa con Google. Si es la primera vez, crea la cuenta.
	 *
	 * @param datos credencial del botón de Google
	 * @return token JWT y datos del usuario
	 */
	@PostMapping("/google")
	ResponseEntity<AuthRespuestaDTO> google(@Valid @RequestBody GoogleLoginDTO datos) {
		return new ResponseEntity<>(authServ.ingresarConGoogle(datos), HttpStatus.OK);
	}

	/**
	 * Cambia el nombre del usuario que tiene la sesión abierta.
	 *
	 * @param jwt token del usuario que tiene la sesión abierta
	 * @param datos nombre nuevo
	 * @return datos actualizados del usuario
	 */
	@PutMapping("/perfil")
	@SecurityRequirement(name = "bearerAuth")
	ResponseEntity<UsuarioDTO> actualizarPerfil(@AuthenticationPrincipal Jwt jwt,
			@Valid @RequestBody ActualizarPerfilDTO datos) {
		return new ResponseEntity<>(authServ.actualizarPerfil(jwt.getSubject(), datos), HttpStatus.OK);
	}

	/**
	 * Registra en la auditoría que el usuario cerró sesión. El token sigue valiendo hasta que vence.
	 *
	 * @return respuesta vacía (204)
	 */
	@PostMapping("/logout")
	@SecurityRequirement(name = "bearerAuth")
	ResponseEntity<Void> logout() {
		authServ.registrarCierreSesion();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Devuelve los datos del usuario que tiene la sesión abierta.
	 *
	 * @param jwt token del usuario que tiene la sesión abierta
	 * @return datos del usuario
	 */
	@GetMapping("/me")
	@SecurityRequirement(name = "bearerAuth")
	ResponseEntity<UsuarioDTO> me(@AuthenticationPrincipal Jwt jwt) {
		return new ResponseEntity<>(authServ.buscarPorCorreo(jwt.getSubject()), HttpStatus.OK);
	}

}
