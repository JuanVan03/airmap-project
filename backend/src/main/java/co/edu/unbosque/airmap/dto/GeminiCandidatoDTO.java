package co.edu.unbosque.airmap.dto;

/**
 * Una de las respuestas posibles que devuelve Gemini.
 *
 * @author Juan
 * @version 1.0
 */
public class GeminiCandidatoDTO {

	private GeminiContenidoDTO content;
	private String finishReason;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public GeminiCandidatoDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param content mensaje que generó Gemini
	 * @param finishReason motivo por el que Gemini terminó la respuesta
	 */
	public GeminiCandidatoDTO(GeminiContenidoDTO content, String finishReason) {
		super();
		this.content = content;
		this.finishReason = finishReason;
	}

	public GeminiContenidoDTO getContent() {
		return content;
	}

	public void setContent(GeminiContenidoDTO content) {
		this.content = content;
	}

	public String getFinishReason() {
		return finishReason;
	}

	public void setFinishReason(String finishReason) {
		this.finishReason = finishReason;
	}

	@Override
	public String toString() {
		return "GeminiCandidatoDTO [" + "content=" + content + ", finishReason=" + finishReason + "]";
	}

}
