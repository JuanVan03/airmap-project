package co.edu.unbosque.airmap.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegistroDTO {

	@NotBlank(message = "Ingresa tu nombre.")
	@Size(max = 100, message = "El nombre es muy largo.")
	private String nombre;

	@NotBlank(message = "Ingresa tu correo.")
	@Email(message = "Ingresa un correo valido.")
	private String correo;

	// Minimo 8 caracteres, una mayuscula y un numero
	@NotBlank(message = "La contrasena es obligatoria.")
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$", message = "Debe tener mínimo 8 caracteres, una mayuscula y un numero.")
	private String contrasena;

	public RegistroDTO() {
	}

	public RegistroDTO(String nombre, String correo, String contrasena) {
		super();
		this.nombre = nombre;
		this.correo = correo;
		this.contrasena = contrasena;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

}
