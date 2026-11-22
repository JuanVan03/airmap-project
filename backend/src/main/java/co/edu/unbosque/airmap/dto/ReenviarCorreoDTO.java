package co.edu.unbosque.airmap.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Correo para volver a enviar el enlace de confirmación de la cuenta.
 *
 * @author Juan
 * @version 1.0
 */
public class ReenviarCorreoDTO {

	@NotBlank(message = "Ingresa tu correo.")
	private String correo;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public ReenviarCorreoDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param correo correo electrónico
	 */
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
