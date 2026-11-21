import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute, convertToParamMap, provideRouter } from '@angular/router';
import { Restablecercomponent } from './restablecercomponent';

const URL_BACK = 'http://localhost:8080';

function crear(token: string | null) {
  TestBed.configureTestingModule({
    declarations: [Restablecercomponent],
    imports: [FormsModule],
    providers: [
      provideHttpClient(),
      provideHttpClientTesting(),
      provideRouter([]),
      { provide: ActivatedRoute, useValue: { snapshot: { queryParamMap: convertToParamMap(token ? { token } : {}) } } },
    ],
  });
  const fixture = TestBed.createComponent(Restablecercomponent);
  fixture.detectChanges();
  return { componente: fixture.componentInstance, http: TestBed.inject(HttpTestingController) };
}

// Formulario falso: solo importa si es valido o no
const formulario = { invalid: false } as NgForm;

describe('Restablecercomponent', () => {
  it('sin token el enlace se marca como no valido', () => {
    const { componente } = crear(null);

    expect(componente.enlaceInvalido()).toBe(true);
  });

  it('valida el formato de la contraseña', () => {
    const { componente } = crear('abc');
    componente.contrasena = 'clave';
    expect(componente.contrasenaValida()).toBe(false);

    componente.contrasena = 'Clave1234';
    expect(componente.contrasenaValida()).toBe(true);
  });

  it('no llama al back si las contraseñas no coinciden', () => {
    const { componente, http } = crear('abc');
    componente.contrasena = 'Clave1234';
    componente.confirmar = 'Otra12345';
    componente.cambiar(formulario);

    http.expectNone(`${URL_BACK}/api/auth/restablecer`);
  });

  it('cambia la contraseña con el token del enlace', () => {
    const { componente, http } = crear('abc');
    componente.contrasena = 'Clave1234';
    componente.confirmar = 'Clave1234';
    componente.cambiar(formulario);

    const peticion = http.expectOne(`${URL_BACK}/api/auth/restablecer`);
    expect(peticion.request.body).toEqual({ token: 'abc', contrasena: 'Clave1234' });
    peticion.flush({ mensaje: 'ok' });

    expect(componente.listo()).toBe(true);
  });

  it('muestra el error del back si el enlace venció', () => {
    const { componente, http } = crear('abc');
    componente.contrasena = 'Clave1234';
    componente.confirmar = 'Clave1234';
    componente.cambiar(formulario);
    http
      .expectOne(`${URL_BACK}/api/auth/restablecer`)
      .flush({ mensaje: 'El enlace venció. Solicita uno nuevo.' }, { status: 400, statusText: 'Bad Request' });

    expect(componente.listo()).toBe(false);
    expect(componente.error()).toBe('El enlace venció. Solicita uno nuevo.');
  });
});
