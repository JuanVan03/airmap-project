package co.edu.unbosque.airmap.dto;

import java.util.List;

/**
 * Datos de TransMilenio que cambian cada 30 segundos: próximos buses y dónde van.
 *
 * @author SakiYoshida
 * @version 1.0
 */
public class TiempoRealDTO {

	private List<LlegadaDTO> llegadas;
	private List<BusDTO> buses;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public TiempoRealDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param llegadas próximos buses que llegan al paradero
	 * @param buses buses en vivo cerca del paradero
	 */
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
