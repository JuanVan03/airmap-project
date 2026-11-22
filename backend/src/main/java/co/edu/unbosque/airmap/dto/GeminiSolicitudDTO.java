package co.edu.unbosque.airmap.dto;

import java.util.List;

/**
 * Petición que se le manda a Gemini: instrucciones, conversación y configuración.
 *
 * @author SakiYoshida
 * @version 1.0
 */
public class GeminiSolicitudDTO {

	private GeminiContenidoDTO systemInstruction;
	private List<GeminiContenidoDTO> contents;
	private GeminiConfigDTO generationConfig;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public GeminiSolicitudDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param systemInstruction instrucciones fijas del asistente
	 * @param contents mensajes de la conversación
	 * @param generationConfig configuración de la respuesta
	 */
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
