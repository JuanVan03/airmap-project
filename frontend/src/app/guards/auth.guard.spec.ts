import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, provideRouter } from '@angular/router';
import { adminGuard, authGuard, invitadoGuard } from './auth.guard';

describe('Guards de sesion', () => {
  beforeEach(() => {
    localStorage.clear();
    sessionStorage.clear();
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    });
  });

  it('authGuard manda al login cuando no hay sesion', () => {
    const resultado = TestBed.runInInjectionContext(() =>
      authGuard({} as ActivatedRouteSnapshot, { url: '/perfil' } as RouterStateSnapshot),
    );
    expect(resultado instanceof UrlTree).toBe(true);
    expect((resultado as UrlTree).toString()).toContain('/login');
  });

  it('invitadoGuard deja pasar cuando no hay sesion', () => {
    const resultado = TestBed.runInInjectionContext(() =>
      invitadoGuard({} as ActivatedRouteSnapshot, { url: '/login' } as RouterStateSnapshot),
    );
    expect(resultado).toBe(true);
  });

  it('adminGuard manda al login cuando no hay sesion', () => {
    const resultado = TestBed.runInInjectionContext(() =>
      adminGuard({} as ActivatedRouteSnapshot, { url: '/admin' } as RouterStateSnapshot),
    );
    expect(resultado instanceof UrlTree).toBe(true);
    expect((resultado as UrlTree).toString()).toContain('/login');
  });

  it('adminGuard no deja pasar a un usuario normal', () => {
    // Token de prueba que vence en el año 2286, para que la sesion cuente como vigente
    const datos = btoa(JSON.stringify({ exp: 9999999999 }));
    const usuario = { nombre: 'Juan', correo: 'juan@correo.com', proveedor: 'LOCAL', rol: 'USUARIO' };
    localStorage.setItem('airmap_token', `a.${datos}.c`);
    localStorage.setItem('airmap_usuario', JSON.stringify(usuario));

    const resultado = TestBed.runInInjectionContext(() =>
      adminGuard({} as ActivatedRouteSnapshot, { url: '/admin' } as RouterStateSnapshot),
    );
    expect(resultado instanceof UrlTree).toBe(true);
    expect((resultado as UrlTree).toString()).toBe('/inicio');
  });

  it('adminGuard deja pasar al administrador', () => {
    const datos = btoa(JSON.stringify({ exp: 9999999999 }));
    const admin = { nombre: 'Administrador', correo: 'admin@correo.com', proveedor: 'LOCAL', rol: 'ADMIN' };
    localStorage.setItem('airmap_token', `a.${datos}.c`);
    localStorage.setItem('airmap_usuario', JSON.stringify(admin));

    const resultado = TestBed.runInInjectionContext(() =>
      adminGuard({} as ActivatedRouteSnapshot, { url: '/admin' } as RouterStateSnapshot),
    );
    expect(resultado).toBe(true);
  });
});
