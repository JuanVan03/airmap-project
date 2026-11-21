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

// Guarda cada accion de AirMap en la tabla auditoria y la consulta para el administrador
@Service
public class AuditoriaService {

	public static final String EXITO = "EXITO";
	public static final String FALLO = "FALLO";

	private static final Logger log = LoggerFactory.getLogger(AuditoriaService.class);

	@Autowired
	private AuditoriaRepository auditoriaRepo;

	public AuditoriaService() {
	}

	// Registra una accion de quien tiene la sesion abierta en esta peticion ("anonimo" si no hay)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void registrar(String accion, String resultado, String detalle) {
		guardar(accion, usuarioActual(), resultado, detalle);
	}

	// Registra una accion de un usuario concreto (por ejemplo, al intentar ingresar todavia no hay sesion).
	// Va en su propia transaccion: asi queda guardada aunque la operacion principal falle y se deshaga.
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
