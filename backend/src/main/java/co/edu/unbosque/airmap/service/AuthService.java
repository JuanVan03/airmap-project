package co.edu.unbosque.airmap.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.airmap.dto.ActualizarPerfilDTO;
import co.edu.unbosque.airmap.dto.AuthRespuestaDTO;
import co.edu.unbosque.airmap.dto.GoogleLoginDTO;
import co.edu.unbosque.airmap.dto.LoginDTO;
import co.edu.unbosque.airmap.dto.MensajeDTO;
import co.edu.unbosque.airmap.dto.RegistroDTO;
import co.edu.unbosque.airmap.dto.RestablecerDTO;
import co.edu.unbosque.airmap.dto.UsuarioDTO;
import co.edu.unbosque.airmap.entity.TokenCorreo;
import co.edu.unbosque.airmap.entity.Usuario;
import co.edu.unbosque.airmap.exception.AuthException;
import co.edu.unbosque.airmap.exception.NegocioException;
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

	@Autowired
	private TokenCorreoService tokenServ;

	@Autowired
	private CorreoService correoServ;

	@Autowired
	private AuditoriaService auditoria;

	// Minutos que dura el enlace de confirmacion (24 horas)
	private static final int MINUTOS_VERIFICACION = 24 * 60;

	// Minutos que dura el enlace para restablecer la contrasena (1 hora)
	private static final int MINUTOS_RESTABLECER = 60;

	public AuthService() {
	}

	// Crea la cuenta sin verificar y manda el correo de confirmacion. No devuelve token:
	// la persona solo puede ingresar despues de confirmar su correo.
	public MensajeDTO registrar(RegistroDTO datos) {
		String correo = limpiarCorreo(datos.getCorreo());
		if (usuarioRepo.existsByCorreo(correo)) {
			auditoria.registrar("REGISTRO", correo, AuditoriaService.FALLO, "El correo ya estaba registrado");
			throw new AuthException("Este correo ya está registrado. Si no confirmaste tu cuenta, intenta ingresar y te reenviamos el correo.",
					409, "CORREO_YA_REGISTRADO");
		}
		Usuario usuario = new Usuario(datos.getNombre().trim(), correo, passwordEncoder.encode(datos.getContrasena()),
				"LOCAL");
		usuarioRepo.save(usuario);
		auditoria.registrar("REGISTRO", correo, AuditoriaService.EXITO,
				"Cuenta creada con correo y contraseña; falta confirmar el correo");

		enviarVerificacion(usuario);
		return new MensajeDTO("Te enviamos un correo a " + correo + " para confirmar tu cuenta.");
	}

	// Confirma el correo con el enlace que llego a la bandeja de entrada
	@Transactional
	public MensajeDTO verificarCorreo(String token) {
		Usuario usuario;
		try {
			usuario = tokenServ.consumir(token, TokenCorreo.VERIFICACION);
		} catch (NegocioException ex) {
			auditoria.registrar("VERIFICACION_FALLIDA", AuditoriaService.FALLO, ex.getMessage());
			throw ex;
		}
		usuario.setVerificado(true);
		usuarioRepo.save(usuario);
		auditoria.registrar("CORREO_VERIFICADO", usuario.getCorreo(), AuditoriaService.EXITO,
				"Confirmó su correo con el enlace");
		return new MensajeDTO("¡Correo confirmado! Ya puedes ingresar a AirMap.");
	}

	// Vuelve a enviar el enlace. La respuesta es la misma exista o no el correo,
	// para que nadie pueda averiguar quien tiene cuenta.
	public MensajeDTO reenviarVerificacion(String correo) {
		String correoLimpio = limpiarCorreo(correo);
		Usuario usuario = usuarioRepo.findByCorreo(correoLimpio).orElse(null);
		if (usuario != null && !usuario.isVerificado()) {
			enviarVerificacion(usuario);
			auditoria.registrar("REENVIO_VERIFICACION", correoLimpio, AuditoriaService.EXITO,
					"Pidió otro correo de confirmación");
		} else {
			auditoria.registrar("REENVIO_VERIFICACION", correoLimpio, AuditoriaService.FALLO,
					"El correo no está registrado o ya estaba confirmado");
		}
		return new MensajeDTO("Si el correo está pendiente de confirmar, te enviamos un nuevo enlace.");
	}

	// Envia el enlace para cambiar la contrasena. La respuesta es siempre la misma, exista o no
	// el correo, para que nadie pueda averiguar quien tiene cuenta.
	public MensajeDTO solicitarRestablecer(String correo) {
		String correoLimpio = limpiarCorreo(correo);
		Usuario usuario = usuarioRepo.findByCorreo(correoLimpio).orElse(null);

		if (usuario == null) {
			auditoria.registrar("SOLICITUD_RESTABLECER", correoLimpio, AuditoriaService.FALLO, "El correo no está registrado");
		} else if (usuario.getContrasena() == null) {
			// Las cuentas de Google no tienen contrasena que restablecer
			auditoria.registrar("SOLICITUD_RESTABLECER", correoLimpio, AuditoriaService.FALLO, "La cuenta usa Google y no tiene contraseña");
		} else if ("ADMIN".equals(usuario.getRol())) {
			// La contrasena del administrador solo se cambia en el .env del back
			auditoria.registrar("SOLICITUD_RESTABLECER", correoLimpio, AuditoriaService.FALLO, "No aplica a la cuenta de administrador");
		} else {
			try {
				String token = tokenServ.crear(usuario, TokenCorreo.RESTABLECER, MINUTOS_RESTABLECER);
				correoServ.enviarRestablecer(usuario, token);
				auditoria.registrar("SOLICITUD_RESTABLECER", correoLimpio, AuditoriaService.EXITO,
						"Se envió el enlace para cambiar la contraseña");
			} catch (NegocioException ex) {
				// Si pidio otro correo muy seguido no se envia, y la respuesta sigue siendo la misma
				if (!"CORREO_MUY_SEGUIDO".equals(ex.getCodigo())) {
					auditoria.registrar("SOLICITUD_RESTABLECER", correoLimpio, AuditoriaService.FALLO, ex.getMessage());
					throw ex;
				}
				auditoria.registrar("SOLICITUD_RESTABLECER", correoLimpio, AuditoriaService.FALLO,
						"Pidió el enlace demasiado seguido");
			}
		}
		return new MensajeDTO("Si el correo está registrado, te enviamos un enlace para cambiar tu contraseña.");
	}

	// Guarda la contrasena nueva con el enlace que llego al correo
	@Transactional
	public MensajeDTO restablecerContrasena(RestablecerDTO datos) {
		Usuario usuario;
		try {
			usuario = tokenServ.consumir(datos.getToken(), TokenCorreo.RESTABLECER);
		} catch (NegocioException ex) {
			auditoria.registrar("RESTABLECER_FALLIDO", AuditoriaService.FALLO, ex.getMessage());
			throw ex;
		}
		usuario.setContrasena(passwordEncoder.encode(datos.getContrasena()));
		// Abrir el enlace desde el correo comprueba que el correo es suyo
		usuario.setVerificado(true);
		usuarioRepo.save(usuario);
		auditoria.registrar("CAMBIO_CONTRASENA", usuario.getCorreo(), AuditoriaService.EXITO,
				"Cambió su contraseña con el enlace del correo");
		return new MensajeDTO("Tu contraseña se cambió. Ya puedes ingresar con la nueva.");
	}

	public AuthRespuestaDTO ingresar(LoginDTO datos) {
		String correo = limpiarCorreo(datos.getCorreo());

		// Mismo mensaje si el correo no existe o la contrasena esta mal
		AuthException invalido = new AuthException("Correo o contraseña incorrectos.", 401, "CREDENCIALES_INVALIDAS");
		Usuario usuario = usuarioRepo.findByCorreo(correo).orElse(null);
		if (usuario == null) {
			auditoria.registrar("LOGIN_FALLIDO", correo, AuditoriaService.FALLO, "El correo no está registrado");
			throw invalido;
		}

		// Los usuarios de Google no tienen contrasena
		if (usuario.getContrasena() == null) {
			auditoria.registrar("LOGIN_FALLIDO", correo, AuditoriaService.FALLO, "Cuenta de Google intentó entrar con contraseña");
			throw new AuthException("Esta cuenta usa Google. Ingresa con el botón \"Continuar con Google\".", 401,
					"CUENTA_GOOGLE");
		}
		if (!passwordEncoder.matches(datos.getContrasena(), usuario.getContrasena())) {
			auditoria.registrar("LOGIN_FALLIDO", correo, AuditoriaService.FALLO, "Contraseña incorrecta");
			throw invalido;
		}
		// Se revisa despues de la contrasena para no revelar quien tiene cuenta
		if (!usuario.isVerificado()) {
			auditoria.registrar("LOGIN_FALLIDO", correo, AuditoriaService.FALLO, "El correo todavía no está confirmado");
			throw new AuthException("Debes confirmar tu correo antes de ingresar. Revisa tu bandeja de entrada y la carpeta de spam.",
					403, "CORREO_NO_VERIFICADO");
		}
		auditoria.registrar("LOGIN_EXITOSO", correo, AuditoriaService.EXITO,
				"ADMIN".equals(usuario.getRol()) ? "Ingreso del administrador" : "Ingreso con correo y contraseña");
		return respuesta(usuario);
	}

	public AuthRespuestaDTO ingresarConGoogle(GoogleLoginDTO datos) {
		Jwt google;
		try {
			google = googleService.verificar(datos.getCredencial());
		} catch (AuthException ex) {
			auditoria.registrar("LOGIN_GOOGLE", AuditoriaService.FALLO, "Credencial de Google no válida");
			throw ex;
		}
		String correo = limpiarCorreo(google.getClaimAsString("email"));

		// Si es la primera vez se crea la cuenta, si ya existe solo se ingresa
		Usuario existente = usuarioRepo.findByCorreo(correo).orElse(null);
		if (existente != null && "ADMIN".equals(existente.getRol())) {
			// El administrador solo entra con la contrasena del .env
			auditoria.registrar("LOGIN_GOOGLE", correo, AuditoriaService.FALLO, "Intento de entrar con Google a la cuenta de administrador");
			throw new AuthException("Esta cuenta no puede ingresar con Google.", 403, "ADMIN_SIN_GOOGLE");
		}

		boolean esNuevo = existente == null;
		Usuario usuario = existente;
		if (esNuevo) {
			String nombre = google.getClaimAsString("name") != null ? google.getClaimAsString("name") : correo;
			usuario = usuarioRepo.save(new Usuario(nombre, correo, null, "GOOGLE"));
		}
		// Google ya comprobo que el correo es de la persona
		if (!usuario.isVerificado()) {
			usuario.setVerificado(true);
			usuarioRepo.save(usuario);
		}
		auditoria.registrar(esNuevo ? "REGISTRO_GOOGLE" : "LOGIN_GOOGLE", correo, AuditoriaService.EXITO,
				esNuevo ? "Cuenta creada con Google" : "Ingreso con Google");
		return respuesta(usuario);
	}

	// Cambia el nombre del usuario del token. El correo no se puede cambiar.
	public UsuarioDTO actualizarPerfil(String correo, ActualizarPerfilDTO datos) {
		Usuario usuario = usuarioRepo.findByCorreo(correo)
				.orElseThrow(() -> new AuthException("El usuario ya no existe.", 401, "USUARIO_NO_ENCONTRADO"));

		String nombreNuevo = datos.getNombre().trim();
		String nombreAnterior = usuario.getNombre();
		if (!nombreNuevo.equals(nombreAnterior)) {
			usuario.setNombre(nombreNuevo);
			usuarioRepo.save(usuario);
			auditoria.registrar("PERFIL_ACTUALIZADO", correo, AuditoriaService.EXITO,
					"Cambió su nombre de \"" + nombreAnterior + "\" a \"" + nombreNuevo + "\"");
		}
		return aDTO(usuario);
	}

	// El front avisa cuando alguien cierra sesion (el token sigue valiendo hasta que vence)
	public void registrarCierreSesion() {
		auditoria.registrar("CIERRE_SESION", AuditoriaService.EXITO, "Cerró sesión");
	}

	public UsuarioDTO buscarPorCorreo(String correo) {
		Usuario usuario = usuarioRepo.findByCorreo(correo)
				.orElseThrow(() -> new AuthException("El usuario ya no existe.", 401, "USUARIO_NO_ENCONTRADO"));
		return aDTO(usuario);
	}

	private void enviarVerificacion(Usuario usuario) {
		try {
			String token = tokenServ.crear(usuario, TokenCorreo.VERIFICACION, MINUTOS_VERIFICACION);
			correoServ.enviarVerificacion(usuario, token);
		} catch (NegocioException ex) {
			auditoria.registrar("CORREO_NO_ENVIADO", usuario.getCorreo(), AuditoriaService.FALLO,
					"Correo de confirmación: " + ex.getMessage());
			throw ex;
		}
	}

	private String limpiarCorreo(String correo) {
		return correo.trim().toLowerCase();
	}

	private UsuarioDTO aDTO(Usuario usuario) {
		return new UsuarioDTO(usuario.getNombre(), usuario.getCorreo(), usuario.getProveedor(), usuario.getRol());
	}

	private AuthRespuestaDTO respuesta(Usuario usuario) {
		return new AuthRespuestaDTO(jwtService.generar(usuario), aDTO(usuario));
	}

}
