package co.edu.unbosque.airmap.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import co.edu.unbosque.airmap.exception.ExternalApiException;

public class ExternalHTTPRequestHandler {

	private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2)
			.connectTimeout(Duration.ofSeconds(10)).followRedirects(HttpClient.Redirect.NORMAL).build();

	private static final Gson GSON = new GsonBuilder().create();

	public static <T> T doGetAndConvertToDto(String url, Class<T> clazz) {
		HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url)).header("Accept", "application/json")
				.timeout(Duration.ofSeconds(30)).build();
		return convert(getBody(request), clazz, url);
	}

	// POST con cuerpo JSON y la clave de la API en el encabezado Authorization
	public static <T> T doPostAndConvertToDto(String url, String apiKey, Object body, Class<T> clazz) {
		return doPostAndConvertToDto(url, "Authorization", apiKey, body, clazz);
	}

	// Igual, pero el nombre del encabezado de la clave cambia segun la API (Gemini usa x-goog-api-key)
	public static <T> T doPostAndConvertToDto(String url, String headerName, String apiKey, Object body,
			Class<T> clazz) {
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
				.header("Content-Type", "application/json").header("Accept", "application/json, application/geo+json")
				.header(headerName, apiKey).timeout(Duration.ofSeconds(30))
				.POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(body))).build();
		return convert(getBody(request), clazz, url);
	}

	private static String getBody(HttpRequest request) {
		HttpResponse<String> response = send(request);
		if (response.statusCode() >= 400) {
			throw new ExternalApiException("La API respondio " + response.statusCode() + ": " + response.body(),
					response.statusCode());
		}
		return response.body();
	}

	private static HttpResponse<String> send(HttpRequest request) {
		try {
			return HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			throw new ExternalApiException("No se pudo conectar con " + request.uri() + ": " + e.getMessage(), 503, e);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new ExternalApiException("Peticion interrumpida: " + request.uri(), 503, e);
		}
	}

	private static <T> T convert(String json, Class<T> clazz, String url) {
		try {
			return GSON.fromJson(json, clazz);
		} catch (JsonSyntaxException e) {
			throw new ExternalApiException("El JSON de " + url + " no coincide con " + clazz.getSimpleName(), 502, e);
		}
	}

}
