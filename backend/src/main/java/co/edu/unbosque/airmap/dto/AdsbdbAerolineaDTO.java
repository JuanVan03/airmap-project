package co.edu.unbosque.airmap.dto;

public class AdsbdbAerolineaDTO {

	private String name;

	public AdsbdbAerolineaDTO() {
	}

	public AdsbdbAerolineaDTO(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "AdsbdbAerolineaDTO [" + "name=" + name + "]";
	}

}
