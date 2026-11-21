package co.edu.unbosque.airmap.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import co.edu.unbosque.airmap.dto.GeminiCandidatoDTO;
import co.edu.unbosque.airmap.dto.GeminiConfigDTO;
import co.edu.unbosque.airmap.dto.GeminiContenidoDTO;
import co.edu.unbosque.airmap.dto.GeminiParteDTO;
import co.edu.unbosque.airmap.dto.GeminiRespuestaDTO;
import co.edu.unbosque.airmap.dto.GeminiSolicitudDTO;
import co.edu.unbosque.airmap.exception.ExternalApiException;
import co.edu.unbosque.airmap.exception.NegocioException;

@Service
public class GeminiService {

	@Value("${external.gemini.url}")
	private String baseUrl;

	@Value("${external.gemini.apikey:}")
	private String apiKey;

	@Value("${external.gemini.modelo}")
	private String modelo;

	public GeminiService() {
	}

	// Le manda las instrucciones y la conversacion a Gemini y devuelve su respuesta en texto
	public String responder(String instrucciones, List<GeminiContenidoDTO> conversacion) {
		if (apiKey == null || apiKey.isBlank()) {
			throw new NegocioException("Falta la clave de Gemini (GEMINI_API_KEY en el .env del back).", 503,
					"ASISTENTE_NO_CONFIGURADO");
		}

		GeminiContenidoDTO sistema = new GeminiContenidoDTO(null, List.of(new GeminiParteDTO(instrucciones)));
		GeminiSolicitudDTO solicitud = new GeminiSolicitudDTO(sistema, conversacion, new GeminiConfigDTO(0.4, 2048));

		GeminiRespuestaDTO respuesta;
		try {
			// La clave va en el encabezado x-goog-api-key, asi no queda escrita en la URL
			respuesta = ExternalHTTPRequestHandler.doPostAndConvertToDto(
					baseUrl + "/models/" + modelo + ":generateContent", "x-goog-api-key", apiKey, solicitud,
					GeminiRespuestaDTO.class);
		} catch (ExternalApiException ex) {
			throw traducirError(ex);
		}

		String texto = extraerTexto(respuesta);
		if (texto.isBlank()) {
			throw new NegocioException("El asistente no pudo responder esa pregunta. Intenta reformularla.", 502,
					"ASISTENTE_SIN_RESPUESTA");
		}
		return texto;
	}

	// El texto esta en candidates[0].content.parts[].text
	private String extraerTexto(GeminiRespuestaDTO respuesta) {
		if (respuesta == null || respuesta.getCandidates() == null || respuesta.getCandidates().isEmpty()) {
			return "";
		}
		GeminiCandidatoDTO candidato = respuesta.getCandidates().get(0);
		if (candidato.getContent() == null || candidato.getContent().getParts() == null) {
			return "";
		}
		StringBuilder texto = new StringBuilder();
		for (GeminiParteDTO parte : candidato.getContent().getParts()) {
			if (parte.getText() != null) {
				texto.append(parte.getText());
			}
		}
		return texto.toString().trim();
	}

	// Convierte el fallo de Gemini en un mensaje que el usuario entienda
	private NegocioException traducirError(ExternalApiException ex) {
		int estado = ex.getStatusCode();
		String detalle = ex.getMessage() == null ? "" : ex.getMessage();

		if (estado == 429) {
			return new NegocioException("El asistente está recibiendo muchas preguntas. Intenta de nuevo en un minuto.",
					429, "ASISTENTE_OCUPADO");
		}
		if (estado == 401 || estado == 403 || detalle.contains("API_KEY") || detalle.contains("API key")) {
			return new NegocioException("La clave de Gemini no es válida (revisa GEMINI_API_KEY en el .env del back).",
					503, "ASISTENTE_CLAVE_INVALIDA");
		}
		if (estado == 404) {
			return new NegocioException("El modelo de Gemini configurado no existe (revisa external.gemini.modelo).",
					503, "ASISTENTE_MODELO_INVALIDO");
		}
		return new NegocioException("El asistente no está disponible en este momento. Intenta más tarde.", 502,
				"ASISTENTE_NO_DISPONIBLE");
	}

}
