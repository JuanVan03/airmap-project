package co.edu.unbosque.airmap.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad de la tabla auditoria. Cada fila es una acción que pasó en AirMap.
 *
 * @author SakiYoshida
 * @version 1.0
 */
@Entity
@Table(name = "auditoria")
public class Auditoria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private LocalDateTime fecha;

	// Correo de quien hizo la accion ("anonimo" si no habia sesion, "sistema" si la hizo la aplicacion).
	// Es texto y no una llave a usuarios, para que el registro se conserve aunque la cuenta cambie.
	@Column(nullable = false, length = 150)
	private String usuario;

	// Codigo de la accion: LOGIN_EXITOSO, CAMBIO_CONTRASENA, FAVORITO_AGREGADO...
	@Column(nullable = false, length = 40)
	private String accion;

	// EXITO o FALLO
	@Column(nullable = false, length = 10)
	private String resultado;

	@Column(length = 500)
	private String detalle;

	@Column(length = 64)
	private String ip;

	@Column(length = 200)
	private String navegador;

	/**
	 * Constructor vacío que necesita JPA.
	 */
	public Auditoria() {
	}

	/**
	 * Crea el registro con sus datos. El id lo pone la base de datos. La fecha se toma del momento actual.
	 *
	 * @param usuario correo del usuario que hizo la acción
	 * @param accion nombre de la acción (por ejemplo LOGIN_EXITOSO)
	 * @param resultado resultado de la acción (EXITO o FALLO)
	 * @param detalle descripción de lo que pasó
	 * @param ip dirección IP desde donde se hizo la petición
	 * @param navegador navegador desde donde se hizo la petición
	 */
	public Auditoria(String usuario, String accion, String resultado, String detalle, String ip, String navegador) {
		super();
		this.fecha = LocalDateTime.now();
		this.usuario = usuario;
		this.accion = accion;
		this.resultado = resultado;
		this.detalle = detalle;
		this.ip = ip;
		this.navegador = navegador;
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public String getUsuario() {
		return usuario;
	}

	public String getAccion() {
		return accion;
	}

	public String getResultado() {
		return resultado;
	}

	public String getDetalle() {
		return detalle;
	}

	public String getIp() {
		return ip;
	}

	public String getNavegador() {
		return navegador;
	}

}
