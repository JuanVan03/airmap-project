package co.edu.unbosque.airmap.dto;

/**
 * Próximo bus que llega al paradero.
 *
 * @author Juan
 * @version 1.0
 */
public class LlegadaDTO {

	private String ruta;
	private int minutos;
	private boolean aproximado;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public LlegadaDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param ruta código de la ruta del bus
	 * @param minutos minutos que faltan para que llegue
	 * @param aproximado true si el tiempo es un cálculo y no lo envió TransMilenio
	 */
	public LlegadaDTO(String ruta, int minutos, boolean aproximado) {
		super();
		this.ruta = ruta;
		this.minutos = minutos;
		this.aproximado = aproximado;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public int getMinutos() {
		return minutos;
	}

	public void setMinutos(int minutos) {
		this.minutos = minutos;
	}

	public boolean isAproximado() {
		return aproximado;
	}

	public void setAproximado(boolean aproximado) {
		this.aproximado = aproximado;
	}

	@Override
	public String toString() {
		return "LlegadaDTO [" + "ruta=" + ruta + ", minutos=" + minutos + ", aproximado=" + aproximado + "]";
	}

}
