package co.edu.unbosque.airmap.dto;

public class AdsbdbRutaDTO {

	private AdsbdbAerolineaDTO airline;
	private AeropuertoDTO origin;
	private AeropuertoDTO destination;

	public AdsbdbRutaDTO() {
	}

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
