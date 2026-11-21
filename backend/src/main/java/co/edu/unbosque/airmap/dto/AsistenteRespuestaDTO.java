package co.edu.unbosque.airmap.dto;

import java.util.List;

// Lo que responde POST /api/asistente: el texto y los vuelos que se mencionaron
public class AsistenteRespuestaDTO {

	private String respuesta;
	private List<VueloDTO> vuelos;

	public AsistenteRespuestaDTO() {
	}

	public AsistenteRespuestaDTO(String respuesta, List<VueloDTO> vuelos) {
		super();
		this.respuesta = respuesta;
		this.vuelos = vuelos;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public List<VueloDTO> getVuelos() {
		return vuelos;
	}

	public void setVuelos(List<VueloDTO> vuelos) {
		this.vuelos = vuelos;
	}

	@Override
	public String toString() {
		return "AsistenteRespuestaDTO [" + "respuesta=" + respuesta + ", vuelos=" + vuelos + "]";
	}

}
