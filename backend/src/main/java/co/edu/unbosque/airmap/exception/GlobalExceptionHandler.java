package co.edu.unbosque.airmap.exception;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ExternalApiException.class)
	public ResponseEntity<Map<String, Object>> handleExternalApi(ExternalApiException ex) {
		// Si no existe el vuelo respondemos 404, cualquier otro fallo de la API externa es un 502
		boolean noEncontrado = ex.getStatusCode() == 404;
		HttpStatus status = noEncontrado ? HttpStatus.NOT_FOUND : HttpStatus.BAD_GATEWAY;
		return crearError(status, noEncontrado ? "VUELO_NO_ENCONTRADO" : "API_EXTERNA_NO_DISPONIBLE", ex.getMessage(),
				List.of());
	}

	// Errores de registro e ingreso (correo repetido, contrasena incorrecta, Google invalido)
	@ExceptionHandler(AuthException.class)
	public ResponseEntity<Map<String, Object>> handleAuth(AuthException ex) {
		return crearError(HttpStatus.valueOf(ex.getStatusCode()), ex.getCodigo(), ex.getMessage(), List.of());
	}

	// Datos del formulario que no cumplen las validaciones (@NotBlank, @Email, etc.)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidacion(MethodArgumentNotValidException ex) {
		List<Map<String, String>> errores = new ArrayList<>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			Map<String, String> detalle = new LinkedHashMap<>();
			detalle.put("campo", error.getField());
			detalle.put("mensaje", error.getDefaultMessage());
			errores.add(detalle);
		});
		return crearError(HttpStatus.BAD_REQUEST, "DATOS_INVALIDOS", "Revisa los datos del formulario.", errores);
	}

	// Mismo formato de error que acordamos con el front
	private ResponseEntity<Map<String, Object>> crearError(HttpStatus status, String codigo, String mensaje,
			List<?> errores) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("estado", status.value());
		body.put("codigo", codigo);
		body.put("mensaje", mensaje);
		body.put("fecha", Instant.now().toString());
		body.put("errores", errores);
		return new ResponseEntity<>(body, status);
	}

}
