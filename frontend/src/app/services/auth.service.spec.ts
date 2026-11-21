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

  it('cerrarSesion borra la sesion', () => {
    service.ingresar({ correo: 'juan@correo.com', contrasena: 'Clave1234' }, true).subscribe();
    http.expectOne(`${URL_BACK}/api/auth/login`).flush(respuesta);

    service.cerrarSesion();

    expect(service.autenticado()).toBe(false);
    expect(localStorage.getItem('airmap_token')).toBeNull();
  });
});
