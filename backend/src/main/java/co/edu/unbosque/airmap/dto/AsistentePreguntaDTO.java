package co.edu.unbosque.airmap.dto;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Pregunta que el usuario le hace al asistente, con el historial de la conversación.
 *
 * @author Juan
 * @version 1.0
 */
public class AsistentePreguntaDTO {

	@NotBlank(message = "Escribe tu pregunta.")
	@Size(max = 500, message = "La pregunta es muy larga (maximo 500 caracteres).")
	private String mensaje;
	@Size(max = 20, message = "El id del vuelo es muy largo.")
	private String vueloId;
	@Size(max = 30, message = "El historial es muy largo.")
	@Valid
	private List<MensajeChatDTO> historial;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public AsistentePreguntaDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param mensaje pregunta del usuario
	 * @param vueloId id del vuelo de la página desde donde pregunta (puede ser null)
	 * @param historial mensajes anteriores de la conversación
	 */
	public AsistentePreguntaDTO(String mensaje, String vueloId, List<MensajeChatDTO> historial) {
		super();
		this.mensaje = mensaje;
		this.vueloId = vueloId;
		this.historial = historial;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getVueloId() {
		return vueloId;
	}

	public void setVueloId(String vueloId) {
		this.vueloId = vueloId;
	}

	public List<MensajeChatDTO> getHistorial() {
		return historial;
	}

	public void setHistorial(List<MensajeChatDTO> historial) {
		this.historial = historial;
	}

	@Override
	public String toString() {
		return "AsistentePreguntaDTO [" + "mensaje=" + mensaje + ", vueloId=" + vueloId + ", historial=" + historial + "]";
	}

}
