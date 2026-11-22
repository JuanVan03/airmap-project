package co.edu.unbosque.airmap.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad de la tabla historial_vuelos. Guarda los vuelos que el usuario ha abierto.
 *
 * @author SakiYoshida
 * @version 1.0
 */
@Entity
@Table(name = "historial_vuelos")
public class HistorialVuelo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Dueno del historial
	@Column(nullable = false)
	private Long usuarioId;

	// Id del vuelo (el mismo de /api/vuelos/{id}). Solo se guarda el id: los datos del vuelo se piden en vivo
	@Column(nullable = false, length = 20)
	private String vueloId;

	@Column(length = 20)
	private String numeroVuelo;

	// Ultima vez que lo abrio
	@Column(nullable = false)
	private LocalDateTime fechaVista;

	/**
	 * Constructor vacío que necesita JPA.
	 */
	public HistorialVuelo() {
	}

	/**
	 * Crea el registro con sus datos. El id lo pone la base de datos. La fecha se toma del momento actual.
	 *
	 * @param usuarioId id del usuario dueño del registro
	 * @param vueloId id del vuelo
	 * @param numeroVuelo número o indicativo del vuelo (por ejemplo AVA010)
	 */
	public HistorialVuelo(Long usuarioId, String vueloId, String numeroVuelo) {
		super();
		this.usuarioId = usuarioId;
		this.vueloId = vueloId;
		this.numeroVuelo = numeroVuelo;
		this.fechaVista = LocalDateTime.now();
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

	public LocalDateTime getFechaVista() {
		return fechaVista;
	}

	public void setFechaVista(LocalDateTime fechaVista) {
		this.fechaVista = fechaVista;
	}

}
