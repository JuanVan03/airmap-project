package co.edu.unbosque.airmap.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Credencial que entrega el botón de Google en el front.
 *
 * @author Juan
 * @version 1.0
 */
public class GoogleLoginDTO {

	// Token (ID token) que entrega el boton "Continuar con Google" del front
	@NotBlank(message = "Falta la credencial de Google.")
	private String credencial;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public GoogleLoginDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param credencial token que entrega el botón de Google
	 */
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
