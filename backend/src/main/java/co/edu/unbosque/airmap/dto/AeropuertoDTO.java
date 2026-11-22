package co.edu.unbosque.airmap.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Datos de un aeropuerto. Se usa en la búsqueda y como origen o destino de un vuelo.
 *
 * @author SakiYoshida
 * @version 1.0
 */
public class AeropuertoDTO {

	@SerializedName("iata_code")
	private String codigo;
	@SerializedName("name")
	private String nombre;
	@SerializedName("municipality")
	private String ciudad;
	@SerializedName("country_name")
	private String pais;
	@SerializedName("latitude")
	private double latitud;
	@SerializedName("longitude")
	private double longitud;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public AeropuertoDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param codigo código IATA del aeropuerto (por ejemplo BOG)
	 * @param nombre nombre del aeropuerto
	 * @param ciudad ciudad del aeropuerto
	 * @param pais país del aeropuerto
	 * @param latitud latitud
	 * @param longitud longitud
	 */
	public AeropuertoDTO(String codigo, String nombre, String ciudad, String pais, double latitud, double longitud) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.ciudad = ciudad;
		this.pais = pais;
		this.latitud = latitud;
		this.longitud = longitud;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	@Override
	public String toString() {
		return "AeropuertoDTO [" + "codigo=" + codigo + ", nombre=" + nombre + ", ciudad=" + ciudad + ", pais=" + pais + ", latitud=" + latitud + ", longitud=" + longitud + "]";
	}

}
