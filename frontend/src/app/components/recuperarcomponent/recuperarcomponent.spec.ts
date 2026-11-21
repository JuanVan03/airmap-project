import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule, NgForm } from '@angular/forms';
import { provideRouter } from '@angular/router';
import { Recuperarcomponent } from './recuperarcomponent';

const URL_BACK = 'http://localhost:8080';

describe('Recuperarcomponent', () => {
  let component: Recuperarcomponent;
  let fixture: ComponentFixture<Recuperarcomponent>;
  let http: HttpTestingController;

  // Formulario falso: solo importa si es valido o no
  const formulario = (invalid: boolean) => ({ invalid }) as NgForm;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Recuperarcomponent],
      imports: [FormsModule],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(Recuperarcomponent);
    component = fixture.componentInstance;
    http = TestBed.inject(HttpTestingController);
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('no llama al back si el correo no es valido', () => {
    component.enviar(formulario(true));

    http.expectNone(`${URL_BACK}/api/auth/recuperar`);
    expect(component.enviado()).toBe(false);
  });

  it('muestra la confirmacion despues de enviar', () => {
    component.correo = 'juan@correo.com';
    component.enviar(formulario(false));
    http.expectOne(`${URL_BACK}/api/auth/recuperar`).flush({ mensaje: 'ok' });

    expect(component.enviado()).toBe(true);
  });
});
