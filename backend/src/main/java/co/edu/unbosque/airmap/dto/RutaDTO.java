package co.edu.unbosque.airmap.dto;

import java.util.List;

/**
 * Ruta terrestre calculada: distancia, duración y puntos para dibujarla en el mapa.
 *
 * @author SakiYoshida
 * @version 1.0
 */
public class RutaDTO {

	private double distanciaKm;
	private int duracionMin;
	private List<List<Double>> coordenadas;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public RutaDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param distanciaKm distancia en kilómetros
	 * @param duracionMin duración en minutos
	 * @param coordenadas puntos de la ruta como [latitud, longitud]
	 */
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
