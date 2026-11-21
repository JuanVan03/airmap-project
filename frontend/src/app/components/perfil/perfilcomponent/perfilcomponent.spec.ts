import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { Perfilcomponent } from './perfilcomponent';

describe('Perfilcomponent', () => {
  let component: Perfilcomponent;
  let fixture: ComponentFixture<Perfilcomponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Perfilcomponent],
      providers: [provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(Perfilcomponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
