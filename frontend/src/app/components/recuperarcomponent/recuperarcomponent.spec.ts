import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { Recuperarcomponent } from './recuperarcomponent';

describe('Recuperarcomponent', () => {
  let component: Recuperarcomponent;
  let fixture: ComponentFixture<Recuperarcomponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Recuperarcomponent],
      providers: [provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(Recuperarcomponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should show confirmation after sending', () => {
    component.enviar();
    expect(component.enviado()).toBe(true);
  });
});
