import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { provideRouter } from '@angular/router';
import { Registercomponent } from './registercomponent';

describe('Registercomponent', () => {
  let component: Registercomponent;
  let fixture: ComponentFixture<Registercomponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Registercomponent],
      imports: [FormsModule],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(Registercomponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('valida el formato de la contraseña', () => {
    component.contrasena = 'clave';
    expect(component.contrasenaValida()).toBe(false);
    component.contrasena = 'Clave1234';
    expect(component.contrasenaValida()).toBe(true);
  });
});
