package co.edu.unbosque.airmap.exception;

public class ExternalApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final int statusCode;

	public ExternalApiException(String message, int statusCode) {
		super(message);
		this.statusCode = statusCode;
	}

	public ExternalApiException(String message, int statusCode, Throwable cause) {
		super(message, cause);
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

}
