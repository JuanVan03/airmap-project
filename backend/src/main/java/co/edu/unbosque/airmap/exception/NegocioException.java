package co.edu.unbosque.airmap.exception;

// Errores de reglas de la app: algo no existe, se paso un limite, etc.
public class NegocioException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final int statusCode;
	private final String codigo;

	public NegocioException(String message, int statusCode, String codigo) {
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
