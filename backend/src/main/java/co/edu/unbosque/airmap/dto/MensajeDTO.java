package co.edu.unbosque.airmap.dto;

/**
 * Respuesta sencilla con un mensaje para mostrarle al usuario.
 *
 * @author SakiYoshida
 * @version 1.0
 */
public class MensajeDTO {

	private String mensaje;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public MensajeDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param mensaje texto del mensaje
	 */
	public MensajeDTO(String mensaje) {
		super();
		this.mensaje = mensaje;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

}
