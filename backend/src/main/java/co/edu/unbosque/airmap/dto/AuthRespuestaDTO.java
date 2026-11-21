package co.edu.unbosque.airmap.dto;

public class AuthRespuestaDTO {

	private String token;
	private UsuarioDTO usuario;

	public AuthRespuestaDTO() {
	}

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
