package co.edu.unbosque.airmap.dto;

/**
 * Resumen de la ruta de openrouteservice: distancia y duración.
 *
 * @author SakiYoshida
 * @version 1.0
 */
public class OrsResumenDTO {

	private double distance;
	private double duration;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public OrsResumenDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param distance distancia en metros
	 * @param duration duración en segundos
	 */
	public OrsResumenDTO(double distance, double duration) {
		super();
		this.distance = distance;
		this.duration = duration;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		return "OrsResumenDTO [" + "distance=" + distance + ", duration=" + duration + "]";
	}

}
