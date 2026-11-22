package co.edu.unbosque.airmap.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.airmap.entity.Preferencias;

public interface PreferenciasRepository extends JpaRepository<Preferencias, Long> {

	Optional<Preferencias> findByUsuarioId(Long usuarioId);

}
