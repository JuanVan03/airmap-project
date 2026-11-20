package co.edu.unbosque.airmap.dto;

import java.util.List;

public class OpenSkyResponseDTO {

	private long time;
	private List<List<Object>> states;

	public OpenSkyResponseDTO() {
	}

	public OpenSkyResponseDTO(long time, List<List<Object>> states) {
		super();
		this.time = time;
		this.states = states;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public List<List<Object>> getStates() {
		return states;
	}

	public void setStates(List<List<Object>> states) {
		this.states = states;
	}

	@Override
	public String toString() {
		return "OpenSkyResponseDTO [" + "time=" + time + ", states=" + states + "]";
	}

}
