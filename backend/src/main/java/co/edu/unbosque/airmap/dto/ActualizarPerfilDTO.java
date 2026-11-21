package co.edu.unbosque.airmap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Datos que el usuario puede cambiar de su perfil (el correo no se cambia)
public class ActualizarPerfilDTO {

	@NotBlank(message = "Ingresa tu nombre.")
	@Size(max = 100, message = "El nombre es muy largo.")
	private String nombre;

	public ActualizarPerfilDTO() {
	}

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
