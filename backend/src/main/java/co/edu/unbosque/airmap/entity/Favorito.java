package co.edu.unbosque.airmap.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "favoritos")
public class Favorito {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Dueno del favorito
	@Column(nullable = false)
	private Long usuarioId;

	// Id del vuelo (el mismo de /api/vuelos/{id}). Solo se guarda el id: los datos del vuelo se piden en vivo
	@Column(nullable = false, length = 20)
	private String vueloId;

	@Column(length = 20)
	private String numeroVuelo;

	private LocalDateTime fechaGuardado;

	public Favorito() {
	}

	public Favorito(Long usuarioId, String vueloId, String numeroVuelo) {
		super();
		this.usuarioId = usuarioId;
		this.vueloId = vueloId;
		this.numeroVuelo = numeroVuelo;
		this.fechaGuardado = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
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

	public LocalDateTime getFechaGuardado() {
		return fechaGuardado;
	}

	public void setFechaGuardado(LocalDateTime fechaGuardado) {
		this.fechaGuardado = fechaGuardado;
	}

	@Override
	public String toString() {
		return "Favorito [id=" + id + ", usuarioId=" + usuarioId + ", vueloId=" + vueloId + ", numeroVuelo="
				+ numeroVuelo + "]";
	}

}
