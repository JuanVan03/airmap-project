package co.edu.unbosque.airmap.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import co.edu.unbosque.airmap.dto.AuditoriaDTO;
import co.edu.unbosque.airmap.dto.PaginaAuditoriaDTO;
import co.edu.unbosque.airmap.entity.Auditoria;
import co.edu.unbosque.airmap.repository.AuditoriaRepository;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Servicio de auditoría. Guarda cada acción que pasa en AirMap (ingresos, cambios, consultas)
 * y permite que el administrador la consulte con filtros.
 *
 * @author SakiYoshida
 * @version 1.0
 */
@Service
public class AuditoriaService {

	/** Resultado de una acción que salió bien. */
	public static final String EXITO = "EXITO";
	/** Resultado de una acción que falló. */
	public static final String FALLO = "FALLO";

	private static final Logger log = LoggerFactory.getLogger(AuditoriaService.class);

	@Autowired
	private AuditoriaRepository auditoriaRepo;

	/**
	 * Constructor vacío. Spring crea la instancia e inyecta las dependencias.
	 */
	public AuditoriaService() {
	}

	/**
	 * Registra una acción del usuario que tiene la sesión abierta ("anonimo" si no hay sesión).
	 *
	 * @param accion nombre de la acción
	 * @param resultado EXITO o FALLO
	 * @param detalle descripción de lo que pasó
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void registrar(String accion, String resultado, String detalle) {
		guardar(accion, usuarioActual(), resultado, detalle);
	}

	/**
	 * Registra una acción de un usuario concreto, por ejemplo al intentar ingresar cuando todavía
	 * no hay sesión. Va en su propia transacción para que quede guardada aunque la operación falle.
	 *
	 * @param accion nombre de la acción
	 * @param usuario correo del usuario
	 * @param resultado EXITO o FALLO
	 * @param detalle descripción de lo que pasó
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void registrar(String accion, String usuario, String resultado, String detalle) {
		guardar(accion, usuario, resultado, detalle);
	}

	// Una falla al auditar nunca debe romper lo que el usuario estaba haciendo
	private void guardar(String accion, String usuario, String resultado, String detalle) {
		try {
			HttpServletRequest peticion = peticionActual();
			String ip = peticion == null ? null : peticion.getRemoteAddr();
			String navegador = peticion == null ? null : peticion.getHeader("User-Agent");

			auditoriaRepo.save(new Auditoria(recortar(usuario == null ? "anonimo" : usuario, 150), accion, resultado,
					recortar(detalle, 500), recortar(ip, 64), recortar(navegador, 200)));
		} catch (RuntimeException ex) {
			log.error("No se pudo guardar la auditoria de {}: {}", accion, ex.getMessage());
		}
	}

	// ----- Consulta (solo la usa el administrador) -----

	/**
	 * Consulta la auditoría con filtros, del registro más nuevo al más viejo.
	 *
	 * @param pagina número de página (empieza en 0)
	 * @param tamano registros por página
	 * @param accion acción (vacío para todas)
	 * @param resultado EXITO o FALLO (vacío para ambos)
	 * @param usuario correo del usuario
	 * @param desde fecha inicial (puede ser null)
	 * @param hasta fecha final, incluye todo ese día (puede ser null)
	 * @return página de registros
	 */
	public PaginaAuditoriaDTO consultar(int pagina, int tamano, String accion, String resultado, String usuario,
			LocalDate desde, LocalDate hasta) {
		int tamanoSeguro = Math.min(Math.max(tamano, 1), 100);
		LocalDateTime inicio = desde == null ? LocalDateTime.of(2000, 1, 1, 0, 0) : desde.atStartOfDay();
		// "hasta" incluye todo ese dia
		LocalDateTime fin = hasta == null ? LocalDateTime.of(2999, 12, 31, 23, 59) : hasta.atTime(23, 59, 59);

		Page<Auditoria> resultados = auditoriaRepo.buscar(texto(accion), texto(resultado), texto(usuario), inicio, fin,
				PageRequest.of(Math.max(pagina, 0), tamanoSeguro));

		List<AuditoriaDTO> contenido = resultados.getContent().stream().map(this::aDTO).toList();
		return new PaginaAuditoriaDTO(contenido, resultados.getNumber(), resultados.getSize(),
				resultados.getTotalElements(), resultados.getTotalPages());
	}

	/**
	 * Lista las acciones que ya hay en la auditoría.
	 *
	 * @return acciones sin repetir
	 */
	public List<String> acciones() {
		return auditoriaRepo.acciones();
	}

	// ----- Ayudas -----

	// Correo de quien hizo la peticion (sale del token JWT)
	private String usuarioActual() {
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		if (autenticacion == null || !autenticacion.isAuthenticated()
				|| "anonymousUser".equals(autenticacion.getName())) {
			return "anonimo";
		}
		return autenticacion.getName();
	}

	private HttpServletRequest peticionActual() {
		if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes atributos) {
			return atributos.getRequest();
		}
		return null;
	}

	private String texto(String valor) {
		return valor == null ? "" : valor.trim();
	}

	private String recortar(String valor, int maximo) {
		if (valor == null) {
			return null;
		}
		return valor.length() <= maximo ? valor : valor.substring(0, maximo - 3) + "...";
	}

	private AuditoriaDTO aDTO(Auditoria a) {
		return new AuditoriaDTO(a.getId(), a.getFecha().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), a.getUsuario(),
				a.getAccion(), a.getResultado(), a.getDetalle(), a.getIp(), a.getNavegador());
	}

}
