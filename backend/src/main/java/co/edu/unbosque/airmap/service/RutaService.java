package co.edu.unbosque.airmap.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import co.edu.unbosque.airmap.dto.OrsFeatureDTO;
import co.edu.unbosque.airmap.dto.OrsResponseDTO;
import co.edu.unbosque.airmap.dto.RutaDTO;
import co.edu.unbosque.airmap.exception.ExternalApiException;

@Service
public class RutaService {

	@Value("${external.ors.url}")
	private String baseUrl;

	@Value("${external.ors.apikey}")
	private String apiKey;

	public RutaService() {
	}

	public RutaDTO calcular(double latOrigen, double lonOrigen, double latDestino, double lonDestino, String modo) {
		if (apiKey.isBlank()) {
			throw new ExternalApiException("Falta la clave de openrouteservice (ORS_API_KEY en el .env del back).", 503);
		}

		// Medio de transporte que entiende openrouteservice
		String perfil = "driving-car";
		if (modo.equals("pie")) {
			perfil = "foot-walking";
		} else if (modo.equals("bici")) {
			perfil = "cycling-regular";
		}

		// Las coordenadas van como [longitud, latitud]
		// El -1 permite buscar la via mas cercana sin limite de distancia
		Map<String, Object> cuerpo = new HashMap<>();
		cuerpo.put("coordinates", List.of(List.of(lonOrigen, latOrigen), List.of(lonDestino, latDestino)));
		cuerpo.put("radiuses", List.of(-1, -1));

		OrsResponseDTO respuesta;
		try {
			respuesta = ExternalHTTPRequestHandler.doPostAndConvertToDto(
					baseUrl + "/v2/directions/" + perfil + "/geojson", apiKey, cuerpo, OrsResponseDTO.class);
		} catch (ExternalApiException ex) {
			throw new ExternalApiException(mensajeError(ex.getStatusCode()), 502, ex);
		}

		if (respuesta == null || respuesta.getFeatures() == null || respuesta.getFeatures().isEmpty()) {
			throw new ExternalApiException("No se encontro una ruta hasta el aeropuerto.", 502);
		}

		OrsFeatureDTO ruta = respuesta.getFeatures().get(0);

		// Leaflet usa [latitud, longitud], asi que se invierte cada punto
		List<List<Double>> puntos = new ArrayList<>();
		for (List<Double> punto : ruta.getGeometry().getCoordinates()) {
			puntos.add(List.of(punto.get(1), punto.get(0)));
		}

		// Metros a kilometros con un decimal, y segundos a minutos
		double km = Math.round(ruta.getProperties().getSummary().getDistance() / 100.0) / 10.0;
		int minutos = (int) Math.round(ruta.getProperties().getSummary().getDuration() / 60.0);
		return new RutaDTO(km, minutos, puntos);
	}

	private String mensajeError(int status) {
		if (status == 401 || status == 403) {
			return "La clave de openrouteservice no es valida o se acabo el limite diario.";
		}
		if (status == 404) {
			return "No se encontro una via cerca de tu ubicacion o del aeropuerto.";
		}
		if (status == 400) {
			return "No se pudo calcular: la distancia es demasiado larga para ese medio de transporte.";
		}
		if (status == 429) {
			return "Hay demasiadas consultas, intenta de nuevo en un minuto.";
		}
		return "No se pudo calcular la ruta en este momento.";
	}

}
