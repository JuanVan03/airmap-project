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

// Enlace de un solo uso que se envia por correo (confirmar cuenta o restablecer contrasena)
@Entity
@Table(name = "tokens_correo")
public class TokenCorreo {

	public static final String VERIFICACION = "VERIFICACION";
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

	public TokenCorreo() {
	}

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
