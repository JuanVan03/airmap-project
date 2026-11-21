import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { Informacioncomponent } from './informacioncomponent';

describe('Informacioncomponent', () => {
  let component: Informacioncomponent;
  let fixture: ComponentFixture<Informacioncomponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Informacioncomponent],
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    fixture = TestBed.createComponent(Informacioncomponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
