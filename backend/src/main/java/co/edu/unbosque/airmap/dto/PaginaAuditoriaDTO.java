package co.edu.unbosque.airmap.dto;

import java.util.List;

/**
 * Una página de registros de auditoría con los datos para paginar.
 *
 * @author Juan
 * @version 1.0
 */
public class PaginaAuditoriaDTO {

	private List<AuditoriaDTO> contenido;
	private int pagina;
	private int tamano;
	private long totalElementos;
	private int totalPaginas;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public PaginaAuditoriaDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param contenido registros de la página
	 * @param pagina número de la página (empieza en 0)
	 * @param tamano cantidad de registros por página
	 * @param totalElementos cantidad total de registros
	 * @param totalPaginas cantidad total de páginas
	 */
	public PaginaAuditoriaDTO(List<AuditoriaDTO> contenido, int pagina, int tamano, long totalElementos,
			int totalPaginas) {
		super();
		this.contenido = contenido;
		this.pagina = pagina;
		this.tamano = tamano;
		this.totalElementos = totalElementos;
		this.totalPaginas = totalPaginas;
	}

	public List<AuditoriaDTO> getContenido() {
		return contenido;
	}

	public void setContenido(List<AuditoriaDTO> contenido) {
		this.contenido = contenido;
	}

	public int getPagina() {
		return pagina;
	}

	public void setPagina(int pagina) {
		this.pagina = pagina;
	}

	public int getTamano() {
		return tamano;
	}

	public void setTamano(int tamano) {
		this.tamano = tamano;
	}

	public long getTotalElementos() {
		return totalElementos;
	}

	public void setTotalElementos(long totalElementos) {
		this.totalElementos = totalElementos;
	}

	public int getTotalPaginas() {
		return totalPaginas;
	}

	public void setTotalPaginas(int totalPaginas) {
		this.totalPaginas = totalPaginas;
	}

}
