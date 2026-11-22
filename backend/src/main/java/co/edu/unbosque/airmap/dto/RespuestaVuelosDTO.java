package co.edu.unbosque.airmap.dto;

import java.util.List;

/**
 * Vuelos que se dibujan en el mapa y cuándo se actualizaron.
 *
 * @author Juan
 * @version 1.0
 */
public class RespuestaVuelosDTO {

	private String actualizadoEn;
	private int total;
	private List<VueloResumenDTO> vuelos;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public RespuestaVuelosDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param actualizadoEn fecha y hora en que se consultaron los vuelos
	 * @param total cantidad de vuelos
	 * @param vuelos lista de vuelos
	 */
	public RespuestaVuelosDTO(String actualizadoEn, int total, List<VueloResumenDTO> vuelos) {
		super();
		this.actualizadoEn = actualizadoEn;
		this.total = total;
		this.vuelos = vuelos;
	}

	public String getActualizadoEn() {
		return actualizadoEn;
	}

	public void setActualizadoEn(String actualizadoEn) {
		this.actualizadoEn = actualizadoEn;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<VueloResumenDTO> getVuelos() {
		return vuelos;
	}

	public void setVuelos(List<VueloResumenDTO> vuelos) {
		this.vuelos = vuelos;
	}

	@Override
	public String toString() {
		return "RespuestaVuelosDTO [" + "actualizadoEn=" + actualizadoEn + ", total=" + total + ", vuelos=" + vuelos + "]";
	}

}
