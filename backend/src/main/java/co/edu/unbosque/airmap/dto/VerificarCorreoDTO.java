package co.edu.unbosque.airmap.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Token del enlace para confirmar el correo.
 *
 * @author Juan
 * @version 1.0
 */
public class VerificarCorreoDTO {

	@NotBlank(message = "Falta el enlace de confirmacion.")
	private String token;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public VerificarCorreoDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param token token que llegó en el enlace del correo
	 */
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
