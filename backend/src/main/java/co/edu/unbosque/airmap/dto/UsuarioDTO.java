package co.edu.unbosque.airmap.dto;

/**
 * Datos públicos del usuario (sin la contraseña).
 *
 * @author SakiYoshida
 * @version 1.0
 */
public class UsuarioDTO {

	private String nombre;
	private String correo;
	private String proveedor;
	// USUARIO o ADMIN
	private String rol;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public UsuarioDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param nombre nombre completo del usuario
	 * @param correo correo electrónico
	 * @param proveedor forma de ingreso (LOCAL o GOOGLE)
	 * @param rol rol del usuario (USUARIO o ADMIN)
	 */
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
