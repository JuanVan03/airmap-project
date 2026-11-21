package co.edu.unbosque.airmap.dto;

// Respuesta sencilla con un texto para mostrarle al usuario
public class MensajeDTO {

	private String mensaje;

	public MensajeDTO() {
	}

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
