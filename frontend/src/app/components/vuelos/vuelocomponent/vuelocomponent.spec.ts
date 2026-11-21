import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { Vuelocomponent } from './vuelocomponent';

describe('Vuelocomponent', () => {
  let component: Vuelocomponent;
  let fixture: ComponentFixture<Vuelocomponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Vuelocomponent],
      imports: [FormsModule],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    fixture = TestBed.createComponent(Vuelocomponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
