package co.edu.unbosque.airmap.service;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import co.edu.unbosque.airmap.entity.Usuario;
import co.edu.unbosque.airmap.exception.NegocioException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

// Envia los correos de AirMap con la libreria JavaMail (spring-boot-starter-mail)
@Service
public class CorreoService {

	@Autowired
	private JavaMailSender mailSender;

	// La cuenta desde la que se envian los correos (MAIL_USER en el .env)
	@Value("${spring.mail.username:}")
	private String remitente;

	@Value("${app.nombre-remitente:AirMap}")
	private String nombreRemitente;

	// Direccion del front, para armar los enlaces de los correos
	@Value("${app.front-url}")
	private String frontUrl;

	public CorreoService() {
	}

	public void enviarVerificacion(Usuario usuario, String token) {
		String enlace = frontUrl + "/verificar?token=" + token;
		String titulo = "Confirma tu correo";
		String saludo = "Hola " + usuario.getNombre() + ",";
		String mensaje = "Gracias por crear tu cuenta en AirMap. Para empezar a usarla, confirma que este correo es tuyo.";
		String nota = "Este enlace vence en 24 horas y solo se puede usar una vez.";

		enviar(usuario.getCorreo(), "Confirma tu correo en AirMap",
				PlantillaCorreo.crear(titulo, saludo, mensaje, "Confirmar mi correo", enlace, nota),
				PlantillaCorreo.crearTexto(titulo, saludo, mensaje, enlace, nota));
	}

	public void enviarRestablecer(Usuario usuario, String token) {
		String enlace = frontUrl + "/restablecer?token=" + token;
		String titulo = "Restablece tu contraseña";
		String saludo = "Hola " + usuario.getNombre() + ",";
		String mensaje = "Recibimos una solicitud para cambiar la contraseña de tu cuenta de AirMap. Usa el botón para crear una nueva.";
		String nota = "Este enlace vence en 1 hora y solo se puede usar una vez. Si no pediste el cambio, ignora este correo: tu contraseña actual sigue funcionando.";

		enviar(usuario.getCorreo(), "Restablece tu contraseña de AirMap",
				PlantillaCorreo.crear(titulo, saludo, mensaje, "Crear nueva contraseña", enlace, nota),
				PlantillaCorreo.crearTexto(titulo, saludo, mensaje, enlace, nota));
	}

	// Envia un correo con version HTML y version en texto plano
	public void enviar(String destino, String asunto, String html, String texto) {
		if (remitente == null || remitente.isBlank()) {
			throw new NegocioException("El envío de correos no está configurado (MAIL_USER y MAIL_PASSWORD en el .env del back).",
					503, "CORREO_NO_CONFIGURADO");
		}
		try {
			MimeMessage mensaje = mailSender.createMimeMessage();
			MimeMessageHelper ayudante = new MimeMessageHelper(mensaje, true, "UTF-8");
			ayudante.setFrom(remitente, nombreRemitente);
			ayudante.setTo(destino);
			ayudante.setSubject(asunto);
			ayudante.setText(texto, html);
			mailSender.send(mensaje);
		} catch (MessagingException | UnsupportedEncodingException | MailException ex) {
			throw new NegocioException("No pudimos enviar el correo. Intenta de nuevo en unos minutos.", 502,
					"CORREO_NO_ENVIADO");
		}
	}

}
