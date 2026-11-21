import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { Perfilcomponent } from './perfilcomponent';

describe('Perfilcomponent', () => {
  let component: Perfilcomponent;
  let fixture: ComponentFixture<Perfilcomponent>;
  let http: HttpTestingController;

  beforeEach(async () => {
    localStorage.clear();
    sessionStorage.clear();
    await TestBed.configureTestingModule({
      declarations: [Perfilcomponent],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(Perfilcomponent);
    component = fixture.componentInstance;
    http = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('carga el historial al abrir', () => {
    http
      .expectOne('http://localhost:8080/api/historial')
      .flush([{ vueloId: 'abc123', numeroVuelo: 'AVA010', fechaVista: '2026-11-21T15:30:00' }]);

    expect(component.cargandoHistorial()).toBe(false);
    expect(component.historial().length).toBe(1);
  });

  it('quita un vuelo del historial', () => {
    http
      .expectOne('http://localhost:8080/api/historial')
      .flush([{ vueloId: 'abc123', numeroVuelo: 'AVA010', fechaVista: '2026-11-21T15:30:00' }]);

    component.quitarDelHistorial(component.historial()[0]);
    http.expectOne('http://localhost:8080/api/historial/abc123').flush(null, { status: 204, statusText: 'No Content' });

    expect(component.historial().length).toBe(0);
  });

  it('borra todo el historial', () => {
    http
      .expectOne('http://localhost:8080/api/historial')
      .flush([{ vueloId: 'abc123', numeroVuelo: 'AVA010', fechaVista: '2026-11-21T15:30:00' }]);

    component.vaciarHistorial();
    http.expectOne({ method: 'DELETE', url: 'http://localhost:8080/api/historial' }).flush(null, { status: 204, statusText: 'No Content' });

    expect(component.historial().length).toBe(0);
  });

  it('no guarda un nombre vacío', () => {
    component.nombreEditado = '   ';
    component.guardarAjustes();

    http.expectNone('http://localhost:8080/api/auth/perfil');
    expect(component.errorAjustes()).toBe('Ingresa tu nombre.');
  });

  it('guarda el nombre nuevo', () => {
    component.nombreEditado = ' Juan Camilo ';
    component.guardarAjustes();

    const peticion = http.expectOne('http://localhost:8080/api/auth/perfil');
    expect(peticion.request.body).toEqual({ nombre: 'Juan Camilo' });
    peticion.flush({ nombre: 'Juan Camilo', correo: 'juan@correo.com', proveedor: 'LOCAL', rol: 'USUARIO' });

    expect(component.exitoAjustes()).toBe(true);
    expect(component.nombreEditado).toBe('Juan Camilo');
  });

  it('muestra el error del back al guardar', () => {
    component.nombreEditado = 'Juan';
    component.guardarAjustes();
    http
      .expectOne('http://localhost:8080/api/auth/perfil')
      .flush({ mensaje: 'Ingresa tu nombre.' }, { status: 400, statusText: 'Bad Request' });

    expect(component.exitoAjustes()).toBe(false);
    expect(component.errorAjustes()).toBe('Ingresa tu nombre.');
  });

  it('carga las rutas guardadas al abrir', () => {
    http.expectOne('http://localhost:8080/api/rutas-guardadas').flush([]);
    expect(component.cargandoRutas()).toBe(false);
    expect(component.rutasGuardadas().length).toBe(0);
  });
});
