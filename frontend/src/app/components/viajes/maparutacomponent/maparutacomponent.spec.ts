import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Maparutacomponent } from './maparutacomponent';

describe('Maparutacomponent', () => {
  let component: Maparutacomponent;
  let fixture: ComponentFixture<Maparutacomponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Maparutacomponent],
    }).compileComponents();

    fixture = TestBed.createComponent(Maparutacomponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
