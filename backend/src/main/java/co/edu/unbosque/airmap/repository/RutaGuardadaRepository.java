package co.edu.unbosque.airmap.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.airmap.entity.RutaGuardada;

/**
 * Repositorio JPA de las rutas terrestres guardadas.
 *
 * @author Juan
 * @version 1.0
 */
public interface RutaGuardadaRepository extends JpaRepository<RutaGuardada, Long> {

	/**
	 * Rutas de un usuario, de la más nueva a la más vieja.
	 *
	 * @param usuarioId id del usuario
	 * @return rutas guardadas
	 */
	List<RutaGuardada> findByUsuarioIdOrderByFechaGuardadoDesc(Long usuarioId);

	/**
	 * Busca una ruta por id y por dueño, para que nadie toque rutas de otro usuario.
	 *
	 * @param id id de la ruta
	 * @param usuarioId id del usuario
	 * @return la ruta si existe y es del usuario
	 */
	Optional<RutaGuardada> findByIdAndUsuarioId(Long id, Long usuarioId);

	/**
	 * Cuenta las rutas guardadas de un usuario.
	 *
	 * @param usuarioId id del usuario
	 * @return cantidad de rutas
	 */
	long countByUsuarioId(Long usuarioId);

}
