package co.edu.unbosque.airmap.dto;

/**
 * Parte "response" de la respuesta de adsbdb, donde viene la ruta del vuelo.
 *
 * @author Juan David Vanegas Peña
 * @version 1.0
 */
public class AdsbdbDatosDTO {

	private AdsbdbRutaDTO flightroute;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public AdsbdbDatosDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param flightroute ruta del vuelo
	 */
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
