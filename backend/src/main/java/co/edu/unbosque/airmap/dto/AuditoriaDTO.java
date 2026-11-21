package co.edu.unbosque.airmap.dto;

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

	public AuditoriaDTO() {
	}

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
