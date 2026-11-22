package co.edu.unbosque.airmap.dto;

import java.util.List;

/**
 * Cómo llegar al aeropuerto El Dorado en TransMilenio desde la ubicación del usuario.
 *
 * @author Juan
 * @version 1.0
 */
public class TransporteDTO {

	private ParaderoDTO paradero;
	private RutaDTO caminata;
	private List<RutaBusDTO> rutas;
	private List<LlegadaDTO> llegadas;
	private List<BusDTO> buses;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public TransporteDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param paradero paradero más cercano con ruta al aeropuerto
	 * @param caminata ruta caminando hasta el paradero (puede ser null)
	 * @param rutas rutas que llegan al aeropuerto sin transbordo
	 * @param llegadas próximos buses que llegan al paradero
	 * @param buses buses en vivo cerca del paradero
	 */
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
