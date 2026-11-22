package co.edu.unbosque.airmap.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.Duration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import co.edu.unbosque.airmap.exception.ExternalApiException;

/**
 * Clase de apoyo para hacer peticiones HTTP a las APIs externas y convertir la respuesta
 * JSON en DTO con Gson.
 *
 * @author Juan
 * @version 1.0
 */
public class ExternalHTTPRequestHandler {

	private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2)
			.connectTimeout(Duration.ofSeconds(10)).followRedirects(HttpClient.Redirect.NORMAL).build();

	private static final Gson GSON = new GsonBuilder().create();

	/**
	 * Hace un GET y convierte el JSON de la respuesta en un DTO.
	 *
	 * @param <T> tipo del DTO
	 * @param url dirección completa de la API
	 * @param clazz clase del DTO
	 * @return el DTO con los datos de la respuesta
	 * @throws ExternalApiException si la API falla o el JSON no coincide
	 */
	public static <T> T doGetAndConvertToDto(String url, Class<T> clazz) {
		HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url)).header("Accept", "application/json")
				.timeout(Duration.ofSeconds(30)).build();
		return convert(getBody(request), clazz, url);
	}

	/**
	 * Hace un POST con cuerpo JSON. La clave de la API va en el encabezado Authorization.
	 *
	 * @param <T> tipo del DTO
	 * @param url dirección completa de la API
	 * @param apiKey clave de la API
	 * @param body objeto que se envía como JSON
	 * @param clazz clase del DTO
	 * @return el DTO con los datos de la respuesta
	 * @throws ExternalApiException si la API falla o el JSON no coincide
	 */
	public static <T> T doPostAndConvertToDto(String url, String apiKey, Object body, Class<T> clazz) {
		return doPostAndConvertToDto(url, "Authorization", apiKey, body, clazz);
	}

	/**
	 * Hace un POST con cuerpo JSON. Sirve para las APIs que piden la clave en otro encabezado
	 * (Gemini usa x-goog-api-key).
	 *
	 * @param <T> tipo del DTO
	 * @param url dirección completa de la API
	 * @param headerName nombre del encabezado de la clave
	 * @param apiKey clave de la API
	 * @param body objeto que se envía como JSON
	 * @param clazz clase del DTO
	 * @return el DTO con los datos de la respuesta
	 * @throws ExternalApiException si la API falla o el JSON no coincide
	 */
	public static <T> T doPostAndConvertToDto(String url, String headerName, String apiKey, Object body,
			Class<T> clazz) {
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
				.header("Content-Type", "application/json").header("Accept", "application/json, application/geo+json")
				.header(headerName, apiKey).timeout(Duration.ofSeconds(30))
				.POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(body))).build();
		return convert(getBody(request), clazz, url);
	}

	/**
	 * Hace un GET y devuelve la respuesta en bytes. Se usa para los datos en vivo de TransMilenio
	 * (.pb), que no son JSON.
	 *
	 * @param url dirección del archivo
	 * @return contenido de la respuesta
	 * @throws ExternalApiException si la petición falla
	 */
	public static byte[] doGetBytes(String url) {
		HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url)).timeout(Duration.ofSeconds(30))
				.build();
		HttpResponse<byte[]> response = enviar(request, HttpResponse.BodyHandlers.ofByteArray());
		if (response.statusCode() >= 400) {
			throw new ExternalApiException("La API respondio " + response.statusCode(), response.statusCode());
		}
		return response.body();
	}

	/**
	 * Descarga un archivo directo al disco. Se usa para el GTFS de TransMilenio, que pesa más de 100 MB.
	 *
	 * @param url dirección del archivo
	 * @param destino ruta donde se guarda
	 * @throws ExternalApiException si la descarga falla
	 */
	public static void descargarArchivo(String url, Path destino) {
		HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url)).timeout(Duration.ofMinutes(15))
				.build();
		HttpResponse<Path> response = enviar(request, HttpResponse.BodyHandlers.ofFile(destino));
		if (response.statusCode() >= 400) {
			throw new ExternalApiException("La descarga respondio " + response.statusCode(), response.statusCode());
		}
	}

	private static <T> HttpResponse<T> enviar(HttpRequest request, HttpResponse.BodyHandler<T> manejador) {
		try {
			return HTTP_CLIENT.send(request, manejador);
		} catch (IOException e) {
			throw new ExternalApiException("No se pudo conectar con " + request.uri() + ": " + e.getMessage(), 503, e);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new ExternalApiException("Peticion interrumpida: " + request.uri(), 503, e);
		}
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
