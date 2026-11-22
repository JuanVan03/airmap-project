package co.edu.unbosque.airmap.dto;

// Ruta que llega al aeropuerto sin transbordo, bajada es el paradero donde hay que bajarse
public class RutaBusDTO {

	private String codigo;
	private String nombre;
	private String bajada;

	public RutaBusDTO() {
	}

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
