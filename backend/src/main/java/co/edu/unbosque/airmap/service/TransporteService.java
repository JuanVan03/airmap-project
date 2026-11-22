package co.edu.unbosque.airmap.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import com.google.transit.realtime.GtfsRealtime.FeedMessage;
import com.google.transit.realtime.GtfsRealtime.TripUpdate;
import com.google.transit.realtime.GtfsRealtime.TripUpdate.StopTimeUpdate;
import com.google.transit.realtime.GtfsRealtime.VehiclePosition;

import co.edu.unbosque.airmap.dto.BusDTO;
import co.edu.unbosque.airmap.dto.LlegadaDTO;
import co.edu.unbosque.airmap.dto.ParaderoDTO;
import co.edu.unbosque.airmap.dto.RutaBusDTO;
import co.edu.unbosque.airmap.dto.RutaDTO;
import co.edu.unbosque.airmap.dto.TiempoRealDTO;
import co.edu.unbosque.airmap.dto.TransporteDTO;
import co.edu.unbosque.airmap.exception.ExternalApiException;
import co.edu.unbosque.airmap.exception.NegocioException;

@Service
public class TransporteService {

	// Se busca un paradero con ruta al aeropuerto hasta 1,5 km del usuario
	private static final double RADIO_BUSQUEDA = 1500;
	// Solo se muestran los buses a menos de 5 km del paradero
	private static final double RADIO_BUSES = 5000;
	// Velocidad promedio de un bus en Bogota (km/h), para estimar cuando llega
	private static final double VELOCIDAD_BUS = 18;

	@Value("${external.transmilenio.viajes}")
	private String urlViajes;

	@Value("${external.transmilenio.posiciones}")
	private String urlPosiciones;

	@Autowired
	private GtfsEstaticoService gtfs;

	@Autowired
	private RutaService rutaServ;

	// Ultima copia de los datos en vivo; se vuelven a pedir cada 30 segundos
	private FeedMessage viajes;
	private FeedMessage posiciones;
	private long ultimaConsulta = 0;

	public TransporteService() {
	}

	public TransporteDTO planear(double lat, double lon) {
		revisarDatos();
		ParaderoDTO paradero = gtfs.paraderoMasCercano(lat, lon, RADIO_BUSQUEDA);
		if (paradero == null) {
			throw new NegocioException("No hay un paradero con ruta directa al aeropuerto a menos de 1,5 km de ti.",
					404, "SIN_PARADERO_CERCANO");
		}

		// Caminata hasta el paradero con openrouteservice
		RutaDTO caminata = null;
		try {
			caminata = rutaServ.calcular(lat, lon, paradero.getLatitud(), paradero.getLongitud(), "pie");
		} catch (ExternalApiException e) {
			// Sin la caminata igual se muestran el paradero y los buses
		}

		List<RutaBusDTO> rutas = new ArrayList<>();
		for (String idRuta : gtfs.rutasDesde(paradero.getId())) {
			ParaderoDTO bajada = gtfs.paradero(gtfs.bajada(idRuta));
			rutas.add(new RutaBusDTO(gtfs.codigoRuta(idRuta), gtfs.nombreRuta(idRuta),
					bajada == null ? "Aeropuerto El Dorado" : bajada.getNombre()));
		}

		TiempoRealDTO vivo = tiempoReal(paradero.getId());
		return new TransporteDTO(paradero, caminata, rutas, vivo.getLlegadas(), vivo.getBuses());
	}

	// Proximos buses y su posicion; el front lo pide cada 30 segundos
	public TiempoRealDTO tiempoReal(String idParadero) {
		revisarDatos();
		ParaderoDTO paradero = gtfs.paradero(idParadero);
		if (paradero == null) {
			throw new NegocioException("No existe ese paradero.", 404, "PARADERO_NO_ENCONTRADO");
		}
		Set<String> rutas = gtfs.rutasDesde(idParadero);
		actualizarDatosEnVivo();

		List<BusDTO> buses = busesCerca(paradero, rutas);
		List<LlegadaDTO> llegadas = llegadasPrevistas(idParadero, rutas);
		// Si TransMilenio no envia la hora de llegada, se calcula con la distancia de los buses
		if (llegadas.isEmpty()) {
			llegadas = llegadasEstimadas(buses);
		}

		// Los que llegan primero arriba, maximo 6
		llegadas.sort(Comparator.comparingInt(LlegadaDTO::getMinutos));
		if (llegadas.size() > 6) {
			llegadas = new ArrayList<>(llegadas.subList(0, 6));
		}
		return new TiempoRealDTO(llegadas, buses);
	}

