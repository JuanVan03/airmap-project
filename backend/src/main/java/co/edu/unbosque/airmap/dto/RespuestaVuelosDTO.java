package co.edu.unbosque.airmap.dto;

import java.util.List;

public class RespuestaVuelosDTO {

	private String actualizadoEn;
	private int total;
	private List<VueloResumenDTO> vuelos;

	public RespuestaVuelosDTO() {
	}

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
