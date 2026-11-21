import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { AuditoriaService } from './auditoria.service';

const URL = 'http://localhost:8080/api/admin/auditoria';

describe('AuditoriaService', () => {
  let service: AuditoriaService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    service = TestBed.inject(AuditoriaService);
    http = TestBed.inject(HttpTestingController);
  });

  it('solo manda los filtros que tienen algo escrito', () => {
    service.listar({ usuario: 'juan', accion: '', resultado: 'FALLO', desde: '', hasta: '' }, 2, 20).subscribe();
    const peticion = http.expectOne((p) => p.url === URL);

    expect(peticion.request.params.get('pagina')).toBe('2');
    expect(peticion.request.params.get('tamano')).toBe('20');
    expect(peticion.request.params.get('usuario')).toBe('juan');
    expect(peticion.request.params.get('resultado')).toBe('FALLO');
    expect(peticion.request.params.has('accion')).toBe(false);
    expect(peticion.request.params.has('desde')).toBe(false);
    peticion.flush({ contenido: [], pagina: 2, tamano: 20, totalElementos: 0, totalPaginas: 0 });
  });

  it('pide la lista de acciones', () => {
    service.acciones().subscribe();
    http.expectOne(`${URL}/acciones`).flush([]);
  });
});
