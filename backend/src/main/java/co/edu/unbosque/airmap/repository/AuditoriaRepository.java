package co.edu.unbosque.airmap.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unbosque.airmap.entity.Auditoria;

/**
 * Repositorio JPA de los registros de auditoría.
 *
 * @author Juan
 * @version 1.0
 */
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {

	/**
	 * Busca registros con filtros, del más nuevo al más viejo. Los filtros vacíos no filtran y las
	 * fechas siempre llegan: si no se eligieron, el servicio pone un rango amplio.
	 *
	 * @param accion acción exacta (vacío para todas)
	 * @param resultado EXITO o FALLO (vacío para ambos)
	 * @param usuario parte del correo del usuario
	 * @param desde fecha y hora inicial
	 * @param hasta fecha y hora final
	 * @param pageable página, tamaño y orden
	 * @return página de registros
	 */
	@Query("select a from Auditoria a where (:accion = '' or a.accion = :accion) "
			+ "and (:resultado = '' or a.resultado = :resultado) "
			+ "and (:usuario = '' or lower(a.usuario) like lower(concat('%', :usuario, '%'))) "
			+ "and a.fecha >= :desde and a.fecha <= :hasta order by a.fecha desc, a.id desc")
	Page<Auditoria> buscar(@Param("accion") String accion, @Param("resultado") String resultado,
			@Param("usuario") String usuario, @Param("desde") LocalDateTime desde,
			@Param("hasta") LocalDateTime hasta, Pageable pageable);

	/**
	 * Lista las acciones distintas que hay guardadas.
	 *
	 * @return acciones ordenadas
	 */
	@Query("select distinct a.accion from Auditoria a order by a.accion")
	List<String> acciones();

}
