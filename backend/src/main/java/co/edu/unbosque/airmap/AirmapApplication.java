package co.edu.unbosque.airmap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal del back de AirMap. Arranca la aplicación de Spring Boot.
 *
 * @author Juan
 * @version 1.0
 */
@SpringBootApplication
public class AirmapApplication {

	/**
	 * Punto de entrada del back.
	 *
	 * @param args argumentos de la línea de comandos
	 */
	public static void main(String[] args) {
		SpringApplication.run(AirmapApplication.class, args);
	}

}
