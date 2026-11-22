package co.edu.unbosque.airmap.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.airmap.entity.Preferencias;

/**
 * Repositorio JPA de la apariencia guardada de cada usuario.
 *
 * @author SakiYoshida
 * @version 1.0
 */
public interface PreferenciasRepository extends JpaRepository<Preferencias, Long> {

	/**
	 * Busca la apariencia guardada de un usuario.
	 *
	 * @param usuarioId id del usuario
	 * @return las preferencias si existen
	 */
	Optional<Preferencias> findByUsuarioId(Long usuarioId);

}
