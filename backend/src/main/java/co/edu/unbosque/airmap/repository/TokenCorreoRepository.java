package co.edu.unbosque.airmap.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.airmap.entity.TokenCorreo;
import co.edu.unbosque.airmap.entity.Usuario;

/**
 * Repositorio JPA de los tokens que se mandan por correo (confirmar cuenta y restablecer
 * contraseña).
 *
 * @author SakiYoshida
 * @version 1.0
 */
public interface TokenCorreoRepository extends JpaRepository<TokenCorreo, Long> {

	/**
	 * Busca un token por su hash.
	 *
	 * @param tokenHash hash SHA-256 del token
	 * @return el token si existe
	 */
	Optional<TokenCorreo> findByTokenHash(String tokenHash);

	/**
	 * Último token de un tipo que se le creó al usuario.
	 *
	 * @param usuario usuario
	 * @param tipo VERIFICACION o RESTABLECER
	 * @return el token más reciente si existe
	 */
	Optional<TokenCorreo> findFirstByUsuarioAndTipoOrderByCreadoDesc(Usuario usuario, String tipo);

	/**
	 * Borra los tokens de un tipo del usuario.
	 *
	 * @param usuario usuario
	 * @param tipo VERIFICACION o RESTABLECER
	 */
	void deleteByUsuarioAndTipo(Usuario usuario, String tipo);

}
