import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable, computed, signal } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { AuthRespuesta } from '../models/auth-respuesta';
import { MensajeRespuesta } from '../models/mensaje-respuesta';
import { DatosLogin, DatosRegistro } from '../models/datos-auth';
import { Usuario } from '../models/usuario';

// Nombres con los que se guarda la sesion en el navegador
const CLAVE_TOKEN = 'airmap_token';
const CLAVE_USUARIO = 'airmap_usuario';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly url = 'http://localhost:8080/api/auth';

  // Sesion actual: null cuando nadie ha ingresado
  readonly token = signal<string | null>(null);
  readonly usuario = signal<Usuario | null>(null);
  readonly autenticado = computed(() => this.token() !== null);
  readonly esAdmin = computed(() => this.usuario()?.rol === 'ADMIN');

  constructor(
    private http: HttpClient,
    private router: Router,
  ) {
    this.restaurarSesion();
  }

  // Crea la cuenta y el back envia un correo de confirmacion. No inicia sesion:
  // la persona ingresa despues de confirmar su correo.
  registrar(datos: DatosRegistro): Observable<MensajeRespuesta> {
    return this.http.post<MensajeRespuesta>(`${this.url}/registro`, datos);
  }

  // Confirma el correo con el token que viene en el enlace del mensaje
  verificarCorreo(token: string): Observable<MensajeRespuesta> {
    return this.http.post<MensajeRespuesta>(`${this.url}/verificar`, { token });
  }

  // Pide el correo con el enlace para cambiar la contrasena
  recuperarContrasena(correo: string): Observable<MensajeRespuesta> {
    return this.http.post<MensajeRespuesta>(`${this.url}/recuperar`, { correo });
  }

  // Guarda la contrasena nueva con el token que viene en el enlace del correo
  restablecerContrasena(token: string, contrasena: string): Observable<MensajeRespuesta> {
    return this.http.post<MensajeRespuesta>(`${this.url}/restablecer`, { token, contrasena });
  }

  // Pide que se vuelva a enviar el correo de confirmacion
  reenviarVerificacion(correo: string): Observable<MensajeRespuesta> {
    return this.http.post<MensajeRespuesta>(`${this.url}/reenviar-verificacion`, { correo });
  }

  // "recordar" = true guarda la sesion aunque se cierre el navegador
  ingresar(datos: DatosLogin, recordar: boolean): Observable<AuthRespuesta> {
    return this.http
      .post<AuthRespuesta>(`${this.url}/login`, datos)
      .pipe(tap((respuesta) => this.guardarSesion(respuesta, recordar)));
  }

  // La credencial es el token que entrega el boton de Google
  ingresarConGoogle(credencial: string): Observable<AuthRespuesta> {
    return this.http
      .post<AuthRespuesta>(`${this.url}/google`, { credencial })
      .pipe(tap((respuesta) => this.guardarSesion(respuesta, true)));
  }

  // Cambia el nombre del usuario. El back devuelve los datos nuevos y se actualiza la sesion guardada.
  actualizarPerfil(nombre: string): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.url}/perfil`, { nombre }).pipe(
      tap((usuario) => {
        this.usuario.set(usuario);
        this.actualizarUsuarioGuardado(usuario);
      }),
    );
  }

  // "avisarAlBack" deja constancia del cierre de sesion en la auditoria. Es false cuando la sesion
  // se cierra porque el token ya no sirve (el back lo rechazaria y se repetiria el aviso sin fin).
  cerrarSesion(destino = '/inicio', avisarAlBack = true): void {
    if (avisarAlBack && this.token() !== null) {
      // Si falla no importa: igual se cierra la sesion en este navegador
      this.http.post<void>(`${this.url}/logout`, {}).subscribe({ error: () => {} });
    }
    this.limpiarSesion();
    this.router.navigateByUrl(destino);
  }

  // Dice si hay una sesion valida; si el token ya vencio la cierra
  haySesion(): boolean {
    const token = this.token();
    if (token === null) {
      return false;
    }
    if (this.estaVencido(token)) {
      this.limpiarSesion();
      return false;
    }
    return true;
  }

  // Convierte el error del back en un texto para mostrarle al usuario
  mensajeDeError(error: HttpErrorResponse): string {
    if (error.status === 0) {
      return 'No se pudo conectar con el servidor. Intenta más tarde.';
    }
    const detalle = error.error?.errores?.[0]?.mensaje;
    return detalle ?? error.error?.mensaje ?? 'Ocurrió un error inesperado. Intenta de nuevo.';
  }

  private guardarSesion(respuesta: AuthRespuesta, recordar: boolean): void {
    this.limpiarAlmacenamiento();
    const almacen = recordar ? localStorage : sessionStorage;
    almacen.setItem(CLAVE_TOKEN, respuesta.token);
    almacen.setItem(CLAVE_USUARIO, JSON.stringify(respuesta.usuario));
    this.token.set(respuesta.token);
    this.usuario.set(respuesta.usuario);
  }

  // Cambia los datos del usuario en el lugar donde se guardo la sesion (recordada o solo de esta pestaña)
  private actualizarUsuarioGuardado(usuario: Usuario): void {
    for (const almacen of [localStorage, sessionStorage]) {
      if (almacen.getItem(CLAVE_TOKEN)) {
        almacen.setItem(CLAVE_USUARIO, JSON.stringify(usuario));
      }
    }
  }

  // Al abrir la pagina vuelve a cargar la sesion guardada, si todavia sirve
  private restaurarSesion(): void {
    for (const almacen of [localStorage, sessionStorage]) {
      const token = almacen.getItem(CLAVE_TOKEN);
      const usuario = almacen.getItem(CLAVE_USUARIO);
      if (token && usuario && !this.estaVencido(token)) {
        this.token.set(token);
        this.usuario.set(JSON.parse(usuario));
        return;
      }
    }
    this.limpiarAlmacenamiento();
  }

  private limpiarSesion(): void {
    this.limpiarAlmacenamiento();
    this.token.set(null);
    this.usuario.set(null);
  }

  private limpiarAlmacenamiento(): void {
    for (const almacen of [localStorage, sessionStorage]) {
      almacen.removeItem(CLAVE_TOKEN);
      almacen.removeItem(CLAVE_USUARIO);
    }
  }

  // El token es "encabezado.datos.firma": en los datos viene "exp", la fecha en que vence
  private estaVencido(token: string): boolean {
    try {
      const datos = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/');
      const exp = JSON.parse(atob(datos)).exp;
      return exp * 1000 <= Date.now();
    } catch {
      return true;
    }
  }
}
