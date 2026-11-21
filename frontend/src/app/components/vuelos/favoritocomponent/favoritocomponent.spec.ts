import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { Favoritocomponent } from './favoritocomponent';

describe('Favoritocomponent', () => {
  let component: Favoritocomponent;
  let fixture: ComponentFixture<Favoritocomponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Favoritocomponent],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(Favoritocomponent);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('vuelo', { id: 'abc123', numeroVuelo: 'AVA010' });
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('no es favorito al empezar', () => {
    expect(component.esFavorito()).toBe(false);
  });
});
