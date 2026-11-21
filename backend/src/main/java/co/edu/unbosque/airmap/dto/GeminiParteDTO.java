package co.edu.unbosque.airmap.dto;

// Un pedazo de texto dentro de un mensaje de Gemini
public class GeminiParteDTO {

	private String text;

	public GeminiParteDTO() {
	}

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
