package co.edu.unbosque.airmap.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import co.edu.unbosque.airmap.entity.Usuario;
import co.edu.unbosque.airmap.repository.UsuarioRepository;
import co.edu.unbosque.airmap.service.AuditoriaService;

/**
 * Crea o actualiza el único administrador de AirMap cada vez que arranca el back.
 * Los datos salen del .env (ADMIN_CORREO y ADMIN_CONTRASENA), por eso el administrador
 * no se puede registrar desde la página.
 *
 * @author SakiYoshida
 * @version 1.0
 */
@Component
public class AdminInicializador implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(AdminInicializador.class);

	@Value("${admin.correo:}")
	private String correo;

	@Value("${admin.contrasena:}")
	private String contrasena;

	@Autowired
	private UsuarioRepository usuarioRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuditoriaService auditoriaServ;

	/**
	 * Constructor vacío. Spring crea la instancia e inyecta las dependencias.
	 */
	public AdminInicializador() {
	}

	/**
	 * Crea el administrador si no existe, o le actualiza la contraseña si cambió en el .env.
	 * Si el correo ya es de un usuario normal no hace nada y lo deja en el log.
	 *
	 * @param args argumentos con los que arrancó la aplicación
	 */
	@Override
	public void run(ApplicationArguments args) {
		if (correo == null || correo.isBlank() || contrasena == null || contrasena.isBlank()) {
			log.warn("No hay administrador: faltan ADMIN_CORREO y ADMIN_CONTRASENA en el .env del back.");
			return;
		}
		String correoAdmin = correo.trim().toLowerCase();

		Usuario admin = usuarioRepo.findByCorreo(correoAdmin).orElse(null);
		if (admin == null) {
			admin = new Usuario("Administrador", correoAdmin, passwordEncoder.encode(contrasena), "LOCAL");
			admin.setRol("ADMIN");
			admin.setVerificado(true);
			usuarioRepo.save(admin);
			auditoriaServ.registrar("ADMIN_CREADO", "sistema", AuditoriaService.EXITO,
					"Se creó la cuenta de administrador " + correoAdmin);
			return;
		}

		// Ese correo ya lo tenia una cuenta normal: no se convierte en administrador
		if (!"ADMIN".equals(admin.getRol())) {
			log.error("El correo {} ya es de un usuario normal. Usa otro ADMIN_CORREO en el .env.", correoAdmin);
			return;
		}

		// Si cambiaste ADMIN_CONTRASENA en el .env, se actualiza al reiniciar
		if (!passwordEncoder.matches(contrasena, admin.getContrasena())) {
			admin.setContrasena(passwordEncoder.encode(contrasena));
			usuarioRepo.save(admin);
			auditoriaServ.registrar("ADMIN_CLAVE_ACTUALIZADA", "sistema", AuditoriaService.EXITO,
					"La contraseña del administrador se actualizó desde el .env");
		}
	}

}
