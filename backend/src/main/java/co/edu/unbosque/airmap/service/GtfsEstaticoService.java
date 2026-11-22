package co.edu.unbosque.airmap.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import co.edu.unbosque.airmap.dto.ParaderoDTO;
import jakarta.annotation.PostConstruct;

@Service
public class GtfsEstaticoService {

	private static final Logger log = LoggerFactory.getLogger(GtfsEstaticoService.class);


	private static final double AEROPUERTO_LAT = 4.7016;
	private static final double AEROPUERTO_LON = -74.1469;
	private static final double RADIO_AEROPUERTO = 700;

	@Value("${external.transmilenio.gtfs}")
	private String urlGtfs;

		private Map<String, ParaderoDTO> paraderos = new HashMap<>();
	private Map<String, String[]> rutas = new HashMap<>();
	private Set<String> paraderosAeropuerto = new HashSet<>();
	private Map<String, Set<String>> rutasAlAeropuerto = new HashMap<>();
	private Map<String, String> bajadaPorRuta = new HashMap<>();

	private volatile boolean listo = false;

	public GtfsEstaticoService() {
	}

	@PostConstruct
	public void iniciar() {
		new Thread(this::cargar).start();
	}

	private void cargar() {
		try {
			Path archivo = Path.of("gtfs", "GTFS.zip");
			descargarSiHaceFalta(archivo);
			try (ZipFile zip = new ZipFile(archivo.toFile())) {
				leerParaderos(zip);
				leerRutas(zip);
				Map<String, String> rutaPorViaje = leerViajes(zip);
				leerRecorridos(zip, rutaPorViaje);
			}
			listo = true;
			log.info("TransMilenio listo: {} paraderos con ruta directa al aeropuerto", rutasAlAeropuerto.size());
		} catch (Exception e) {
			log.error("No se pudieron cargar los datos de TransMilenio: {}", e.getMessage());
		}
	}

	private void descargarSiHaceFalta(Path archivo) throws IOException {
		if (Files.exists(archivo)) {
			Instant modificado = Files.getLastModifiedTime(archivo).toInstant();
			if (modificado.isAfter(Instant.now().minus(Duration.ofDays(7)))) {
				return;
			}
		}
		log.info("Descargando el GTFS de TransMilenio, puede tardar unos minutos...");
		Files.createDirectories(archivo.getParent());
		Path temporal = Path.of("gtfs", "GTFS.zip.tmp");
		ExternalHTTPRequestHandler.descargarArchivo(urlGtfs, temporal);
		Files.move(temporal, archivo, StandardCopyOption.REPLACE_EXISTING);
	}

	private void leerParaderos(ZipFile zip) throws IOException {
		try (BufferedReader lector = abrir(zip, "stops.txt")) {
			Map<String, Integer> col = columnas(lector.readLine());
			String linea;
			while ((linea = lector.readLine()) != null) {
				String[] c = separar(linea);
				String id = c[col.get("stop_id")];
				double lat = Double.parseDouble(c[col.get("stop_lat")]);
				double lon = Double.parseDouble(c[col.get("stop_lon")]);
				paraderos.put(id, new ParaderoDTO(id, c[col.get("stop_name")], lat, lon, 0));
				if (distanciaMetros(lat, lon, AEROPUERTO_LAT, AEROPUERTO_LON) <= RADIO_AEROPUERTO) {
					paraderosAeropuerto.add(id);
				}
			}
		}
	}

	private void leerRutas(ZipFile zip) throws IOException {
		try (BufferedReader lector = abrir(zip, "routes.txt")) {
			Map<String, Integer> col = columnas(lector.readLine());
			String linea;
			while ((linea = lector.readLine()) != null) {
				String[] c = separar(linea);
				rutas.put(c[col.get("route_id")],
						new String[] { c[col.get("route_short_name")], c[col.get("route_long_name")] });
			}
		}
	}

	private Map<String, String> leerViajes(ZipFile zip) throws IOException {
		Map<String, String> rutaPorViaje = new HashMap<>();
		try (BufferedReader lector = abrir(zip, "trips.txt")) {
			Map<String, Integer> col = columnas(lector.readLine());
			String linea;
			while ((linea = lector.readLine()) != null) {
				String[] c = linea.replace("\"", "").split(",", -1);
				rutaPorViaje.put(c[col.get("trip_id")], c[col.get("route_id")]);
			}
		}
		return rutaPorViaje;
	}

