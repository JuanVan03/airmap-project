package co.edu.unbosque.airmap.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Correo para pedir el enlace de restablecer la contraseña.
 *
 * @author SakiYoshida
 * @version 1.0
 */
public class RecuperarDTO {

	@NotBlank(message = "Ingresa tu correo.")
	private String correo;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public RecuperarDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param correo correo electrónico
	 */
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
