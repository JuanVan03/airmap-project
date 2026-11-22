package co.edu.unbosque.airmap.dto;

import java.util.List;

/**
 * Puntos de la ruta de openrouteservice, como [longitud, latitud].
 *
 * @author Juan
 * @version 1.0
 */
public class OrsGeometriaDTO {

	private List<List<Double>> coordinates;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public OrsGeometriaDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param coordinates puntos de la ruta como [longitud, latitud]
	 */
	public OrsGeometriaDTO(List<List<Double>> coordinates) {
		super();
		this.coordinates = coordinates;
	}

	public List<List<Double>> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<List<Double>> coordinates) {
		this.coordinates = coordinates;
	}

	@Override
	public String toString() {
		return "OrsGeometriaDTO [" + "coordinates=" + coordinates + "]";
	}

}