	private void leerRecorridos(ZipFile zip, Map<String, String> rutaPorViaje) throws IOException {
		// 1. En que parada (orden) cada viaje llega al aeropuerto
		Map<String, Integer> ordenAeropuerto = new HashMap<>();
		Map<String, String> bajadaPorViaje = new HashMap<>();
		try (BufferedReader lector = abrir(zip, "stop_times.txt")) {
			Map<String, Integer> col = columnas(lector.readLine());
			String linea;
			while ((linea = lector.readLine()) != null) {
				String[] c = linea.replace("\"", "").split(",", -1);
				String paradero = c[col.get("stop_id")];
				if (paraderosAeropuerto.contains(paradero)) {
					String viaje = c[col.get("trip_id")];
					int orden = Integer.parseInt(c[col.get("stop_sequence")]);
					if (orden > ordenAeropuerto.getOrDefault(viaje, -1)) {
						ordenAeropuerto.put(viaje, orden);
						bajadaPorViaje.put(viaje, paradero);
					}
				}
			}
		}

		try (BufferedReader lector = abrir(zip, "stop_times.txt")) {
			Map<String, Integer> col = columnas(lector.readLine());
			String linea;
			while ((linea = lector.readLine()) != null) {
				String[] c = linea.replace("\"", "").split(",", -1);
				String viaje = c[col.get("trip_id")];
				Integer orden = ordenAeropuerto.get(viaje);
				if (orden == null || Integer.parseInt(c[col.get("stop_sequence")]) >= orden) {
					continue;
				}
				String ruta = rutaPorViaje.get(viaje);
				if (ruta == null) {
					continue;
				}
				rutasAlAeropuerto.computeIfAbsent(c[col.get("stop_id")], k -> new HashSet<>()).add(ruta);
				bajadaPorRuta.putIfAbsent(ruta, bajadaPorViaje.get(viaje));
			}
		}
	}

	private BufferedReader abrir(ZipFile zip, String nombre) throws IOException {
		if (zip.getEntry(nombre) == null) {
			throw new IOException("El GTFS no tiene " + nombre);
		}
		return new BufferedReader(new InputStreamReader(zip.getInputStream(zip.getEntry(nombre)), StandardCharsets.UTF_8));
	}

	private Map<String, Integer> columnas(String encabezado) {
		Map<String, Integer> col = new HashMap<>();
		String[] nombres = separar(encabezado.replace("﻿", ""));
		for (int i = 0; i < nombres.length; i++) {
			col.put(nombres[i].trim(), i);
		}
		return col;
	}

	private String[] separar(String linea) {
		List<String> campos = new ArrayList<>();
		StringBuilder actual = new StringBuilder();
		boolean enComillas = false;
		for (char letra : linea.toCharArray()) {
			if (letra == '"') {
				enComillas = !enComillas;
			} else if (letra == ',' && !enComillas) {
				campos.add(actual.toString());
				actual.setLength(0);
			} else {
				actual.append(letra);
			}
		}
		campos.add(actual.toString());
		return campos.toArray(new String[0]);
	}

	public ParaderoDTO paraderoMasCercano(double lat, double lon, double radioMetros) {
		ParaderoDTO mejor = null;
		double menor = radioMetros;
		for (String id : rutasAlAeropuerto.keySet()) {
			ParaderoDTO p = paraderos.get(id);
			if (p == null) {
				continue;
			}
			double distancia = distanciaMetros(lat, lon, p.getLatitud(), p.getLongitud());
			if (distancia <= menor) {
				menor = distancia;
				mejor = p;
			}
		}
		if (mejor == null) {
			return null;
		}
		// Copia con la distancia, para no cambiar el paradero guardado
		return new ParaderoDTO(mejor.getId(), mejor.getNombre(), mejor.getLatitud(), mejor.getLongitud(),
				(int) Math.round(menor));
	}

	public boolean isListo() {
		return listo;
	}

	public ParaderoDTO paradero(String id) {
		return paraderos.get(id);
	}

	public Set<String> rutasDesde(String idParadero) {
		return rutasAlAeropuerto.getOrDefault(idParadero, Set.of());
	}

	public String codigoRuta(String idRuta) {
		String[] ruta = rutas.get(idRuta);
		return ruta == null ? idRuta : ruta[0];
	}

	public String nombreRuta(String idRuta) {
		String[] ruta = rutas.get(idRuta);
		return ruta == null ? "" : ruta[1];
	}

	public String bajada(String idRuta) {
		return bajadaPorRuta.get(idRuta);
	}

	// Distancia en metros entre dos puntos (formula de Haversine)
	public static double distanciaMetros(double lat1, double lon1, double lat2, double lon2) {
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
		return 6371000 * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	}

}
