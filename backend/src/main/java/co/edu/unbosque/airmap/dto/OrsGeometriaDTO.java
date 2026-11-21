package co.edu.unbosque.airmap.dto;

import java.util.List;

public class OrsGeometriaDTO {

	private List<List<Double>> coordinates;

	public OrsGeometriaDTO() {
	}

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
