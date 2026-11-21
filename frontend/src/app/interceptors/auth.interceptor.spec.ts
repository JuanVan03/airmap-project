import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { authInterceptor } from './auth.interceptor';

const URL_BACK = 'http://localhost:8080';

describe('authInterceptor', () => {
  let http: HttpClient;
  let controlador: HttpTestingController;
  let auth: AuthService;

  beforeEach(() => {
    localStorage.clear();
    sessionStorage.clear();
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([authInterceptor])),
        provideHttpClientTesting(),
        provideRouter([]),
      ],
    });
    http = TestBed.inject(HttpClient);
    controlador = TestBed.inject(HttpTestingController);
    auth = TestBed.inject(AuthService);
  });

  it('agrega el token a las peticiones al back', () => {
    auth.token.set('mi-token');
    http.get(`${URL_BACK}/api/vuelos`).subscribe();

    const peticion = controlador.expectOne(`${URL_BACK}/api/vuelos`);
    expect(peticion.request.headers.get('Authorization')).toBe('Bearer mi-token');
  });

  it('no manda el token a otras direcciones', () => {
    auth.token.set('mi-token');
    http.get('https://otro-sitio.com/datos').subscribe();

    const peticion = controlador.expectOne('https://otro-sitio.com/datos');
    expect(peticion.request.headers.has('Authorization')).toBe(false);
  });
});
