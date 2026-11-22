package co.edu.unbosque.airmap.configuration;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

/**
 * Configura cómo se firman y se leen los tokens JWT. Se usa la misma clave secreta
 * (JWT_SECRET) para firmar y verificar con el algoritmo HS256.
 *
 * @author Juan
 * @version 1.0
 */
@Configuration
public class JwtConfig {

	@Value("${jwt.secret}")
	private String secreto;

	// La misma clave firma y verifica los tokens (HS256)
	private SecretKey clave() {
		return new SecretKeySpec(secreto.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
	}

	/**
	 * Crea el objeto que firma los tokens JWT.
	 *
	 * @return encoder de JWT con la clave secreta
	 */
	@Bean
	JwtEncoder jwtEncoder() {
		return new NimbusJwtEncoder(new ImmutableSecret<>(clave()));
	}

	/**
	 * Crea el objeto que valida los tokens JWT que llegan en cada petición.
	 *
	 * @return decoder de JWT con la clave secreta
	 */
	@Bean
	JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withSecretKey(clave()).macAlgorithm(MacAlgorithm.HS256).build();
	}

}
