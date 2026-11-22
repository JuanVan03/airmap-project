package co.edu.unbosque.airmap.dto;

public class BusDTO {

	private String ruta;
	private double latitud;
	private double longitud;
	private int distanciaM;

	public BusDTO() {
	}

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
