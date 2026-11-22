package co.edu.unbosque.airmap.dto;

import java.util.List;

/**
 * Respuesta completa de la API de Gemini.
 *
 * @author Juan
 * @version 1.0
 */
public class GeminiRespuestaDTO {

	private List<GeminiCandidatoDTO> candidates;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public GeminiRespuestaDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param candidates respuestas que generó Gemini
	 */
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