	private void revisarDatos() {
		if (!gtfs.isListo()) {
			throw new NegocioException("Los datos de TransMilenio se están cargando, intenta de nuevo en un minuto.",
					503, "TRANSPORTE_CARGANDO");
		}
	}

	private synchronized void actualizarDatosEnVivo() {
		if (System.currentTimeMillis() - ultimaConsulta < 30000) {
			return;
		}
		viajes = leerFeed(urlViajes);
		posiciones = leerFeed(urlPosiciones);
		if (viajes == null && posiciones == null) {
			throw new ExternalApiException("No se pudo consultar TransMilenio en este momento.", 502);
		}
		ultimaConsulta = System.currentTimeMillis();
	}

	// Descarga un .pb y lo convierte con la libreria de GTFS Realtime. Si falla devuelve null
	private FeedMessage leerFeed(String url) {
		try {
			return FeedMessage.parseFrom(ExternalHTTPRequestHandler.doGetBytes(url));
		} catch (ExternalApiException | InvalidProtocolBufferException e) {
			return null;
		}
	}

	// Hora de llegada que envia TransMilenio para este paradero (proximos 60 minutos)
	private List<LlegadaDTO> llegadasPrevistas(String idParadero, Set<String> rutas) {
		List<LlegadaDTO> llegadas = new ArrayList<>();
		if (viajes == null) {
			return llegadas;
		}
		long ahora = System.currentTimeMillis() / 1000;
		for (FeedEntity entidad : viajes.getEntityList()) {
			if (!entidad.hasTripUpdate()) {
				continue;
			}
			TripUpdate viaje = entidad.getTripUpdate();
			String idRuta = viaje.getTrip().getRouteId();
			if (!rutas.contains(idRuta)) {
				continue;
			}
			for (StopTimeUpdate parada : viaje.getStopTimeUpdateList()) {
				if (!parada.getStopId().equals(idParadero)) {
					continue;
				}
				long hora = parada.getArrival().getTime();
				if (hora == 0) {
					hora = parada.getDeparture().getTime();
				}
				long minutos = (hora - ahora) / 60;
				if (hora > 0 && minutos >= 0 && minutos <= 60) {
					llegadas.add(new LlegadaDTO(gtfs.codigoRuta(idRuta), (int) minutos, false));
				}
			}
		}
		return llegadas;
	}

	// Buses de esas rutas que van cerca del paradero
	private List<BusDTO> busesCerca(ParaderoDTO paradero, Set<String> rutas) {
		List<BusDTO> buses = new ArrayList<>();
		if (posiciones == null) {
			return buses;
		}
		for (FeedEntity entidad : posiciones.getEntityList()) {
			if (!entidad.hasVehicle()) {
				continue;
			}
			VehiclePosition bus = entidad.getVehicle();
			String idRuta = bus.getTrip().getRouteId();
			if (!rutas.contains(idRuta)) {
				continue;
			}
			double lat = bus.getPosition().getLatitude();
			double lon = bus.getPosition().getLongitude();
			double distancia = GtfsEstaticoService.distanciaMetros(lat, lon, paradero.getLatitud(),
					paradero.getLongitud());
			if (distancia <= RADIO_BUSES) {
				buses.add(new BusDTO(gtfs.codigoRuta(idRuta), lat, lon, (int) Math.round(distancia)));
			}
		}
		return buses;
	}

	// Tiempo aproximado = distancia / velocidad promedio
	// No se sabe si el bus va hacia el paradero o ya paso, por eso se marca como aproximado
	private List<LlegadaDTO> llegadasEstimadas(List<BusDTO> buses) {
		List<LlegadaDTO> llegadas = new ArrayList<>();
		for (BusDTO bus : buses) {
			int minutos = (int) Math.round(bus.getDistanciaM() / 1000.0 / VELOCIDAD_BUS * 60);
			llegadas.add(new LlegadaDTO(bus.getRuta(), minutos, true));
		}
		return llegadas;
	}

}
