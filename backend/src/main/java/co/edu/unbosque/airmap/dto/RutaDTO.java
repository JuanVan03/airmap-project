package co.edu.unbosque.airmap.dto;

import java.util.List;

public class RutaDTO {

	private double distanciaKm;
	private int duracionMin;
	private List<List<Double>> coordenadas;

	public RutaDTO() {
	}

	public RutaDTO(double distanciaKm, int duracionMin, List<List<Double>> coordenadas) {
		super();
		this.distanciaKm = distanciaKm;
		this.duracionMin = duracionMin;
		this.coordenadas = coordenadas;
	}

	public double getDistanciaKm() {
		return distanciaKm;
	}

	public void setDistanciaKm(double distanciaKm) {
		this.distanciaKm = distanciaKm;
	}

	public int getDuracionMin() {
		return duracionMin;
	}

	public void setDuracionMin(int duracionMin) {
		this.duracionMin = duracionMin;
	}

	public List<List<Double>> getCoordenadas() {
		return coordenadas;
	}

	public void setCoordenadas(List<List<Double>> coordenadas) {
		this.coordenadas = coordenadas;
	}

	@Override
	public String toString() {
		return "RutaDTO [" + "distanciaKm=" + distanciaKm + ", duracionMin=" + duracionMin + ", coordenadas=" + coordenadas + "]";
	}

}
