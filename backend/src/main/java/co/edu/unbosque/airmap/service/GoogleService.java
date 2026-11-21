package co.edu.unbosque.airmap.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import co.edu.unbosque.airmap.exception.AuthException;

@Service
public class GoogleService {

	// Client ID de Google Cloud (GOOGLE_CLIENT_ID en el .env)
	@Value("${google.client-id:}")
	private String clientId;

	// Descarga las llaves publicas de Google para comprobar la firma del token
	private NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
			.build();

	public GoogleService() {
		decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(JwtValidators.createDefault(), validarDatos()));
	}

	// Revisa que el token sea de Google, de nuestra app y con el correo verificado
	private OAuth2TokenValidator<Jwt> validarDatos() {
		return jwt -> {
			// Google usa el emisor con o sin https://
			String emisorToken = jwt.getClaimAsString("iss");
			boolean emisor = "https://accounts.google.com".equals(emisorToken) || "accounts.google.com".equals(emisorToken);
			boolean app = jwt.getAudience() != null && jwt.getAudience().contains(clientId);
			boolean verificado = Boolean.TRUE.equals(jwt.getClaimAsBoolean("email_verified"));
			if (emisor && app && verificado) {
				return OAuth2TokenValidatorResult.success();
			}
			return OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "Token de Google no valido", null));
		};
	}

	// Devuelve los datos del token (email, name) o lanza AuthException si no sirve
	public Jwt verificar(String credencial) {
		if (clientId == null || clientId.isBlank()) {
			throw new AuthException("Falta el Client ID de Google (GOOGLE_CLIENT_ID en el .env del back).", 503,
					"GOOGLE_NO_CONFIGURADO");
		}
		try {
			return decoder.decode(credencial);
		} catch (JwtException e) {
			throw new AuthException("No se pudo validar tu cuenta de Google. Intenta de nuevo.", 401, "GOOGLE_INVALIDO");
		}
	}

}
