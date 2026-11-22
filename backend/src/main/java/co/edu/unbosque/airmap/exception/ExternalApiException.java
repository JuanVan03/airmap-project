package co.edu.unbosque.airmap.exception;

/**
 * Excepción que se lanza cuando falla una API externa (OpenSky, adsbdb, openrouteservice,
 * Gemini o TransMilenio).
 *
 * @author SakiYoshida
 * @version 1.0
 */
public class ExternalApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final int statusCode;

	/**
	 * Crea la excepción con el mensaje y el código HTTP.
	 *
	 * @param message mensaje del error
	 * @param statusCode código HTTP de la respuesta
	 */
	public ExternalApiException(String message, int statusCode) {
		super(message);
		this.statusCode = statusCode;
	}

	/**
	 * Crea la excepción guardando también la excepción que la causó.
	 *
	 * @param message mensaje del error
	 * @param statusCode código HTTP de la respuesta
	 * @param cause excepción original
	 */
	public ExternalApiException(String message, int statusCode, Throwable cause) {
		super(message, cause);
		this.statusCode = statusCode;
	}

	/**
	 * Devuelve el código HTTP que respondió la API externa (o 503 si no hubo conexión).
	 *
	 * @return código HTTP
	 */
	public int getStatusCode() {
		return statusCode;
	}

}
