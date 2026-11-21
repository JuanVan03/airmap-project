import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, provideRouter } from '@angular/router';
import { authGuard, invitadoGuard } from './auth.guard';

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
});
