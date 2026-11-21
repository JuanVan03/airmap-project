package co.edu.unbosque.airmap.dto;

public class GeminiCandidatoDTO {

	private GeminiContenidoDTO content;
	private String finishReason;

	public GeminiCandidatoDTO() {
	}

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
