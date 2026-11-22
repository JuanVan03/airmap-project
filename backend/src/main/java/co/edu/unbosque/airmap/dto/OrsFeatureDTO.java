package co.edu.unbosque.airmap.dto;

/**
 * Ruta que devuelve openrouteservice (formato GeoJSON).
 *
 * @author SakiYoshida
 * @version 1.0
 */
public class OrsFeatureDTO {

	private OrsPropiedadesDTO properties;
	private OrsGeometriaDTO geometry;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public OrsFeatureDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param properties resumen de la ruta
	 * @param geometry puntos de la ruta
	 */
	public OrsFeatureDTO(OrsPropiedadesDTO properties, OrsGeometriaDTO geometry) {
		super();
		this.properties = properties;
		this.geometry = geometry;
	}

	public OrsPropiedadesDTO getProperties() {
		return properties;
	}

	public void setProperties(OrsPropiedadesDTO properties) {
		this.properties = properties;
	}

	public OrsGeometriaDTO getGeometry() {
		return geometry;
	}

	public void setGeometry(OrsGeometriaDTO geometry) {
		this.geometry = geometry;
	}

	@Override
	public String toString() {
		return "OrsFeatureDTO [" + "properties=" + properties + ", geometry=" + geometry + "]";
	}

}
