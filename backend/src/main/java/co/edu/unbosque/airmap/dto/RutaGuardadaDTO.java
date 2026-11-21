package co.edu.unbosque.airmap.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class RutaGuardadaDTO {

	private Long id;

	@NotBlank(message = "Ponle un nombre a la ruta.")
	@Size(max = 100, message = "El nombre es muy largo.")
	private String nombre;

	@NotNull(message = "Falta la latitud de salida.")
	@DecimalMin(value = "-90", message = "Latitud de salida no valida.")
	@DecimalMax(value = "90", message = "Latitud de salida no valida.")
	private Double origenLat;

	@NotNull(message = "Falta la longitud de salida.")
	@DecimalMin(value = "-180", message = "Longitud de salida no valida.")
	@DecimalMax(value = "180", message = "Longitud de salida no valida.")
	private Double origenLon;

	@NotBlank(message = "Falta el codigo del aeropuerto.")
	@Size(max = 10, message = "Codigo de aeropuerto no valido.")
	private String destinoCodigo;

	@NotBlank(message = "Falta el nombre del aeropuerto.")
	@Size(max = 150, message = "El nombre del aeropuerto es muy largo.")
	private String destinoNombre;

	@NotNull(message = "Falta la latitud del aeropuerto.")
	@DecimalMin(value = "-90", message = "Latitud del aeropuerto no valida.")
	@DecimalMax(value = "90", message = "Latitud del aeropuerto no valida.")
	private Double destinoLat;

	@NotNull(message = "Falta la longitud del aeropuerto.")
	@DecimalMin(value = "-180", message = "Longitud del aeropuerto no valida.")
	@DecimalMax(value = "180", message = "Longitud del aeropuerto no valida.")
	private Double destinoLon;

	@Pattern(regexp = "^(auto|pie|bici)$", message = "El modo debe ser auto, pie o bici.")
	private String modo;

	@NotNull(message = "Falta la distancia.")
	@PositiveOrZero(message = "Distancia no valida.")
	private Double distanciaKm;

	@NotNull(message = "Falta la duracion.")
	@PositiveOrZero(message = "Duracion no valida.")
	private Integer duracionMin;

	public RutaGuardadaDTO() {
	}

	public RutaGuardadaDTO(Long id, String nombre, Double origenLat, Double origenLon, String destinoCodigo,
			String destinoNombre, Double destinoLat, Double destinoLon, String modo, Double distanciaKm,
			Integer duracionMin) {
		super();
		this.id = id;
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
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Double getOrigenLat() {
		return origenLat;
	}

	public void setOrigenLat(Double origenLat) {
		this.origenLat = origenLat;
	}

	public Double getOrigenLon() {
		return origenLon;
	}

	public void setOrigenLon(Double origenLon) {
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

	public Double getDestinoLat() {
		return destinoLat;
	}

	public void setDestinoLat(Double destinoLat) {
		this.destinoLat = destinoLat;
	}

	public Double getDestinoLon() {
		return destinoLon;
	}

	public void setDestinoLon(Double destinoLon) {
		this.destinoLon = destinoLon;
	}

	public String getModo() {
		return modo;
	}

	public void setModo(String modo) {
		this.modo = modo;
	}

	public Double getDistanciaKm() {
		return distanciaKm;
	}

	public void setDistanciaKm(Double distanciaKm) {
		this.distanciaKm = distanciaKm;
	}

	public Integer getDuracionMin() {
		return duracionMin;
	}

	public void setDuracionMin(Integer duracionMin) {
		this.duracionMin = duracionMin;
	}

	@Override
	public String toString() {
		return "RutaGuardadaDTO [id=" + id + ", nombre=" + nombre + ", destinoCodigo=" + destinoCodigo + ", modo="
				+ modo + "]";
	}

}
