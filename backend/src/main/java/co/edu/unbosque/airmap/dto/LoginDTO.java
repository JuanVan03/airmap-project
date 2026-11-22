package co.edu.unbosque.airmap.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Datos para ingresar con correo y contraseña.
 *
 * @author SakiYoshida
 * @version 1.0
 */
public class LoginDTO {

	@NotBlank(message = "Ingresa tu correo.")
	private String correo;

	@NotBlank(message = "La contraseña es obligatoria.")
	private String contrasena;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public LoginDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param correo correo electrónico
	 * @param contrasena contraseña
	 */
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
