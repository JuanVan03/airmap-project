package co.edu.unbosque.airmap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Vuelo del historial que se envía y se recibe del front.
 *
 * @author SakiYoshida
 * @version 1.0
 */
public class HistorialDTO {

	@NotBlank(message = "Falta el id del vuelo.")
	@Size(max = 20, message = "El id del vuelo es muy largo.")
	private String vueloId;

	@Size(max = 20, message = "El numero de vuelo es muy largo.")
	private String numeroVuelo;

	// Solo sale del back (ultima vez que lo abrio), en formato ISO: 2026-11-21T15:30:00
	private String fechaVista;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public HistorialDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param vueloId id del vuelo
	 * @param numeroVuelo número o indicativo del vuelo (por ejemplo AVA010)
	 * @param fechaVista fecha y hora en que abrió el vuelo
	 */
	public HistorialDTO(String vueloId, String numeroVuelo, String fechaVista) {
		super();
		this.vueloId = vueloId;
		this.numeroVuelo = numeroVuelo;
		this.fechaVista = fechaVista;
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

	public String getFechaVista() {
		return fechaVista;
	}

	public void setFechaVista(String fechaVista) {
		this.fechaVista = fechaVista;
	}

}
