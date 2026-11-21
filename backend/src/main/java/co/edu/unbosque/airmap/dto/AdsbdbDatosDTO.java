package co.edu.unbosque.airmap.dto;

public class AdsbdbDatosDTO {

	private AdsbdbRutaDTO flightroute;

	public AdsbdbDatosDTO() {
	}

	public AdsbdbDatosDTO(AdsbdbRutaDTO flightroute) {
		super();
		this.flightroute = flightroute;
	}

	public AdsbdbRutaDTO getFlightroute() {
		return flightroute;
	}

	public void setFlightroute(AdsbdbRutaDTO flightroute) {
		this.flightroute = flightroute;
	}

	@Override
	public String toString() {
		return "AdsbdbDatosDTO [" + "flightroute=" + flightroute + "]";
	}

}
