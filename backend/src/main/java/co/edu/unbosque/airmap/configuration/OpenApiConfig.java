package co.edu.unbosque.airmap.configuration;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

/**
 * Configuración de Swagger. Agrega el botón "Authorize" para poder pegar el token JWT
 * y probar los endpoints que piden sesión.
 *
 * @author SakiYoshida
 * @version 1.0
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "AirMap API", version = "1.0", description = "Vuelos activos, aeropuertos, rutas terrestres y login con JWT y Google"))
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class OpenApiConfig {

}
