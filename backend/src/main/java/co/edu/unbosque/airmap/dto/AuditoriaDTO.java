package co.edu.unbosque.airmap.dto;

/**
 * Registro de auditoría tal como lo ve el administrador.
 *
 * @author Juan
 * @version 1.0
 */
public class AuditoriaDTO {

	private Long id;
	// Fecha y hora en formato ISO: 2026-11-21T15:30:00
	private String fecha;
	private String usuario;
	private String accion;
	private String resultado;
	private String detalle;
	private String ip;
	private String navegador;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public AuditoriaDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param id id del registro
	 * @param fecha fecha y hora del registro
	 * @param usuario correo del usuario que hizo la acción
	 * @param accion nombre de la acción (por ejemplo LOGIN_EXITOSO)
	 * @param resultado resultado de la acción (EXITO o FALLO)
	 * @param detalle descripción de lo que pasó
	 * @param ip dirección IP desde donde se hizo la petición
	 * @param navegador navegador desde donde se hizo la petición
	 */
	public AuditoriaDTO(Long id, String fecha, String usuario, String accion, String resultado, String detalle,
			String ip, String navegador) {
		super();
		this.id = id;
		this.fecha = fecha;
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

	public void setId(Long id) {
		this.id = id;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getNavegador() {
		return navegador;
	}

	public void setNavegador(String navegador) {
		this.navegador = navegador;
	}

}
