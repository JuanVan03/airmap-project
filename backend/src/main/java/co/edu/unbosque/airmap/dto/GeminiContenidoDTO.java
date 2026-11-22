package co.edu.unbosque.airmap.dto;

import java.util.List;

/**
 * Un mensaje de la conversación en el formato de Gemini (rol y partes de texto).
 *
 * @author Juan
 * @version 1.0
 */
public class GeminiContenidoDTO {

	private String role;
	private List<GeminiParteDTO> parts;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public GeminiContenidoDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param role rol del mensaje (user o model)
	 * @param parts partes de texto del mensaje
	 */
	public GeminiContenidoDTO(String role, List<GeminiParteDTO> parts) {
		super();
		this.role = role;
		this.parts = parts;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<GeminiParteDTO> getParts() {
		return parts;
	}

	public void setParts(List<GeminiParteDTO> parts) {
		this.parts = parts;
	}

	@Override
	public String toString() {
		return "GeminiContenidoDTO [" + "role=" + role + ", parts=" + parts + "]";
	}

}
