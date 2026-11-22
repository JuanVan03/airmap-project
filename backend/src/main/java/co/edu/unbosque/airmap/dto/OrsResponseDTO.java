package co.edu.unbosque.airmap.dto;

import java.util.List;

/**
 * Respuesta completa de openrouteservice al calcular una ruta.
 *
 * @author Juan
 * @version 1.0
 */
public class OrsResponseDTO {

	private List<OrsFeatureDTO> features;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public OrsResponseDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param features rutas encontradas
	 */
	public OrsResponseDTO(List<OrsFeatureDTO> features) {
		super();
		this.features = features;
	}

	public List<OrsFeatureDTO> getFeatures() {
		return features;
	}

	public void setFeatures(List<OrsFeatureDTO> features) {
		this.features = features;
	}

	@Override
	public String toString() {
		return "OrsResponseDTO [" + "features=" + features + "]";
	}

}
