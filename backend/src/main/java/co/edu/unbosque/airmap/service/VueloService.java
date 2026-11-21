package co.edu.unbosque.airmap.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import co.edu.unbosque.airmap.dto.AdsbdbResponseDTO;
import co.edu.unbosque.airmap.dto.AdsbdbRutaDTO;
import co.edu.unbosque.airmap.dto.OpenSkyResponseDTO;
import co.edu.unbosque.airmap.dto.RespuestaVuelosDTO;
import co.edu.unbosque.airmap.dto.VueloDTO;
import co.edu.unbosque.airmap.dto.VueloResumenDTO;
import co.edu.unbosque.airmap.exception.ExternalApiException;

@Service
public class VueloService {

	@Value("${external.opensky.url}")
	private String baseUrl;

	@Value("${external.adsbdb.url}")
	private String adsbdbUrl;

	// Maximo de aviones que se envian al mapa
	private static final int MAXIMO_VUELOS = 1500;

	// Rutas ya consultadas a adsbdb, para no repetir la misma peticion
	private Map<String, AdsbdbRutaDTO> rutas = new HashMap<>();

	// Ultima respuesta de OpenSky y el momento en que se pidio
	private List<List<Object>> estados = new ArrayList<>();
	private long ultimaConsulta = 0;

	public VueloService() {
	}

	public RespuestaVuelosDTO getVuelosActivos() {
		List<List<Object>> enElAire = new ArrayList<>();

		for (List<Object> e : obtenerEstados()) {
			// Sin posicion no se puede dibujar, y los aviones en tierra solo llenan los aeropuertos
			if (e.get(5) == null || e.get(6) == null || Boolean.TRUE.equals(e.get(8))) {
				continue;
			}
			enElAire.add(e);
		}

		// Si hay mas del maximo tomamos uno cada tanto, asi quedan repartidos por todo el mundo
		int salto = Math.max(1, enElAire.size() / MAXIMO_VUELOS);
		List<VueloResumenDTO> vuelos = new ArrayList<>();
		for (int i = 0; i < enElAire.size() && vuelos.size() < MAXIMO_VUELOS; i += salto) {
			List<Object> e = enElAire.get(i);
			vuelos.add(new VueloResumenDTO(leerTexto(e, 0), nombreVuelo(e), leerNumero(e, 6), leerNumero(e, 5),
					leerNumero(e, 10), false));
		}

		return new RespuestaVuelosDTO(Instant.ofEpochMilli(ultimaConsulta).toString(), vuelos.size(), vuelos);
	}

	public VueloDTO getVuelo(String id) {
		for (List<Object> e : obtenerEstados()) {
			if (leerTexto(e, 0).equalsIgnoreCase(id.trim())) {
				VueloDTO vuelo = convertirADetalle(e);
				agregarRuta(vuelo, leerTexto(e, 1).trim());
				return vuelo;
			}
		}
		throw new ExternalApiException("No se encontro el vuelo " + id, 404);
	}

	public List<VueloDTO> buscar(String texto) {
		List<VueloDTO> encontrados = new ArrayList<>();
		if (texto.isBlank()) {
			return encontrados;
		}

		for (List<Object> e : obtenerEstados()) {
			// Se busca dentro del indicativo del vuelo (callsign)
			if (leerTexto(e, 1).toLowerCase().contains(texto.trim().toLowerCase())) {
				encontrados.add(convertirADetalle(e));
			}
			// Con 20 resultados es suficiente
			if (encontrados.size() == 20) {
				break;
			}
		}
		return encontrados;
	}

	private synchronized List<List<Object>> obtenerEstados() {
		// Guardamos la respuesta 30 segundos para no gastar el limite diario de OpenSky
		if (System.currentTimeMillis() - ultimaConsulta < 30000) {
			return estados;
		}

		try {
			OpenSkyResponseDTO respuesta = ExternalHTTPRequestHandler
					.doGetAndConvertToDto(baseUrl + "/states/all", OpenSkyResponseDTO.class);
			estados = (respuesta == null || respuesta.getStates() == null) ? new ArrayList<>() : respuesta.getStates();
			ultimaConsulta = System.currentTimeMillis();
		} catch (ExternalApiException ex) {
			// Si OpenSky falla usamos la ultima respuesta guardada, y si no hay ninguna mostramos el error
			if (estados.isEmpty()) {
				throw ex;
			}
		}
		return estados;
	}

	// Completa aerolinea, origen y destino con los datos de adsbdb
	private void agregarRuta(VueloDTO vuelo, String callsign) {
		// Sin indicativo no hay forma de buscar la ruta
		if (callsign.isEmpty() || !callsign.matches("[A-Za-z0-9]+")) {
			return;
		}

		AdsbdbRutaDTO ruta = buscarRuta(callsign);
		if (ruta == null) {
			return;
		}

		vuelo.setOrigen(ruta.getOrigin());
		vuelo.setDestino(ruta.getDestination());
		if (ruta.getAirline() != null) {
			vuelo.setAerolinea(ruta.getAirline().getName());
		}
	}

	private synchronized AdsbdbRutaDTO buscarRuta(String callsign) {
		// Si ya la pedimos antes no volvemos a llamar a la API
		if (rutas.containsKey(callsign)) {
			return rutas.get(callsign);
		}

		AdsbdbRutaDTO ruta = null;
		try {
			AdsbdbResponseDTO respuesta = ExternalHTTPRequestHandler
					.doGetAndConvertToDto(adsbdbUrl + "/callsign/" + callsign, AdsbdbResponseDTO.class);
			if (respuesta != null && respuesta.getResponse() != null) {
				ruta = respuesta.getResponse().getFlightroute();
			}
		} catch (ExternalApiException ex) {
			// Si adsbdb falla el vuelo se muestra igual sin ruta, y se intenta de nuevo la proxima vez
			if (ex.getStatusCode() != 404) {
				return null;
			}
		}

		// Si adsbdb no conoce el vuelo (404) se guarda vacio para no volver a preguntar
		rutas.put(callsign, ruta);
		return ruta;
	}

	private VueloDTO convertirADetalle(List<Object> e) {
		boolean enTierra = Boolean.TRUE.equals(e.get(8));
		// OpenSky solo informa si el avion esta en el aire o en tierra
		String estado = enTierra ? "ATERRIZADO" : "EN_VUELO";
		// OpenSky entrega la velocidad en m/s, la pasamos a km/h
		int velocidad = (int) Math.round(leerNumero(e, 9) * 3.6);
		int altitud = (int) Math.round(leerNumero(e, 7));

		// Aerolinea, origen y destino se completan despues con adsbdb, las horas quedan en null
		return new VueloDTO(leerTexto(e, 0), nombreVuelo(e), null, estado, leerNumero(e, 6), leerNumero(e, 5),
				leerNumero(e, 10), altitud, velocidad, null, null, null, null);
	}

	// Si el avion no trae indicativo usamos su codigo
	private String nombreVuelo(List<Object> e) {
		String callsign = leerTexto(e, 1).trim();
		return callsign.isEmpty() ? leerTexto(e, 0).toUpperCase() : callsign;
	}

	private String leerTexto(List<Object> e, int posicion) {
		Object valor = e.get(posicion);
		return valor == null ? "" : valor.toString();
	}

	private double leerNumero(List<Object> e, int posicion) {
		Object valor = e.get(posicion);
		return valor == null ? 0 : ((Number) valor).doubleValue();
	}

}
