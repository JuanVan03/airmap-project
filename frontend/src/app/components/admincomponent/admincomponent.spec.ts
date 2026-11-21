import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { provideRouter } from '@angular/router';
import { Admincomponent } from './admincomponent';

const URL = 'http://localhost:8080/api/admin/auditoria';

const pagina = (pag: number, total: number) => ({
  contenido: [
    {
      id: 1,
      fecha: '2026-11-21T15:30:00',
      usuario: 'juan@correo.com',
      accion: 'LOGIN_EXITOSO',
      resultado: 'EXITO',
      detalle: 'Ingreso con correo y contraseña',
      ip: '127.0.0.1',
      navegador: 'Chrome',
    },
  ],
  pagina: pag,
  tamano: 20,
  totalElementos: 40,
  totalPaginas: total,
});

describe('Admincomponent', () => {
  let componente: Admincomponent;
  let fixture: ComponentFixture<Admincomponent>;
  let http: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Admincomponent],
      imports: [FormsModule],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(Admincomponent);
    componente = fixture.componentInstance;
    http = TestBed.inject(HttpTestingController);
    fixture.detectChanges();

    // Al abrir pide las acciones del filtro y la primera pagina
    http.expectOne(`${URL}/acciones`).flush(['LOGIN_EXITOSO']);
    http.expectOne((p) => p.url === URL).flush(pagina(0, 2));
  });

  it('carga la primera pagina al abrir', () => {
    expect(componente.resultados()?.contenido.length).toBe(1);
    expect(componente.acciones()).toEqual(['LOGIN_EXITOSO']);
  });

  it('un filtro nuevo vuelve a la primera pagina', () => {
    componente.filtros.resultado = 'FALLO';
    componente.buscar();

    const peticion = http.expectOne((p) => p.url === URL);
    expect(peticion.request.params.get('pagina')).toBe('0');
    expect(peticion.request.params.get('resultado')).toBe('FALLO');
    peticion.flush(pagina(0, 1));
  });

  it('pasa a la pagina siguiente solo si existe', () => {
    componente.irA(1);
    const peticion = http.expectOne((p) => p.url === URL);
    expect(peticion.request.params.get('pagina')).toBe('1');
    peticion.flush(pagina(1, 2));

    // Ya es la ultima: no pide nada
    componente.irA(2);
    http.expectNone((p) => p.url === URL);
  });

  it('muestra el error del back', () => {
    componente.buscar();
    http.expectOne((p) => p.url === URL).flush({ mensaje: 'Sin permisos' }, { status: 403, statusText: 'Forbidden' });

    expect(componente.error()).toBe('Sin permisos');
  });

  it('traduce el código de la acción', () => {
    expect(componente.etiqueta('LOGIN_EXITOSO')).toBe('Inicio de sesión');
    expect(componente.etiqueta('OTRA_COSA')).toBe('OTRA_COSA');
  });
});
