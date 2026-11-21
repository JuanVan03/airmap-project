import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { provideRouter } from '@angular/router';
import { Viajesterrestrescomponent } from './viajesterrestrescomponent';

describe('Viajesterrestrescomponent', () => {
  let component: Viajesterrestrescomponent;
  let fixture: ComponentFixture<Viajesterrestrescomponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Viajesterrestrescomponent],
      imports: [FormsModule],
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    fixture = TestBed.createComponent(Viajesterrestrescomponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
