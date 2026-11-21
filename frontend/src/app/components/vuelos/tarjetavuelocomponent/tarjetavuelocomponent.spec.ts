import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Vuelo } from '../../../models/vuelo';
import { Tarjetavuelocomponent } from './tarjetavuelocomponent';

describe('Tarjetavuelocomponent', () => {
  let component: Tarjetavuelocomponent;
  let fixture: ComponentFixture<Tarjetavuelocomponent>;

  // La tarjeta necesita un vuelo para dibujarse
  const vuelo: Vuelo = {
    id: 'e8021c',
    numeroVuelo: 'AV010',
    aerolinea: 'Avianca',
    estado: 'EN_VUELO',
    latitud: 4.71,
    longitud: -74.07,
    rumbo: 52,
    altitud: 10670,
    velocidad: 880,
    origen: null,
    destino: null,
    horaSalida: null,
    horaLlegada: null,
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Tarjetavuelocomponent],
    }).compileComponents();

    fixture = TestBed.createComponent(Tarjetavuelocomponent);
    fixture.componentRef.setInput('vuelo', vuelo);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
