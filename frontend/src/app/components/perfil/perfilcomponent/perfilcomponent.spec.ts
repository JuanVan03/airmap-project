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

  it('carga las rutas guardadas al abrir', () => {
    http.expectOne('http://localhost:8080/api/rutas-guardadas').flush([]);
    expect(component.cargandoRutas()).toBe(false);
    expect(component.rutasGuardadas().length).toBe(0);
  });
});
