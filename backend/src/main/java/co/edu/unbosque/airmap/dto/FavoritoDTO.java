package co.edu.unbosque.airmap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class FavoritoDTO {

	private Long id;

	@NotBlank(message = "Falta el id del vuelo.")
	@Size(max = 20, message = "El id del vuelo es muy largo.")
	private String vueloId;

	@Size(max = 20, message = "El numero de vuelo es muy largo.")
	private String numeroVuelo;

	public FavoritoDTO() {
	}

	public FavoritoDTO(Long id, String vueloId, String numeroVuelo) {
		super();
		this.id = id;
		this.vueloId = vueloId;
		this.numeroVuelo = numeroVuelo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVueloId() {
		return vueloId;
	}

	public void setVueloId(String vueloId) {
		this.vueloId = vueloId;
	}

	public String getNumeroVuelo() {
		return numeroVuelo;
	}

	public void setNumeroVuelo(String numeroVuelo) {
		this.numeroVuelo = numeroVuelo;
	}

	@Override
	public String toString() {
		return "FavoritoDTO [id=" + id + ", vueloId=" + vueloId + ", numeroVuelo=" + numeroVuelo + "]";
	}

}
