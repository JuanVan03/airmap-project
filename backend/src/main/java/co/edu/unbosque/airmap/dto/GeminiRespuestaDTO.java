package co.edu.unbosque.airmap.dto;

import java.util.List;

// Respuesta de Gemini: el texto esta en candidates[0].content.parts[].text
public class GeminiRespuestaDTO {

	private List<GeminiCandidatoDTO> candidates;

	public GeminiRespuestaDTO() {
	}

	public GeminiRespuestaDTO(List<GeminiCandidatoDTO> candidates) {
		super();
		this.candidates = candidates;
	}

	public List<GeminiCandidatoDTO> getCandidates() {
		return candidates;
	}

	public void setCandidates(List<GeminiCandidatoDTO> candidates) {
		this.candidates = candidates;
	}

	@Override
	public String toString() {
		return "GeminiRespuestaDTO [" + "candidates=" + candidates + "]";
	}

}
