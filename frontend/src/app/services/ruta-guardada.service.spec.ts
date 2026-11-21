import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { RutaGuardadaService } from './ruta-guardada.service';

describe('RutaGuardadaService', () => {
  let service: RutaGuardadaService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(RutaGuardadaService);
    http = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('listar pide las rutas al back', () => {
    service.listar().subscribe((rutas) => expect(rutas.length).toBe(0));
    http.expectOne('http://localhost:8080/api/rutas-guardadas').flush([]);
  });
});
