package co.edu.unbosque.airmap.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	// Las contrasenas se guardan cifradas con BCrypt
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).cors(Customizer.withDefaults())
				.authorizeHttpRequests(auth -> auth
						// El mapa de vuelos, los aeropuertos y las rutas son publicos
						.requestMatchers("/api/vuelos", "/api/vuelos/**", "/api/aeropuertos", "/api/rutas").permitAll()
						// Registro e ingreso tambien son publicos
						.requestMatchers("/api/auth/registro", "/api/auth/login", "/api/auth/google").permitAll()
						// Swagger para probar los endpoints
						.requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
						// Pagina de error de Spring: sin esto un 404 o un 500 se ve como 401
						.requestMatchers("/error").permitAll()
						.anyRequest().authenticated())
				// Lee el token JWT del encabezado Authorization: Bearer <token>
				.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
		return http.build();
	}

}
