import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { Aparienciacomponent } from './aparienciacomponent';

describe('Aparienciacomponent', () => {
  let component: Aparienciacomponent;
  let fixture: ComponentFixture<Aparienciacomponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Aparienciacomponent],
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    fixture = TestBed.createComponent(Aparienciacomponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
