package co.edu.unbosque.airmap.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.airmap.entity.Favorito;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

	List<Favorito> findByUsuarioIdOrderByFechaGuardadoDesc(Long usuarioId);

	Optional<Favorito> findByUsuarioIdAndVueloId(Long usuarioId, String vueloId);

	long countByUsuarioId(Long usuarioId);

}
