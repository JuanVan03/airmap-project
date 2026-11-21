package co.edu.unbosque.airmap.dto;

import jakarta.validation.constraints.NotBlank;

public class VerificarCorreoDTO {

	@NotBlank(message = "Falta el enlace de confirmacion.")
	private String token;

	public VerificarCorreoDTO() {
	}

	public VerificarCorreoDTO(String token) {
		super();
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
