package co.edu.unbosque.airmap.dto;

public class OrsResumenDTO {

	private double distance;
	private double duration;

	public OrsResumenDTO() {
	}

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
