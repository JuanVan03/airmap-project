package co.edu.unbosque.airmap.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginDTO {

	@NotBlank(message = "Ingresa tu correo.")
	private String correo;

	@NotBlank(message = "La contraseña es obligatoria.")
	private String contrasena;

	public LoginDTO() {
	}

	public LoginDTO(String correo, String contrasena) {
		super();
		this.correo = correo;
		this.contrasena = contrasena;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

}
