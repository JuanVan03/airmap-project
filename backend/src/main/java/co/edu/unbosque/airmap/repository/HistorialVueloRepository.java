package co.edu.unbosque.airmap.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.airmap.entity.HistorialVuelo;

/**
 * Repositorio JPA del historial de vuelos abiertos por cada usuario.
 *
 * @author Juan
 * @version 1.0
 */
public interface HistorialVueloRepository extends JpaRepository<HistorialVuelo, Long> {

	/**
	 * Historial de un usuario, del más reciente al más viejo.
	 *
	 * @param usuarioId id del usuario
	 * @return vuelos del historial
	 */
	List<HistorialVuelo> findByUsuarioIdOrderByFechaVistaDesc(Long usuarioId);

	/**
	 * Busca un vuelo en el historial del usuario.
	 *
	 * @param usuarioId id del usuario
	 * @param vueloId id del vuelo
	 * @return el registro si existe
	 */
	Optional<HistorialVuelo> findFirstByUsuarioIdAndVueloId(Long usuarioId, String vueloId);

	/**
	 * Borra un vuelo del historial del usuario.
	 *
	 * @param usuarioId id del usuario
	 * @param vueloId id del vuelo
	 */
	void deleteByUsuarioIdAndVueloId(Long usuarioId, String vueloId);

	/**
	 * Borra todo el historial de un usuario.
	 *
	 * @param usuarioId id del usuario
	 */
	void deleteByUsuarioId(Long usuarioId);

}
