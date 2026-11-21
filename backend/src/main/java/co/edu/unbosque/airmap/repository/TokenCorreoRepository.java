package co.edu.unbosque.airmap.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.airmap.entity.TokenCorreo;
import co.edu.unbosque.airmap.entity.Usuario;

public interface TokenCorreoRepository extends JpaRepository<TokenCorreo, Long> {

	Optional<TokenCorreo> findByTokenHash(String tokenHash);

	// El ultimo enlace que se le envio al usuario (para no mandar correos sin parar)
	Optional<TokenCorreo> findFirstByUsuarioAndTipoOrderByCreadoDesc(Usuario usuario, String tipo);

	void deleteByUsuarioAndTipo(Usuario usuario, String tipo);

}
