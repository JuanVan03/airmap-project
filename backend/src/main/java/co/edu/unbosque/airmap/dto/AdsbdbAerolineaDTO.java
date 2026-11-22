package co.edu.unbosque.airmap.dto;

/**
 * Aerolínea que devuelve la API de adsbdb.
 *
 * @author SakiYoshida
 * @version 1.0
 */
public class AdsbdbAerolineaDTO {

	private String name;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public AdsbdbAerolineaDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param name nombre de la aerolínea
	 */
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
