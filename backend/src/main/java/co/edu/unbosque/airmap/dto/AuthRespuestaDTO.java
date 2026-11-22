package co.edu.unbosque.airmap.dto;

/**
 * Respuesta al ingresar: el token JWT y los datos del usuario.
 *
 * @author SakiYoshida
 * @version 1.0
 */
public class AuthRespuestaDTO {

	private String token;
	private UsuarioDTO usuario;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public AuthRespuestaDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param token token JWT de la sesión
	 * @param usuario datos del usuario
	 */
	public AuthRespuestaDTO(String token, UsuarioDTO usuario) {
		super();
		this.token = token;
		this.usuario = usuario;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UsuarioDTO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}

}
