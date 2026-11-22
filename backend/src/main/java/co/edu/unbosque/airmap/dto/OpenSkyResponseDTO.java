package co.edu.unbosque.airmap.dto;

import java.util.List;

/**
 * Respuesta de la API de OpenSky con el estado de todos los vuelos.
 *
 * @author Juan
 * @version 1.0
 */
public class OpenSkyResponseDTO {

	private long time;
	private List<List<Object>> states;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public OpenSkyResponseDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param time momento de la consulta (segundos desde 1970)
	 * @param states lista con el estado de cada avión
	 */
	public OpenSkyResponseDTO(long time, List<List<Object>> states) {
		super();
		this.time = time;
		this.states = states;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public List<List<Object>> getStates() {
		return states;
	}

	public void setStates(List<List<Object>> states) {
		this.states = states;
	}

	@Override
	public String toString() {
		return "OpenSkyResponseDTO [" + "time=" + time + ", states=" + states + "]";
	}

}
