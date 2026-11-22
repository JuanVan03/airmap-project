package co.edu.unbosque.airmap.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de Spring Security: qué rutas son públicas, cuáles piden sesión y cuáles
 * son solo del administrador. El token JWT se lee del encabezado Authorization.
 *
 * @author Juan
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	/**
	 * Las contraseñas se guardan cifradas con BCrypt.
	 *
	 * @return codificador BCrypt
	 */
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Convierte la lista "roles" del token en permisos de Spring: ["ADMIN"] -> ROLE_ADMIN
	private JwtAuthenticationConverter convertidorDeRoles() {
		JwtGrantedAuthoritiesConverter permisos = new JwtGrantedAuthoritiesConverter();
		permisos.setAuthoritiesClaimName("roles");
		permisos.setAuthorityPrefix("ROLE_");
		JwtAuthenticationConverter convertidor = new JwtAuthenticationConverter();
		convertidor.setJwtGrantedAuthoritiesConverter(permisos);
		return convertidor;
	}

	/**
	 * Define qué rutas son públicas, cuáles piden sesión y cuáles son solo del administrador.
	 *
	 * @param http configuración de seguridad de Spring
	 * @return cadena de filtros de seguridad
	 * @throws Exception si falla la configuración
	 */
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).cors(Customizer.withDefaults())
				.authorizeHttpRequests(auth -> auth
						// El mapa de vuelos y los aeropuertos son publicos. Las rutas terrestres piden sesion.
						.requestMatchers("/api/vuelos", "/api/vuelos/**", "/api/aeropuertos").permitAll()
						// Registro e ingreso tambien son publicos
						.requestMatchers("/api/auth/registro", "/api/auth/login", "/api/auth/google", "/api/auth/verificar",
								"/api/auth/reenviar-verificacion", "/api/auth/recuperar", "/api/auth/restablecer")
						.permitAll()
						// Swagger para probar los endpoints
						.requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
						// Pagina de error de Spring: sin esto un 404 o un 500 se ve como 401
						.requestMatchers("/error").permitAll()
						// La auditoria y todo lo de administracion es solo para el rol ADMIN
						.requestMatchers("/api/admin/**").hasRole("ADMIN")
						.anyRequest().authenticated())
				// Lee el token JWT del encabezado Authorization: Bearer <token>
				.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(convertidorDeRoles())));
		return http.build();
	}

}
