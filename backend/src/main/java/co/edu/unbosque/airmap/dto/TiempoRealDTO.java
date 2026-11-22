package co.edu.unbosque.airmap.dto;

import java.util.List;

public class TiempoRealDTO {

	private List<LlegadaDTO> llegadas;
	private List<BusDTO> buses;

	public TiempoRealDTO() {
	}

	public TiempoRealDTO(List<LlegadaDTO> llegadas, List<BusDTO> buses) {
		super();
		this.llegadas = llegadas;
		this.buses = buses;
	}

	public List<LlegadaDTO> getLlegadas() {
		return llegadas;
	}

	public void setLlegadas(List<LlegadaDTO> llegadas) {
		this.llegadas = llegadas;
	}

	public List<BusDTO> getBuses() {
		return buses;
	}

	public void setBuses(List<BusDTO> buses) {
		this.buses = buses;
	}

	@Override
	public String toString() {
		return "TiempoRealDTO [" + "llegadas=" + llegadas + ", buses=" + buses + "]";
	}

}
