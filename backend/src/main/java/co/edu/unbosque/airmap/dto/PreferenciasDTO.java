package co.edu.unbosque.airmap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

// Apariencia de la pagina. Solo se aceptan las opciones de la lista, no valores libres
public class PreferenciasDTO {

	@NotBlank(message = "Elige un tema.")
	@Pattern(regexp = "OSCURO|CLARO|ALTO_CONTRASTE", message = "Ese tema no existe.")
	private String tema;

	@NotBlank(message = "Elige un tipo de letra.")
	@Pattern(regexp = "INTER|POPPINS|MONTSERRAT|ATKINSON", message = "Ese tipo de letra no existe.")
	private String letra;

	@NotBlank(message = "Elige un tamaño de letra.")
	@Pattern(regexp = "PEQUENO|NORMAL|GRANDE|MUY_GRANDE", message = "Ese tamaño de letra no existe.")
	private String tamano;

	@NotBlank(message = "Elige un color.")
	@Pattern(regexp = "AZUL|VERDE|MORADO|NARANJA|ROSA|TURQUESA", message = "Ese color no existe.")
	private String color;

	@NotBlank(message = "Elige un ícono para el avión.")
	@Pattern(regexp = "AVION|FLECHA|PUNTO", message = "Ese ícono no existe.")
	private String avion;

	public PreferenciasDTO() {
	}

	public PreferenciasDTO(String tema, String letra, String tamano, String color, String avion) {
		super();
		this.tema = tema;
		this.letra = letra;
		this.tamano = tamano;
		this.color = color;
		this.avion = avion;
	}

	public String getTema() {
		return tema;
	}

	public void setTema(String tema) {
		this.tema = tema;
	}

	public String getLetra() {
		return letra;
	}

	public void setLetra(String letra) {
		this.letra = letra;
	}

	public String getTamano() {
		return tamano;
	}

	public void setTamano(String tamano) {
		this.tamano = tamano;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getAvion() {
		return avion;
	}

	public void setAvion(String avion) {
		this.avion = avion;
	}

	@Override
	public String toString() {
		return "PreferenciasDTO [" + "tema=" + tema + ", letra=" + letra + ", tamano=" + tamano + ", color=" + color
				+ ", avion=" + avion + "]";
	}

}
