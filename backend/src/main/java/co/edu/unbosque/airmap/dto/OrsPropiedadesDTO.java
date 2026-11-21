package co.edu.unbosque.airmap.dto;

public class OrsPropiedadesDTO {

	private OrsResumenDTO summary;

	public OrsPropiedadesDTO() {
	}

	public OrsPropiedadesDTO(OrsResumenDTO summary) {
		super();
		this.summary = summary;
	}

	public OrsResumenDTO getSummary() {
		return summary;
	}

	public void setSummary(OrsResumenDTO summary) {
		this.summary = summary;
	}

	@Override
	public String toString() {
		return "OrsPropiedadesDTO [" + "summary=" + summary + "]";
	}

}
