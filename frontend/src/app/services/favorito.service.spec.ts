import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { FavoritoService } from './favorito.service';

describe('FavoritoService', () => {
  let service: FavoritoService;
  let http: HttpTestingController;

  beforeEach(() => {
    localStorage.clear();
    sessionStorage.clear();
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    });
    service = TestBed.inject(FavoritoService);
    http = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('agregar y quitar actualizan la lista', () => {
    service.agregar('abc123', 'AVA010').subscribe();
    http.expectOne('http://localhost:8080/api/favoritos').flush({ id: 1, vueloId: 'abc123', numeroVuelo: 'AVA010' });
    expect(service.esFavorito('abc123')).toBe(true);

    service.quitar('abc123').subscribe();
    http.expectOne('http://localhost:8080/api/favoritos/abc123').flush(null);
    expect(service.esFavorito('abc123')).toBe(false);
  });
});
