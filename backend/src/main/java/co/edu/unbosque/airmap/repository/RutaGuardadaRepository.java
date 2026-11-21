package co.edu.unbosque.airmap.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.airmap.entity.RutaGuardada;

public interface RutaGuardadaRepository extends JpaRepository<RutaGuardada, Long> {

	List<RutaGuardada> findByUsuarioIdOrderByFechaGuardadoDesc(Long usuarioId);

	Optional<RutaGuardada> findByIdAndUsuarioId(Long id, Long usuarioId);

	long countByUsuarioId(Long usuarioId);

}
