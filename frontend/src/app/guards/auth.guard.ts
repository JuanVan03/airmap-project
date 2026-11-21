import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

// Paginas que necesitan sesion (ej. /perfil): si no hay, manda al login
export const authGuard: CanActivateFn = (ruta, estado) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  if (auth.haySesion()) {
    return true;
  }
  return router.createUrlTree(['/login'], { queryParams: { redirigir: estado.url } });
};

// Login y registro: si ya hay sesion no tiene sentido mostrarlos
export const invitadoGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);
  if (!auth.haySesion()) {
    return true;
  }
  return router.createUrlTree([auth.esAdmin() ? '/admin' : '/perfil']);
};

// Paginas solo para el administrador (ej. /admin). Sin sesion va al login; con sesion normal, al inicio
export const adminGuard: CanActivateFn = (ruta, estado) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  if (!auth.haySesion()) {
    return router.createUrlTree(['/login'], { queryParams: { redirigir: estado.url } });
  }
  return auth.esAdmin() ? true : router.createUrlTree(['/inicio']);
};
