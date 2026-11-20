package co.edu.unbosque.airmap.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ExternalApiException.class)
	public ResponseEntity<Map<String, Object>> handleExternalApi(ExternalApiException ex) {
		// Si no existe el vuelo respondemos 404, cualquier otro fallo de la API externa es un 502
		boolean noEncontrado = ex.getStatusCode() == 404;
		HttpStatus status = noEncontrado ? HttpStatus.NOT_FOUND : HttpStatus.BAD_GATEWAY;

		// Mismo formato de error que acordamos con el front
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("estado", status.value());
		body.put("codigo", noEncontrado ? "VUELO_NO_ENCONTRADO" : "API_EXTERNA_NO_DISPONIBLE");
		body.put("mensaje", ex.getMessage());
		body.put("fecha", Instant.now().toString());
		body.put("errores", List.of());
		return new ResponseEntity<>(body, status);
	}

}
