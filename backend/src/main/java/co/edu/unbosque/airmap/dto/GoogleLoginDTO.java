package co.edu.unbosque.airmap.dto;

import jakarta.validation.constraints.NotBlank;

public class GoogleLoginDTO {

	// Token (ID token) que entrega el boton "Continuar con Google" del front
	@NotBlank(message = "Falta la credencial de Google.")
	private String credencial;

	public GoogleLoginDTO() {
	}

	public GoogleLoginDTO(String credencial) {
		super();
		this.credencial = credencial;
	}

	public String getCredencial() {
		return credencial;
	}

	public void setCredencial(String credencial) {
		this.credencial = credencial;
	}

}
