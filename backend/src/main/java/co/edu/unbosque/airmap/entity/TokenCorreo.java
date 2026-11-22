package co.edu.unbosque.airmap.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entidad de la tabla tokens_correo. Guarda los enlaces de confirmación de cuenta y de
 * restablecimiento de contraseña.
 *
 * @author Juan
 * @version 1.0
 */
@Entity
@Table(name = "tokens_correo")
public class TokenCorreo {

	/** Tipo de token para confirmar el correo. */
	public static final String VERIFICACION = "VERIFICACION";
	/** Tipo de token para restablecer la contraseña. */
	public static final String RESTABLECER = "RESTABLECER";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Se guarda el hash (SHA-256) del token, nunca el token: asi, si alguien ve la base de datos, no puede usar los enlaces
	@Column(nullable = false, unique = true, length = 64)
	private String tokenHash;

	// VERIFICACION o RESTABLECER
	@Column(nullable = false, length = 15)
	private String tipo;

	@ManyToOne(optional = false)
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	@Column(nullable = false)
	private LocalDateTime creado;

	@Column(nullable = false)
	private LocalDateTime expira;

	@Column(nullable = false)
	private boolean usado;

	/**
	 * Constructor vacío que necesita JPA.
	 */
	public TokenCorreo() {
	}

	/**
	 * Crea el registro con sus datos. El id lo pone la base de datos. La fecha se toma del momento actual.
	 *
	 * @param tokenHash hash SHA-256 del token
	 * @param tipo tipo de token (VERIFICACION o RESTABLECER)
	 * @param usuario usuario al que pertenece el token
	 * @param expira fecha y hora en que vence el enlace
	 */
	public TokenCorreo(String tokenHash, String tipo, Usuario usuario, LocalDateTime expira) {
		super();
		this.tokenHash = tokenHash;
		this.tipo = tipo;
		this.usuario = usuario;
		this.creado = LocalDateTime.now();
		this.expira = expira;
		this.usado = false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTokenHash() {
		return tokenHash;
	}

	public void setTokenHash(String tokenHash) {
		this.tokenHash = tokenHash;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public LocalDateTime getCreado() {
		return creado;
	}

	public void setCreado(LocalDateTime creado) {
		this.creado = creado;
	}

	public LocalDateTime getExpira() {
		return expira;
	}

	public void setExpira(LocalDateTime expira) {
		this.expira = expira;
	}

	public boolean isUsado() {
		return usado;
	}

	public void setUsado(boolean usado) {
		this.usado = usado;
	}

}
