package co.edu.unbosque.airmap.dto;

public class UsuarioDTO {

	private String nombre;
	private String correo;
	private String proveedor;
	// USUARIO o ADMIN
	private String rol;

	public UsuarioDTO() {
	}

	public UsuarioDTO(String nombre, String correo, String proveedor, String rol) {
		super();
		this.nombre = nombre;
		this.correo = correo;
		this.proveedor = proveedor;
		this.rol = rol;
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

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	@Override
	public String toString() {
		return "UsuarioDTO [nombre=" + nombre + ", correo=" + correo + ", proveedor=" + proveedor + ", rol=" + rol + "]";
	}

}
