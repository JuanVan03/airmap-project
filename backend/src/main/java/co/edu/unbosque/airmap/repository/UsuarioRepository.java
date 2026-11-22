package co.edu.unbosque.airmap.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.airmap.entity.Usuario;

/**
 * Repositorio JPA de los usuarios.
 *
 * @author Juan
 * @version 1.0
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	/**
	 * Busca un usuario por su correo.
	 *
	 * @param correo correo del usuario
	 * @return el usuario si existe
	 */
	Optional<Usuario> findByCorreo(String correo);

	/**
	 * Revisa si ya hay una cuenta con ese correo.
	 *
	 * @param correo correo a revisar
	 * @return true si el correo ya está registrado
	 */
	boolean existsByCorreo(String correo);

}
