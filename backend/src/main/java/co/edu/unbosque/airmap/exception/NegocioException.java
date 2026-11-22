package co.edu.unbosque.airmap.exception;

/**
 * Excepción para las reglas de la aplicación: algo no existe, se pasó un límite, falta una
 * configuración, etc.
 *
 * @author SakiYoshida
 * @version 1.0
 */
public class NegocioException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final int statusCode;
	private final String codigo;

	/**
	 * Crea la excepción con el mensaje, el código HTTP y el código del error.
	 *
	 * @param message mensaje del error
	 * @param statusCode código HTTP de la respuesta
	 * @param codigo código del error que lee el front
	 */
	public NegocioException(String message, int statusCode, String codigo) {
		super(message);
		this.statusCode = statusCode;
		this.codigo = codigo;
	}

	/**
	 * Devuelve el código HTTP con el que se responde.
	 *
	 * @return código HTTP
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * Devuelve el código del error, por ejemplo LIMITE_FAVORITOS.
	 *
	 * @return código del error
	 */
	public String getCodigo() {
		return codigo;
	}

}
