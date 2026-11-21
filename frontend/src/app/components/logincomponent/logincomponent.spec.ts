import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { provideRouter } from '@angular/router';
import { Logincomponent } from './logincomponent';

describe('Logincomponent', () => {
  let component: Logincomponent;
  let fixture: ComponentFixture<Logincomponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Logincomponent],
      imports: [FormsModule],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(Logincomponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
