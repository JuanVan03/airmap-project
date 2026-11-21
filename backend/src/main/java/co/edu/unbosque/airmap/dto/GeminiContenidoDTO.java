package co.edu.unbosque.airmap.dto;

import java.util.List;

// Un mensaje de Gemini: role es "user" o "model" (queda null en las instrucciones del sistema)
public class GeminiContenidoDTO {

	private String role;
	private List<GeminiParteDTO> parts;

	public GeminiContenidoDTO() {
	}

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
