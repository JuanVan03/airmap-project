import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { AuthService } from './auth.service';

const URL_BACK = 'http://localhost:8080';

describe('AuthService', () => {
  let service: AuthService;
  let http: HttpTestingController;

  const respuesta = {
    token: 'a.b.c',
    usuario: { nombre: 'Juan Pérez', correo: 'juan@correo.com', proveedor: 'LOCAL' as const },
  };

  beforeEach(() => {
    localStorage.clear();
    sessionStorage.clear();
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    });
    service = TestBed.inject(AuthService);
    http = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('guarda el token y el usuario al ingresar', () => {
    service.ingresar({ correo: 'juan@correo.com', contrasena: 'Clave1234' }, true).subscribe();
    http.expectOne(`${URL_BACK}/api/auth/login`).flush(respuesta);

    expect(service.autenticado()).toBe(true);
    expect(service.usuario()?.nombre).toBe('Juan Pérez');
    expect(localStorage.getItem('airmap_token')).toBe('a.b.c');
  });

  it('sin "recordar" la sesion queda solo en esta pestaña', () => {
    service.ingresar({ correo: 'juan@correo.com', contrasena: 'Clave1234' }, false).subscribe();
    http.expectOne(`${URL_BACK}/api/auth/login`).flush(respuesta);

    expect(localStorage.getItem('airmap_token')).toBeNull();
    expect(sessionStorage.getItem('airmap_token')).toBe('a.b.c');
  });

  it('registrar no inicia sesion: solo avisa que se envio el correo', () => {
    service.registrar({ nombre: 'Juan', correo: 'juan@correo.com', contrasena: 'Clave1234' }).subscribe();
    http.expectOne(`${URL_BACK}/api/auth/registro`).flush({ mensaje: 'Te enviamos un correo' });

    expect(service.autenticado()).toBe(false);
    expect(localStorage.getItem('airmap_token')).toBeNull();
  });

  it('verificarCorreo envia el token al back', () => {
    service.verificarCorreo('abc').subscribe();
    const peticion = http.expectOne(`${URL_BACK}/api/auth/verificar`);

    expect(peticion.request.body).toEqual({ token: 'abc' });
    peticion.flush({ mensaje: 'ok' });
  });

  it('reenviarVerificacion envia el correo al back', () => {
    service.reenviarVerificacion('juan@correo.com').subscribe();
    const peticion = http.expectOne(`${URL_BACK}/api/auth/reenviar-verificacion`);

    expect(peticion.request.body).toEqual({ correo: 'juan@correo.com' });
    peticion.flush({ mensaje: 'ok' });
  });

  it('recuperarContrasena envia el correo al back', () => {
    service.recuperarContrasena('juan@correo.com').subscribe();
    const peticion = http.expectOne(`${URL_BACK}/api/auth/recuperar`);

    expect(peticion.request.body).toEqual({ correo: 'juan@correo.com' });
    peticion.flush({ mensaje: 'ok' });
  });

  it('restablecerContrasena envia el token y la contraseña nueva', () => {
    service.restablecerContrasena('abc', 'Nueva1234').subscribe();
    const peticion = http.expectOne(`${URL_BACK}/api/auth/restablecer`);

    expect(peticion.request.body).toEqual({ token: 'abc', contrasena: 'Nueva1234' });
    peticion.flush({ mensaje: 'ok' });
  });

  it('actualizarPerfil cambia el nombre en la sesion y en el almacenamiento', () => {
    service.ingresar({ correo: 'juan@correo.com', contrasena: 'Clave1234' }, true).subscribe();
    http.expectOne(`${URL_BACK}/api/auth/login`).flush(respuesta);

    service.actualizarPerfil('Juan Camilo').subscribe();
    const peticion = http.expectOne(`${URL_BACK}/api/auth/perfil`);
    expect(peticion.request.method).toBe('PUT');
    expect(peticion.request.body).toEqual({ nombre: 'Juan Camilo' });
    peticion.flush({ nombre: 'Juan Camilo', correo: 'juan@correo.com', proveedor: 'LOCAL', rol: 'USUARIO' });

    expect(service.usuario()?.nombre).toBe('Juan Camilo');
    expect(JSON.parse(localStorage.getItem('airmap_usuario')!).nombre).toBe('Juan Camilo');
    expect(service.token()).toBe('a.b.c');
  });

  it('cerrarSesion borra la sesion y avisa al back para la auditoria', () => {
    service.ingresar({ correo: 'juan@correo.com', contrasena: 'Clave1234' }, true).subscribe();
    http.expectOne(`${URL_BACK}/api/auth/login`).flush(respuesta);

    service.cerrarSesion();
    http.expectOne(`${URL_BACK}/api/auth/logout`).flush(null, { status: 204, statusText: 'No Content' });

    expect(service.autenticado()).toBe(false);
    expect(localStorage.getItem('airmap_token')).toBeNull();
  });

  it('cerrarSesion sin avisar no llama al back', () => {
    service.ingresar({ correo: 'juan@correo.com', contrasena: 'Clave1234' }, true).subscribe();
    http.expectOne(`${URL_BACK}/api/auth/login`).flush(respuesta);

    service.cerrarSesion('/login', false);

    http.expectNone(`${URL_BACK}/api/auth/logout`);
    expect(service.autenticado()).toBe(false);
  });

  it('esAdmin es true solo para el rol ADMIN', () => {
    expect(service.esAdmin()).toBe(false);

    service.ingresar({ correo: 'admin@correo.com', contrasena: 'Clave1234' }, true).subscribe();
    http
      .expectOne(`${URL_BACK}/api/auth/login`)
      .flush({ token: 'a.b.c', usuario: { nombre: 'Administrador', correo: 'admin@correo.com', proveedor: 'LOCAL', rol: 'ADMIN' } });

    expect(service.esAdmin()).toBe(true);
  });
});
