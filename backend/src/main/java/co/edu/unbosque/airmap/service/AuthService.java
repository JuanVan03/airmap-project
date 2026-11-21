package co.edu.unbosque.airmap.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import co.edu.unbosque.airmap.dto.AuthRespuestaDTO;
import co.edu.unbosque.airmap.dto.GoogleLoginDTO;
import co.edu.unbosque.airmap.dto.LoginDTO;
import co.edu.unbosque.airmap.dto.RegistroDTO;
import co.edu.unbosque.airmap.dto.UsuarioDTO;
import co.edu.unbosque.airmap.entity.Usuario;
import co.edu.unbosque.airmap.exception.AuthException;
import co.edu.unbosque.airmap.repository.UsuarioRepository;

@Service
public class AuthService {

	@Autowired
	private UsuarioRepository usuarioRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private GoogleService googleService;

	public AuthService() {
	}

	public AuthRespuestaDTO registrar(RegistroDTO datos) {
		String correo = limpiarCorreo(datos.getCorreo());
		if (usuarioRepo.existsByCorreo(correo)) {
			throw new AuthException("Este correo ya está registrado.", 409, "CORREO_YA_REGISTRADO");
		}
		Usuario usuario = new Usuario(datos.getNombre().trim(), correo, passwordEncoder.encode(datos.getContrasena()),
				"LOCAL");
		usuarioRepo.save(usuario);
		return respuesta(usuario);
	}

	public AuthRespuestaDTO ingresar(LoginDTO datos) {
		// Mismo mensaje si el correo no existe o la contrasena esta mal
		AuthException invalido = new AuthException("Correo o contraseña incorrectos.", 401, "CREDENCIALES_INVALIDAS");
		Usuario usuario = usuarioRepo.findByCorreo(limpiarCorreo(datos.getCorreo())).orElseThrow(() -> invalido);

		// Los usuarios de Google no tienen contrasena
		if (usuario.getContrasena() == null) {
			throw new AuthException("Esta cuenta usa Google. Ingresa con el botón \"Continuar con Google\".", 401,
					"CUENTA_GOOGLE");
		}
		if (!passwordEncoder.matches(datos.getContrasena(), usuario.getContrasena())) {
			throw invalido;
		}
		return respuesta(usuario);
	}

	public AuthRespuestaDTO ingresarConGoogle(GoogleLoginDTO datos) {
		Jwt google = googleService.verificar(datos.getCredencial());
		String correo = limpiarCorreo(google.getClaimAsString("email"));

		// Si es la primera vez se crea la cuenta, si ya existe solo se ingresa
		Usuario usuario = usuarioRepo.findByCorreo(correo).orElseGet(() -> {
			String nombre = google.getClaimAsString("name") != null ? google.getClaimAsString("name") : correo;
			return usuarioRepo.save(new Usuario(nombre, correo, null, "GOOGLE"));
		});
		return respuesta(usuario);
	}

	public UsuarioDTO buscarPorCorreo(String correo) {
		Usuario usuario = usuarioRepo.findByCorreo(correo)
				.orElseThrow(() -> new AuthException("El usuario ya no existe.", 401, "USUARIO_NO_ENCONTRADO"));
		return aDTO(usuario);
	}

	private String limpiarCorreo(String correo) {
		return correo.trim().toLowerCase();
	}

	private UsuarioDTO aDTO(Usuario usuario) {
		return new UsuarioDTO(usuario.getNombre(), usuario.getCorreo(), usuario.getProveedor());
	}

	private AuthRespuestaDTO respuesta(Usuario usuario) {
		return new AuthRespuestaDTO(jwtService.generar(usuario), aDTO(usuario));
	}

}
