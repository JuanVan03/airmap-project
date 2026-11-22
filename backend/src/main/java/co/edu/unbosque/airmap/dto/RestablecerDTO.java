package co.edu.unbosque.airmap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Token del enlace y contraseña nueva para restablecer la contraseña.
 *
 * @author SakiYoshida
 * @version 1.0
 */
public class RestablecerDTO {

	@NotBlank(message = "Falta el enlace de recuperacion.")
	private String token;

	// Mismas reglas que al registrarse: minimo 8 caracteres, una mayuscula y un numero
	@NotBlank(message = "La contrasena es obligatoria.")
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$", message = "Debe tener mínimo 8 caracteres, una mayuscula y un numero.")
	private String contrasena;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public RestablecerDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param token token que llegó en el enlace del correo
	 * @param contrasena contraseña nueva
	 */
	public RestablecerDTO(String token, String contrasena) {
		super();
		this.token = token;
		this.contrasena = contrasena;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

}
