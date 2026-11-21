package co.edu.unbosque.airmap.dto;

import java.util.List;

// Cuerpo de POST /models/{modelo}:generateContent
public class GeminiSolicitudDTO {

	private GeminiContenidoDTO systemInstruction;
	private List<GeminiContenidoDTO> contents;
	private GeminiConfigDTO generationConfig;

	public GeminiSolicitudDTO() {
	}

	public GeminiSolicitudDTO(GeminiContenidoDTO systemInstruction, List<GeminiContenidoDTO> contents, GeminiConfigDTO generationConfig) {
		super();
		this.systemInstruction = systemInstruction;
		this.contents = contents;
		this.generationConfig = generationConfig;
	}

	public GeminiContenidoDTO getSystemInstruction() {
		return systemInstruction;
	}

	public void setSystemInstruction(GeminiContenidoDTO systemInstruction) {
		this.systemInstruction = systemInstruction;
	}

	public List<GeminiContenidoDTO> getContents() {
		return contents;
	}

	public void setContents(List<GeminiContenidoDTO> contents) {
		this.contents = contents;
	}

	public GeminiConfigDTO getGenerationConfig() {
		return generationConfig;
	}

	public void setGenerationConfig(GeminiConfigDTO generationConfig) {
		this.generationConfig = generationConfig;
	}

	@Override
	public String toString() {
		return "GeminiSolicitudDTO [" + "systemInstruction=" + systemInstruction + ", contents=" + contents + ", generationConfig=" + generationConfig + "]";
	}

}
