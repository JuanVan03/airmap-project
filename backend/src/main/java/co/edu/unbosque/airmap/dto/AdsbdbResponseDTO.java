package co.edu.unbosque.airmap.dto;

public class AdsbdbResponseDTO {

	private AdsbdbDatosDTO response;

	public AdsbdbResponseDTO() {
	}

	public AdsbdbResponseDTO(AdsbdbDatosDTO response) {
		super();
		this.response = response;
	}

	public AdsbdbDatosDTO getResponse() {
		return response;
	}

	public void setResponse(AdsbdbDatosDTO response) {
		this.response = response;
	}

	@Override
	public String toString() {
		return "AdsbdbResponseDTO [" + "response=" + response + "]";
	}

}
