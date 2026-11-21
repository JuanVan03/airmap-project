package co.edu.unbosque.airmap.dto;

// Ajustes de la respuesta de Gemini
public class GeminiConfigDTO {

	private double temperature;
	private int maxOutputTokens;

	public GeminiConfigDTO() {
	}

	public GeminiConfigDTO(double temperature, int maxOutputTokens) {
		super();
		this.temperature = temperature;
		this.maxOutputTokens = maxOutputTokens;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public int getMaxOutputTokens() {
		return maxOutputTokens;
	}

	public void setMaxOutputTokens(int maxOutputTokens) {
		this.maxOutputTokens = maxOutputTokens;
	}

	@Override
	public String toString() {
		return "GeminiConfigDTO [" + "temperature=" + temperature + ", maxOutputTokens=" + maxOutputTokens + "]";
	}

}
