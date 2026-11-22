import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Transporte } from '../../../models/transporte';
import { Infotransportecomponent } from './infotransportecomponent';

describe('Infotransportecomponent', () => {
  let component: Infotransportecomponent;
  let fixture: ComponentFixture<Infotransportecomponent>;

  const transporte: Transporte = {
    paradero: { id: '52340', nombre: 'Portal Eldorado', latitud: 4.6808, longitud: -74.1201, distanciaM: 172 },
    caminata: { distanciaKm: 0.2, duracionMin: 3, coordenadas: [] },
    rutas: [{ codigo: 'K321', nombre: 'Fontibon Brisas', bajada: 'Aeropuerto Eldorado' }],
    llegadas: [{ ruta: 'K321', minutos: 2, aproximado: true }],
    buses: [{ ruta: 'K321', latitud: 4.6839, longitud: -74.1149, distanciaM: 678 }],
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Infotransportecomponent],
    }).compileComponents();

    fixture = TestBed.createComponent(Infotransportecomponent);
    fixture.componentRef.setInput('transporte', transporte);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
