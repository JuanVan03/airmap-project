package co.edu.unbosque.airmap.dto;

/**
 * Información completa de un vuelo.
 *
 * @author SakiYoshida
 * @version 1.0
 */
public class VueloDTO {

	private String id;
	private String numeroVuelo;
	private String aerolinea;
	private String estado;
	private double latitud;
	private double longitud;
	private double rumbo;
	private int altitud;
	private int velocidad;
	private AeropuertoDTO origen;
	private AeropuertoDTO destino;
	private String horaSalida;
	private String horaLlegada;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public VueloDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param id id del vuelo (código icao24 del avión)
	 * @param numeroVuelo número o indicativo del vuelo (por ejemplo AVA010)
	 * @param aerolinea nombre de la aerolínea
	 * @param estado estado del vuelo
	 * @param latitud latitud
	 * @param longitud longitud
	 * @param rumbo rumbo del avión en grados
	 * @param altitud altitud en metros
	 * @param velocidad velocidad en km/h
	 * @param origen aeropuerto de origen
	 * @param destino aeropuerto de destino
	 * @param horaSalida hora de salida
	 * @param horaLlegada hora estimada de llegada
	 */
	public VueloDTO(String id, String numeroVuelo, String aerolinea, String estado, double latitud, double longitud, double rumbo, int altitud, int velocidad, AeropuertoDTO origen, AeropuertoDTO destino, String horaSalida, String horaLlegada) {
		super();
		this.id = id;
		this.numeroVuelo = numeroVuelo;
		this.aerolinea = aerolinea;
		this.estado = estado;
		this.latitud = latitud;
		this.longitud = longitud;
		this.rumbo = rumbo;
		this.altitud = altitud;
		this.velocidad = velocidad;
		this.origen = origen;
		this.destino = destino;
		this.horaSalida = horaSalida;
		this.horaLlegada = horaLlegada;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNumeroVuelo() {
		return numeroVuelo;
	}

	public void setNumeroVuelo(String numeroVuelo) {
		this.numeroVuelo = numeroVuelo;
	}

	public String getAerolinea() {
		return aerolinea;
	}

	public void setAerolinea(String aerolinea) {
		this.aerolinea = aerolinea;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public double getRumbo() {
		return rumbo;
	}

	public void setRumbo(double rumbo) {
		this.rumbo = rumbo;
	}

	public int getAltitud() {
		return altitud;
	}

	public void setAltitud(int altitud) {
		this.altitud = altitud;
	}

	public int getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}

	public AeropuertoDTO getOrigen() {
		return origen;
	}

	public void setOrigen(AeropuertoDTO origen) {
		this.origen = origen;
	}

	public AeropuertoDTO getDestino() {
		return destino;
	}

	public void setDestino(AeropuertoDTO destino) {
		this.destino = destino;
	}

	public String getHoraSalida() {
		return horaSalida;
	}

	public void setHoraSalida(String horaSalida) {
		this.horaSalida = horaSalida;
	}

	public String getHoraLlegada() {
		return horaLlegada;
	}

	public void setHoraLlegada(String horaLlegada) {
		this.horaLlegada = horaLlegada;
	}

	@Override
	public String toString() {
		return "VueloDTO [" + "id=" + id + ", numeroVuelo=" + numeroVuelo + ", aerolinea=" + aerolinea + ", estado=" + estado + ", latitud=" + latitud + ", longitud=" + longitud + ", rumbo=" + rumbo + ", altitud=" + altitud + ", velocidad=" + velocidad + ", origen=" + origen + ", destino=" + destino + ", horaSalida=" + horaSalida + ", horaLlegada=" + horaLlegada + "]";
	}

}
