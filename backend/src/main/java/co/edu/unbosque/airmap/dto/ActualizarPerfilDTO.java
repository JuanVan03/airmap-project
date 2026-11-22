package co.edu.unbosque.airmap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Datos que el usuario puede cambiar de su perfil. El correo no se puede cambiar.
 *
 * @author Juan David Vanegas Peña
 * @version 1.0
 */
public class ActualizarPerfilDTO {

	@NotBlank(message = "Ingresa tu nombre.")
	@Size(max = 100, message = "El nombre es muy largo.")
	private String nombre;

	/**
	 * Constructor vacío, lo usa la conversión de JSON.
	 */
	public ActualizarPerfilDTO() {
	}

	/**
	 * Crea el objeto con todos sus datos.
	 *
	 * @param nombre nombre nuevo del usuario
	 */
	public ActualizarPerfilDTO(String nombre) {
		super();
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
