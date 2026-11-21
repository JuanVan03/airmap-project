package co.edu.unbosque.airmap.dto;

public class OrsFeatureDTO {

	private OrsPropiedadesDTO properties;
	private OrsGeometriaDTO geometry;

	public OrsFeatureDTO() {
	}

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
