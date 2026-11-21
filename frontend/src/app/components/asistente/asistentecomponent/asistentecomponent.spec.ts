import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { provideRouter } from '@angular/router';
import { Asistentecomponent } from './asistentecomponent';

describe('Asistentecomponent', () => {
  let component: Asistentecomponent;
  let fixture: ComponentFixture<Asistentecomponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Asistentecomponent],
      imports: [FormsModule],
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    fixture = TestBed.createComponent(Asistentecomponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
