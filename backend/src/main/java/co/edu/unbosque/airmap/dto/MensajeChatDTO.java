package co.edu.unbosque.airmap.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Mensaje anterior de la conversación con el asistente.
 *
 * @author Juan
 * @version 1.0
 */
public class MensajeChatDTO {

	@Pattern(regexp = "^(usuario|asistente)$", message = "El rol debe ser usuario o asistente.")
	private String rol;
	@Size(max = 2000, message = "Un mensaje del historial es muy largo.")
	private String texto;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public MensajeChatDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param rol quién escribió el mensaje (usuario o asistente)
	 * @param texto texto del mensaje
	 */
	public MensajeChatDTO(String rol, String texto) {
		super();
		this.rol = rol;
		this.texto = texto;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	@Override
	public String toString() {
		return "MensajeChatDTO [" + "rol=" + rol + ", texto=" + texto + "]";
	}

}
