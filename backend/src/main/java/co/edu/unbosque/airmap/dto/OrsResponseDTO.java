package co.edu.unbosque.airmap.dto;

import java.util.List;

public class OrsResponseDTO {

	private List<OrsFeatureDTO> features;

	public OrsResponseDTO() {
	}

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
