package co.edu.unbosque.airmap.dto;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Lo que envia el front a POST /api/asistente
public class AsistentePreguntaDTO {

	@NotBlank(message = "Escribe tu pregunta.")
	@Size(max = 500, message = "La pregunta es muy larga (maximo 500 caracteres).")
	private String mensaje;
	@Size(max = 20, message = "El id del vuelo es muy largo.")
	private String vueloId;
	@Size(max = 30, message = "El historial es muy largo.")
	@Valid
	private List<MensajeChatDTO> historial;

	public AsistentePreguntaDTO() {
	}

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
