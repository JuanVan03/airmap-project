package co.edu.unbosque.airmap.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Apariencia de la pagina que eligio cada usuario (una fila por usuario)
@Entity
@Table(name = "preferencias")
public class Preferencias {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private Long usuarioId;

	@Column(nullable = false, length = 20)
	private String tema;

	@Column(nullable = false, length = 20)
	private String letra;

	@Column(nullable = false, length = 20)
	private String tamano;

	@Column(nullable = false, length = 20)
	private String color;

	@Column(nullable = false, length = 20)
	private String avion;

	private LocalDateTime fechaActualizado;

	public Preferencias() {
	}

	public Preferencias(Long usuarioId) {
		super();
		this.usuarioId = usuarioId;
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

	public String getTema() {
		return tema;
	}

	public void setTema(String tema) {
		this.tema = tema;
	}

	public String getLetra() {
		return letra;
	}

	public void setLetra(String letra) {
		this.letra = letra;
	}

	public String getTamano() {
		return tamano;
	}

	public void setTamano(String tamano) {
		this.tamano = tamano;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getAvion() {
		return avion;
	}

	public void setAvion(String avion) {
		this.avion = avion;
	}

	public LocalDateTime getFechaActualizado() {
		return fechaActualizado;
	}

	public void setFechaActualizado(LocalDateTime fechaActualizado) {
		this.fechaActualizado = fechaActualizado;
	}

	@Override
	public String toString() {
		return "Preferencias [id=" + id + ", usuarioId=" + usuarioId + ", tema=" + tema + ", letra=" + letra
				+ ", tamano=" + tamano + ", color=" + color + ", avion=" + avion + "]";
	}

}
