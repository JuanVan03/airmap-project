package co.edu.unbosque.airmap.dto;

/**
 * Ruta de un vuelo según adsbdb: aerolínea, aeropuerto de origen y de destino.
 *
 * @author Juan David Vanegas Peña
 * @version 1.0
 */
public class AdsbdbRutaDTO {

	private AdsbdbAerolineaDTO airline;
	private AeropuertoDTO origin;
	private AeropuertoDTO destination;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public AdsbdbRutaDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param airline aerolínea del vuelo
	 * @param origin aeropuerto de origen
	 * @param destination aeropuerto de destino
	 */
	public AdsbdbRutaDTO(AdsbdbAerolineaDTO airline, AeropuertoDTO origin, AeropuertoDTO destination) {
		super();
		this.airline = airline;
		this.origin = origin;
		this.destination = destination;
	}

	public AdsbdbAerolineaDTO getAirline() {
		return airline;
	}

	public void setAirline(AdsbdbAerolineaDTO airline) {
		this.airline = airline;
	}

	public AeropuertoDTO getOrigin() {
		return origin;
	}

	public void setOrigin(AeropuertoDTO origin) {
		this.origin = origin;
	}

	public AeropuertoDTO getDestination() {
		return destination;
	}

	public void setDestination(AeropuertoDTO destination) {
		this.destination = destination;
	}

	@Override
	public String toString() {
		return "AdsbdbRutaDTO [" + "airline=" + airline + ", origin=" + origin + ", destination=" + destination + "]";
	}

}
