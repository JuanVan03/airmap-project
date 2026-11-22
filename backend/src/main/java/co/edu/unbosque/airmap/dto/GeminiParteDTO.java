package co.edu.unbosque.airmap.dto;

/**
 * Parte de texto de un mensaje de Gemini.
 *
 * @author SakiYoshida
 * @version 1.0
 */
public class GeminiParteDTO {

	private String text;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public GeminiParteDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param text texto
	 */
	public GeminiParteDTO(String text) {
		super();
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "GeminiParteDTO [" + "text=" + text + "]";
	}

}
