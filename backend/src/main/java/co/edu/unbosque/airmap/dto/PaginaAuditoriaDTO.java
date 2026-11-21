package co.edu.unbosque.airmap.dto;

import java.util.List;

// Una pagina de resultados de la auditoria
public class PaginaAuditoriaDTO {

	private List<AuditoriaDTO> contenido;
	private int pagina;
	private int tamano;
	private long totalElementos;
	private int totalPaginas;

	public PaginaAuditoriaDTO() {
	}

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
