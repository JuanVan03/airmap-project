import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { environment } from '../../environments/environment';

// Direccion del back de AirMap
const URL_BACK = environment.apiUrl;

// Agrega el token a cada peticion que va al back de AirMap
export const authInterceptor: HttpInterceptorFn = (peticion, siguiente) => {
  const auth = inject(AuthService);
  const token = auth.token();

  // Sin sesion, o peticiones a otras paginas (Google, etc.): no se toca nada
  if (!token || !peticion.url.startsWith(URL_BACK)) {
    return siguiente(peticion);
  }

  const conToken = peticion.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
  return siguiente(conToken).pipe(
    catchError((error: HttpErrorResponse) => {
      // El back rechazo el token (vencio o no sirve): se cierra la sesion
      if (error.status === 401) {
        auth.cerrarSesion('/login', false);
      }
      return throwError(() => error);
    }),
  );
};
