package co.edu.unbosque.airmap.service;

// Arma el HTML de los correos de AirMap. Usa tablas y estilos en linea porque
// asi se ve igual en Gmail, Outlook y el celular.
public final class PlantillaCorreo {

	private static final String HTML = """
			<!DOCTYPE html>
			<html lang="es">
			<head>
			<meta charset="UTF-8">
			<meta name="viewport" content="width=device-width, initial-scale=1.0">
			<title>{{titulo}}</title>
			</head>
			<body style="margin:0;padding:0;background-color:#EEF2F7;">
			<table role="presentation" width="100%" cellpadding="0" cellspacing="0" border="0" style="background-color:#EEF2F7;">
			<tr><td align="center" style="padding:32px 16px;">
			  <table role="presentation" width="560" cellpadding="0" cellspacing="0" border="0" style="width:100%;max-width:560px;background-color:#FFFFFF;border-radius:16px;overflow:hidden;">
			    <tr>
			      <td align="center" style="background-color:#0B1220;padding:28px 24px;">
			        <div style="font-family:Arial,Helvetica,sans-serif;font-size:26px;font-weight:bold;color:#FFFFFF;letter-spacing:0.5px;">&#9992; AirMap</div>
			        <div style="font-family:Arial,Helvetica,sans-serif;font-size:13px;color:#22D3EE;margin-top:6px;">El mundo en vuelo, a un clic</div>
			      </td>
			    </tr>
			    <tr>
			      <td style="padding:36px 36px 12px 36px;font-family:Arial,Helvetica,sans-serif;">
			        <h1 style="margin:0 0 16px 0;font-size:22px;line-height:1.3;color:#0B1220;">{{titulo}}</h1>
			        <p style="margin:0 0 12px 0;font-size:16px;line-height:1.6;color:#334155;">{{saludo}}</p>
			        <p style="margin:0 0 8px 0;font-size:16px;line-height:1.6;color:#334155;">{{mensaje}}</p>
			      </td>
			    </tr>
			    <tr>
			      <td align="center" style="padding:20px 36px 28px 36px;">
			        <table role="presentation" cellpadding="0" cellspacing="0" border="0">
			          <tr>
			            <td align="center" bgcolor="#2F80ED" style="border-radius:10px;">
			              <a href="{{enlace}}" target="_blank" style="display:inline-block;padding:14px 32px;font-family:Arial,Helvetica,sans-serif;font-size:16px;font-weight:bold;color:#FFFFFF;text-decoration:none;border-radius:10px;">{{boton}}</a>
			            </td>
			          </tr>
			        </table>
			      </td>
			    </tr>
			    <tr>
			      <td style="padding:0 36px 28px 36px;font-family:Arial,Helvetica,sans-serif;">
			        <p style="margin:0 0 8px 0;font-size:13px;line-height:1.6;color:#64748B;">{{nota}}</p>
			        <p style="margin:0;font-size:12px;line-height:1.6;color:#94A3B8;">Si el botón no funciona, copia y pega este enlace en tu navegador:<br>
			        <a href="{{enlace}}" target="_blank" style="color:#2F80ED;word-break:break-all;">{{enlace}}</a></p>
			      </td>
			    </tr>
			    <tr>
			      <td align="center" style="background-color:#F8FAFC;border-top:1px solid #E2E8F0;padding:20px 24px;font-family:Arial,Helvetica,sans-serif;font-size:12px;line-height:1.6;color:#94A3B8;">
			        Recibiste este correo porque alguien usó esta dirección en AirMap.<br>
			        Si no fuiste tú, puedes ignorarlo con tranquilidad.
			      </td>
			    </tr>
			  </table>
			</td></tr>
			</table>
			</body>
			</html>
			""";

	private PlantillaCorreo() {
	}

	// Devuelve el correo ya armado. Los textos se escapan para que nadie pueda meter HTML (por ejemplo desde el nombre)
	public static String crear(String titulo, String saludo, String mensaje, String textoBoton, String enlace,
			String nota) {
		return HTML.replace("{{titulo}}", escapar(titulo)).replace("{{saludo}}", escapar(saludo))
				.replace("{{mensaje}}", escapar(mensaje)).replace("{{boton}}", escapar(textoBoton))
				.replace("{{nota}}", escapar(nota)).replace("{{enlace}}", escapar(enlace));
	}

	// Version en texto plano para los programas de correo que no muestran HTML
	public static String crearTexto(String titulo, String saludo, String mensaje, String enlace, String nota) {
		return titulo + "\n\n" + saludo + "\n\n" + mensaje + "\n\n" + enlace + "\n\n" + nota + "\n\n— AirMap";
	}

	public static String escapar(String texto) {
		if (texto == null) {
			return "";
		}
		return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;")
				.replace("'", "&#39;");
	}

}
