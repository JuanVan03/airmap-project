package co.edu.unbosque.airmap.exception;

public class AuthException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final int statusCode;
	private final String codigo;

	public AuthException(String message, int statusCode, String codigo) {
		super(message);
		this.statusCode = statusCode;
		this.codigo = codigo;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getCodigo() {
		return codigo;
	}

}
