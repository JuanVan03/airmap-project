package co.edu.unbosque.airmap.dto;

import jakarta.validation.constraints.NotBlank;

public class RecuperarDTO {

	@NotBlank(message = "Ingresa tu correo.")
	private String correo;

	public RecuperarDTO() {
	}

	public RecuperarDTO(String correo) {
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
