package co.edu.unbosque.airmap.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// Un mensaje de la conversacion: rol es "usuario" o "asistente"
public class MensajeChatDTO {

	@Pattern(regexp = "^(usuario|asistente)$", message = "El rol debe ser usuario o asistente.")
	private String rol;
	@Size(max = 2000, message = "Un mensaje del historial es muy largo.")
	private String texto;

	public MensajeChatDTO() {
	}

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
