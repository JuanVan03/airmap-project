package co.edu.unbosque.airmap.dto;

/**
 * Configuración de la respuesta de Gemini (temperatura y largo máximo).
 *
 * @author SakiYoshida
 * @version 1.0
 */
public class GeminiConfigDTO {

	private double temperature;
	private int maxOutputTokens;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public GeminiConfigDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param temperature temperatura (qué tan creativa es la respuesta)
	 * @param maxOutputTokens largo máximo de la respuesta
	 */
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
