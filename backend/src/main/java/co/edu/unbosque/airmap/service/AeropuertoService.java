package co.edu.unbosque.airmap.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import co.edu.unbosque.airmap.dto.AeropuertoDTO;

@Service
public class AeropuertoService {

	// Aeropuertos con codigo IATA, se leen del archivo aeropuertos.csv al iniciar
	private List<AeropuertoDTO> aeropuertos = new ArrayList<>();

	public AeropuertoService() {
		try (BufferedReader lector = new BufferedReader(
				new InputStreamReader(getClass().getResourceAsStream("/aeropuertos.csv"), StandardCharsets.UTF_8))) {
			String linea;
			while ((linea = lector.readLine()) != null) {
				// Formato: codigo|nombre|ciudad|pais|latitud|longitud
				String[] campos = linea.split("\\|", -1);
				if (campos.length != 6) {
					continue;
				}
				// El pais viene como codigo (CO) y lo pasamos a nombre (Colombia)
				String pais = Locale.of("", campos[3]).getDisplayCountry(Locale.of("es"));
				aeropuertos.add(new AeropuertoDTO(campos[0], campos[1], campos[2], pais,
						Double.parseDouble(campos[4]), Double.parseDouble(campos[5])));
			}
		} catch (IOException e) {
			throw new IllegalStateException("No se pudo leer aeropuertos.csv", e);
		}
	}

	public List<AeropuertoDTO> buscar(String texto) {
		List<AeropuertoDTO> encontrados = new ArrayList<>();
		String buscado = limpiar(texto);
		if (buscado.isEmpty()) {
			return encontrados;
		}

		// Primero el aeropuerto cuyo codigo es exactamente lo escrito (BOG)
		for (AeropuertoDTO a : aeropuertos) {
			if (a.getCodigo().equalsIgnoreCase(buscado)) {
				encontrados.add(a);
			}
		}

		// Despues los que tienen el texto en el nombre o en la ciudad
		for (AeropuertoDTO a : aeropuertos) {
			// Con 8 resultados es suficiente
			if (encontrados.size() == 8) {
				break;
			}
			if (limpiar(a.getNombre() + " " + a.getCiudad()).contains(buscado) && !encontrados.contains(a)) {
				encontrados.add(a);
			}
		}
		return encontrados;
	}

	// Quita tildes y mayusculas para que "Bogota" y "bogotá" sean lo mismo
	private String limpiar(String texto) {
		return Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase().trim();
	}

}
