package co.edu.unbosque.airmap.dto;

import java.util.List;

/**
 * Respuesta del asistente y los vuelos que se muestran como tarjetas.
 *
 * @author SakiYoshida
 * @version 1.0
 */
public class AsistenteRespuestaDTO {

	private String respuesta;
	private List<VueloDTO> vuelos;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public AsistenteRespuestaDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param respuesta texto de la respuesta del asistente
	 * @param vuelos lista de vuelos
	 */
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
