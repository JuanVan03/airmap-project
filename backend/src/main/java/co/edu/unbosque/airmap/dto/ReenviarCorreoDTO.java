package co.edu.unbosque.airmap.dto;

import jakarta.validation.constraints.NotBlank;

public class ReenviarCorreoDTO {

	@NotBlank(message = "Ingresa tu correo.")
	private String correo;

	public ReenviarCorreoDTO() {
	}

	public ReenviarCorreoDTO(String correo) {
		super();
		this.correo = correo;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

}
