package co.edu.unbosque.airmap.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Un renglon por cada cosa que pasa en AirMap. Solo se escribe, nunca se edita ni se borra.
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

	public Auditoria() {
	}

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
