import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { ActivatedRoute, convertToParamMap, provideRouter } from '@angular/router';
import { Verificarcomponent } from './verificarcomponent';

const URL_BACK = 'http://localhost:8080';

function crear(token: string | null) {
  TestBed.configureTestingModule({
    declarations: [Verificarcomponent],
    providers: [
      provideHttpClient(),
      provideHttpClientTesting(),
      provideRouter([]),
      { provide: ActivatedRoute, useValue: { snapshot: { queryParamMap: convertToParamMap(token ? { token } : {}) } } },
    ],
  });
  const fixture = TestBed.createComponent(Verificarcomponent);
  fixture.detectChanges();
  return { componente: fixture.componentInstance, http: TestBed.inject(HttpTestingController) };
}

describe('Verificarcomponent', () => {
  it('confirma el correo con el token del enlace', () => {
    const { componente, http } = crear('abc123');
    http.expectOne(`${URL_BACK}/api/auth/verificar`).flush({ mensaje: 'Listo' });

    expect(componente.estado()).toBe('ok');
    expect(componente.mensaje()).toBe('Listo');
  });

  it('muestra el error del back si el enlace no sirve', () => {
    const { componente, http } = crear('malo');
    http
      .expectOne(`${URL_BACK}/api/auth/verificar`)
      .flush({ mensaje: 'El enlace venció. Solicita uno nuevo.' }, { status: 400, statusText: 'Bad Request' });

    expect(componente.estado()).toBe('error');
    expect(componente.mensaje()).toBe('El enlace venció. Solicita uno nuevo.');
  });

  it('sin token no llama al back', () => {
    const { componente, http } = crear(null);
    http.expectNone(`${URL_BACK}/api/auth/verificar`);

    expect(componente.estado()).toBe('error');
  });
});
