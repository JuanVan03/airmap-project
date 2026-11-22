package co.edu.unbosque.airmap.dto;

/**
 * Ruta de TransMilenio que llega al aeropuerto sin transbordo.
 *
 * @author Juan
 * @version 1.0
 */
public class RutaBusDTO {

	private String codigo;
	private String nombre;
	private String bajada;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public RutaBusDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param codigo código de la ruta (por ejemplo K86)
	 * @param nombre nombre de la ruta
	 * @param bajada paradero donde hay que bajarse en el aeropuerto
	 */
	public RutaBusDTO(String codigo, String nombre, String bajada) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.bajada = bajada;
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

	public String getBajada() {
		return bajada;
	}

	public void setBajada(String bajada) {
		this.bajada = bajada;
	}

	@Override
	public String toString() {
		return "RutaBusDTO [" + "codigo=" + codigo + ", nombre=" + nombre + ", bajada=" + bajada + "]";
	}

}
