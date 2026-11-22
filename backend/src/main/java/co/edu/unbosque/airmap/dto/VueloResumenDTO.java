package co.edu.unbosque.airmap.dto;

/**
 * Datos mínimos de un vuelo para dibujarlo en el mapa.
 *
 * @author Juan
 * @version 1.0
 */
public class VueloResumenDTO {

	private String id;
	private String numeroVuelo;
	private double latitud;
	private double longitud;
	private double rumbo;
	private boolean enTierra;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public VueloResumenDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param id id del vuelo (código icao24 del avión)
	 * @param numeroVuelo número o indicativo del vuelo (por ejemplo AVA010)
	 * @param latitud latitud
	 * @param longitud longitud
	 * @param rumbo rumbo del avión en grados
	 * @param enTierra true si el avión está en tierra
	 */
	public VueloResumenDTO(String id, String numeroVuelo, double latitud, double longitud, double rumbo, boolean enTierra) {
		super();
		this.id = id;
		this.numeroVuelo = numeroVuelo;
		this.latitud = latitud;
		this.longitud = longitud;
		this.rumbo = rumbo;
		this.enTierra = enTierra;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNumeroVuelo() {
		return numeroVuelo;
	}

	public void setNumeroVuelo(String numeroVuelo) {
		this.numeroVuelo = numeroVuelo;
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

	public double getRumbo() {
		return rumbo;
	}

	public void setRumbo(double rumbo) {
		this.rumbo = rumbo;
	}

	public boolean isEnTierra() {
		return enTierra;
	}

	public void setEnTierra(boolean enTierra) {
		this.enTierra = enTierra;
	}

	@Override
	public String toString() {
		return "VueloResumenDTO [" + "id=" + id + ", numeroVuelo=" + numeroVuelo + ", latitud=" + latitud + ", longitud=" + longitud + ", rumbo=" + rumbo + ", enTierra=" + enTierra + "]";
	}

}
