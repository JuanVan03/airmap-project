package co.edu.unbosque.airmap.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad de la tabla rutas_guardadas. Guarda una ruta terrestre hasta un aeropuerto para
 * abrirla después.
 *
 * @author SakiYoshida
 * @version 1.0
 */
@Entity
@Table(name = "rutas_guardadas")
public class RutaGuardada {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long usuarioId;

	@Column(nullable = false, length = 100)
	private String nombre;

	// Punto de salida (donde estaba el usuario)
	private double origenLat;
	private double origenLon;

	// Aeropuerto de destino
	@Column(nullable = false, length = 10)
	private String destinoCodigo;

	@Column(nullable = false, length = 150)
	private String destinoNombre;

	private double destinoLat;
	private double destinoLon;

	// auto, pie o bici
	@Column(nullable = false, length = 10)
	private String modo;

	// Resultado de cuando se guardo, para mostrarlo en la lista sin volver a calcular
	private double distanciaKm;
	private int duracionMin;

	private LocalDateTime fechaGuardado;

	/**
	 * Constructor vacío que necesita JPA.
	 */
	public RutaGuardada() {
	}

	/**
	 * Crea el registro con sus datos. El id lo pone la base de datos. La fecha se toma del momento actual.
	 *
	 * @param usuarioId id del usuario dueño del registro
	 * @param nombre nombre que le puso el usuario a la ruta
	 * @param origenLat latitud del punto de salida
	 * @param origenLon longitud del punto de salida
	 * @param destinoCodigo código IATA del aeropuerto de destino
	 * @param destinoNombre nombre del aeropuerto de destino
	 * @param destinoLat latitud del aeropuerto de destino
	 * @param destinoLon longitud del aeropuerto de destino
	 * @param modo medio de transporte (auto, pie o bici)
	 * @param distanciaKm distancia en kilómetros
	 * @param duracionMin duración en minutos
	 */
	public RutaGuardada(Long usuarioId, String nombre, double origenLat, double origenLon, String destinoCodigo,
			String destinoNombre, double destinoLat, double destinoLon, String modo, double distanciaKm,
			int duracionMin) {
		super();
		this.usuarioId = usuarioId;
		this.nombre = nombre;
		this.origenLat = origenLat;
		this.origenLon = origenLon;
		this.destinoCodigo = destinoCodigo;
		this.destinoNombre = destinoNombre;
		this.destinoLat = destinoLat;
		this.destinoLon = destinoLon;
		this.modo = modo;
		this.distanciaKm = distanciaKm;
		this.duracionMin = duracionMin;
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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getOrigenLat() {
		return origenLat;
	}

	public void setOrigenLat(double origenLat) {
		this.origenLat = origenLat;
	}

	public double getOrigenLon() {
		return origenLon;
	}

	public void setOrigenLon(double origenLon) {
		this.origenLon = origenLon;
	}

	public String getDestinoCodigo() {
		return destinoCodigo;
	}

	public void setDestinoCodigo(String destinoCodigo) {
		this.destinoCodigo = destinoCodigo;
	}

	public String getDestinoNombre() {
		return destinoNombre;
	}

	public void setDestinoNombre(String destinoNombre) {
		this.destinoNombre = destinoNombre;
	}

	public double getDestinoLat() {
		return destinoLat;
	}

	public void setDestinoLat(double destinoLat) {
		this.destinoLat = destinoLat;
	}

	public double getDestinoLon() {
		return destinoLon;
	}

	public void setDestinoLon(double destinoLon) {
		this.destinoLon = destinoLon;
	}

	public String getModo() {
		return modo;
	}

	public void setModo(String modo) {
		this.modo = modo;
	}

	public double getDistanciaKm() {
		return distanciaKm;
	}

	public void setDistanciaKm(double distanciaKm) {
		this.distanciaKm = distanciaKm;
	}

	public int getDuracionMin() {
		return duracionMin;
	}

	public void setDuracionMin(int duracionMin) {
		this.duracionMin = duracionMin;
	}

	public LocalDateTime getFechaGuardado() {
		return fechaGuardado;
	}

	public void setFechaGuardado(LocalDateTime fechaGuardado) {
		this.fechaGuardado = fechaGuardado;
	}

	@Override
	public String toString() {
		return "RutaGuardada [id=" + id + ", usuarioId=" + usuarioId + ", nombre=" + nombre + ", destinoCodigo="
				+ destinoCodigo + ", modo=" + modo + "]";
	}

}
