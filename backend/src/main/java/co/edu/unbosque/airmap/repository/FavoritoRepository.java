package co.edu.unbosque.airmap.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.airmap.entity.Favorito;

/**
 * Repositorio JPA de los vuelos favoritos.
 *
 * @author SakiYoshida
 * @version 1.0
 */
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

	/**
	 * Favoritos de un usuario, del más nuevo al más viejo.
	 *
	 * @param usuarioId id del usuario
	 * @return lista de favoritos
	 */
	List<Favorito> findByUsuarioIdOrderByFechaGuardadoDesc(Long usuarioId);

	/**
	 * Busca si un vuelo ya es favorito del usuario.
	 *
	 * @param usuarioId id del usuario
	 * @param vueloId id del vuelo
	 * @return el favorito si existe
	 */
	Optional<Favorito> findByUsuarioIdAndVueloId(Long usuarioId, String vueloId);

	/**
	 * Cuenta los favoritos de un usuario.
	 *
	 * @param usuarioId id del usuario
	 * @return cantidad de favoritos
	 */
	long countByUsuarioId(Long usuarioId);

}
