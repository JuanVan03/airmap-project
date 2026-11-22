package co.edu.unbosque.airmap.dto;

/**
 * Bus de TransMilenio en vivo cerca del paradero.
 *
 * @author Juan
 * @version 1.0
 */
public class BusDTO {

	private String ruta;
	private double latitud;
	private double longitud;
	private int distanciaM;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public BusDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param ruta código de la ruta del bus
	 * @param latitud latitud
	 * @param longitud longitud
	 * @param distanciaM metros hasta el paradero
	 */
	public BusDTO(String ruta, double latitud, double longitud, int distanciaM) {
		super();
		this.ruta = ruta;
		this.latitud = latitud;
		this.longitud = longitud;
		this.distanciaM = distanciaM;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public int getDistanciaM() {
		return distanciaM;
	}

	public void setDistanciaM(int distanciaM) {
		this.distanciaM = distanciaM;
	}

	@Override
	public String toString() {
		return "BusDTO [" + "ruta=" + ruta + ", latitud=" + latitud + ", longitud=" + longitud + ", distanciaM=" + distanciaM + "]";
	}

}
