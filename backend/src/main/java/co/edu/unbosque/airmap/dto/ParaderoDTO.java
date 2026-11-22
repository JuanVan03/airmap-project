package co.edu.unbosque.airmap.dto;

// Paradero o estacion de TransMilenio. distanciaM: metros desde el usuario
public class ParaderoDTO {

	private String id;
	private String nombre;
	private double latitud;
	private double longitud;
	private int distanciaM;

	public ParaderoDTO() {
	}

	public ParaderoDTO(String id, String nombre, double latitud, double longitud, int distanciaM) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.latitud = latitud;
		this.longitud = longitud;
		this.distanciaM = distanciaM;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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
		return "ParaderoDTO [" + "id=" + id + ", nombre=" + nombre + ", latitud=" + latitud + ", longitud=" + longitud + ", distanciaM=" + distanciaM + "]";
	}

}
