package co.edu.unbosque.airmap.dto;

import java.util.List;

public class TransporteDTO {

	private ParaderoDTO paradero;
	private RutaDTO caminata;
	private List<RutaBusDTO> rutas;
	private List<LlegadaDTO> llegadas;
	private List<BusDTO> buses;

	public TransporteDTO() {
	}

	public TransporteDTO(ParaderoDTO paradero, RutaDTO caminata, List<RutaBusDTO> rutas, List<LlegadaDTO> llegadas, List<BusDTO> buses) {
		super();
		this.paradero = paradero;
		this.caminata = caminata;
		this.rutas = rutas;
		this.llegadas = llegadas;
		this.buses = buses;
	}

	public ParaderoDTO getParadero() {
		return paradero;
	}

	public void setParadero(ParaderoDTO paradero) {
		this.paradero = paradero;
	}

	public RutaDTO getCaminata() {
		return caminata;
	}

	public void setCaminata(RutaDTO caminata) {
		this.caminata = caminata;
	}

	public List<RutaBusDTO> getRutas() {
		return rutas;
	}

	public void setRutas(List<RutaBusDTO> rutas) {
		this.rutas = rutas;
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
		return "TransporteDTO [" + "paradero=" + paradero + ", caminata=" + caminata + ", rutas=" + rutas + ", llegadas=" + llegadas + ", buses=" + buses + "]";
	}

}
