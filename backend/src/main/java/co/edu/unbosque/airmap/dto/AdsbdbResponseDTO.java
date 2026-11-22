package co.edu.unbosque.airmap.dto;

/**
 * Respuesta completa de la API de adsbdb al buscar un vuelo por indicativo.
 *
 * @author SakiYoshida
 * @version 1.0
 */
public class AdsbdbResponseDTO {

	private AdsbdbDatosDTO response;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public AdsbdbResponseDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param response datos de la respuesta
	 */
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
