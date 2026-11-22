package co.edu.unbosque.airmap.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad de la tabla usuarios. Puede ser una cuenta normal (LOCAL), una cuenta de Google o
 * el administrador.
 *
 * @author SakiYoshida
 * @version 1.0
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String nombre;

	@Column(nullable = false, unique = true, length = 150)
	private String correo;

	// Los usuarios de Google no tienen contrasena
	private String contrasena;

	// LOCAL o GOOGLE
	@Column(nullable = false, length = 10)
	private String proveedor;

	private LocalDateTime fechaRegistro;

	// Una cuenta local no puede ingresar hasta confirmar su correo. El valor por defecto
	// (true) hace que las cuentas que ya existian queden como confirmadas.
	@Column(nullable = false, columnDefinition = "boolean not null default true")
	private boolean verificado;

	// USUARIO (el normal) o ADMIN (solo hay uno y no se registra: se crea al iniciar el back)
	@Column(nullable = false, length = 10, columnDefinition = "varchar(10) not null default 'USUARIO'")
	private String rol = "USUARIO";

	/**
	 * Constructor vacío que necesita JPA.
	 */
	public Usuario() {
	}

	/**
	 * Crea el registro con sus datos. El id lo pone la base de datos. La fecha se toma del momento actual.
	 *
	 * @param nombre nombre completo del usuario
	 * @param correo correo electrónico
	 * @param contrasena contraseña cifrada con BCrypt (null en cuentas de Google)
	 * @param proveedor forma de ingreso (LOCAL o GOOGLE)
	 */
	public Usuario(String nombre, String correo, String contrasena, String proveedor) {
		super();
		this.nombre = nombre;
		this.correo = correo;
		this.contrasena = contrasena;
		this.proveedor = proveedor;
		this.fechaRegistro = LocalDateTime.now();
		// Con Google el correo ya viene confirmado; con contrasena se confirma por correo
		this.verificado = "GOOGLE".equals(proveedor);
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

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public String getProveedor() {
		return proveedor;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}

	public LocalDateTime getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(LocalDateTime fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public boolean isVerificado() {
		return verificado;
	}

	public void setVerificado(boolean verificado) {
		this.verificado = verificado;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nombre=" + nombre + ", correo=" + correo + ", proveedor=" + proveedor + "]";
	}

}
