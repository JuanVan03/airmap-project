package co.edu.unbosque.airmap.dto;

/**
 * Propiedades de la ruta de openrouteservice.
 *
 * @author SakiYoshida
 * @version 1.0
 */
public class OrsPropiedadesDTO {

	private OrsResumenDTO summary;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public OrsPropiedadesDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param summary distancia y duración
	 */
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
