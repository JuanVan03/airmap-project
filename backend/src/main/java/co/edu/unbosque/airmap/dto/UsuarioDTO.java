package co.edu.unbosque.airmap.dto;

public class UsuarioDTO {

	private String nombre;
	private String correo;
	private String proveedor;

	public UsuarioDTO() {
	}

	public UsuarioDTO(String nombre, String correo, String proveedor) {
		super();
		this.nombre = nombre;
		this.correo = correo;
		this.proveedor = proveedor;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getProveedor() {
		return proveedor;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}

	@Override
	public String toString() {
		return "UsuarioDTO [nombre=" + nombre + ", correo=" + correo + ", proveedor=" + proveedor + "]";
	}

}
