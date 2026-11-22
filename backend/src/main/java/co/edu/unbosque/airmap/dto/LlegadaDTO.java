package co.edu.unbosque.airmap.dto;

public class LlegadaDTO {

	private String ruta;
	private int minutos;
	private boolean aproximado;

	public LlegadaDTO() {
	}

	public LlegadaDTO(String ruta, int minutos, boolean aproximado) {
		super();
		this.ruta = ruta;
		this.minutos = minutos;
		this.aproximado = aproximado;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public int getMinutos() {
		return minutos;
	}

	public void setMinutos(int minutos) {
		this.minutos = minutos;
	}

	public boolean isAproximado() {
		return aproximado;
	}

	public void setAproximado(boolean aproximado) {
		this.aproximado = aproximado;
	}

	@Override
	public String toString() {
		return "LlegadaDTO [" + "ruta=" + ruta + ", minutos=" + minutos + ", aproximado=" + aproximado + "]";
	}

}
