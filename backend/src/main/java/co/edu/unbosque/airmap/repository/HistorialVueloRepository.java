package co.edu.unbosque.airmap.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.airmap.entity.HistorialVuelo;

public interface HistorialVueloRepository extends JpaRepository<HistorialVuelo, Long> {

	List<HistorialVuelo> findByUsuarioIdOrderByFechaVistaDesc(Long usuarioId);

	Optional<HistorialVuelo> findFirstByUsuarioIdAndVueloId(Long usuarioId, String vueloId);

	void deleteByUsuarioIdAndVueloId(Long usuarioId, String vueloId);

	void deleteByUsuarioId(Long usuarioId);

}
