import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { HistorialService } from './historial.service';

const URL = 'http://localhost:8080/api/historial';

describe('HistorialService', () => {
  let service: HistorialService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    service = TestBed.inject(HistorialService);
    http = TestBed.inject(HttpTestingController);
  });

  it('lista el historial', () => {
    service.listar().subscribe((lista) => expect(lista.length).toBe(1));
    http.expectOne(URL).flush([{ vueloId: 'abc123', numeroVuelo: 'AVA010', fechaVista: '2026-11-21T15:30:00' }]);
  });

  it('registra un vuelo con su id y número', () => {
    service.registrar('abc123', 'AVA010').subscribe();
    const peticion = http.expectOne(URL);

    expect(peticion.request.method).toBe('POST');
    expect(peticion.request.body).toEqual({ vueloId: 'abc123', numeroVuelo: 'AVA010' });
    peticion.flush(null, { status: 204, statusText: 'No Content' });
  });

  it('quita un vuelo por su id', () => {
    service.quitar('abc123').subscribe();
    const peticion = http.expectOne(`${URL}/abc123`);

    expect(peticion.request.method).toBe('DELETE');
    peticion.flush(null, { status: 204, statusText: 'No Content' });
  });

  it('vacía todo el historial', () => {
    service.vaciar().subscribe();
    const peticion = http.expectOne(URL);

    expect(peticion.request.method).toBe('DELETE');
    peticion.flush(null, { status: 204, statusText: 'No Content' });
  });
});
