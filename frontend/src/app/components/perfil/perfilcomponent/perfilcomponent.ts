import { Component, signal } from '@angular/core';
import { Aeropuerto } from '../../../models/aeropuerto';
import { RutaGuardada } from '../../../models/ruta-guardada';
import { ModoViaje } from '../../../models/ruta';
import { Usuario } from '../../../models/usuario';
import { Vuelo } from '../../../models/vuelo';

type Pestana = 'favoritos' | 'historial' | 'rutas' | 'ajustes';

// ---------- DATOS DE EJEMPLO ----------
// Se borran cuando el back entregue los datos reales del usuario
const BOG: Aeropuerto = { codigo: 'BOG', nombre: 'El Dorado', ciudad: 'Bogotá', pais: 'Colombia', latitud: 4.70159, longitud: -74.1469 };
const MAD: Aeropuerto = { codigo: 'MAD', nombre: 'Barajas', ciudad: 'Madrid', pais: 'España', latitud: 40.4936, longitud: -3.56676 };
const LIM: Aeropuerto = { codigo: 'LIM', nombre: 'Jorge Chávez', ciudad: 'Lima', pais: 'Perú', latitud: -12.0219, longitud: -77.1143 };
const SCL: Aeropuerto = { codigo: 'SCL', nombre: 'Arturo Merino Benítez', ciudad: 'Santiago', pais: 'Chile', latitud: -33.393, longitud: -70.7858 };

const FAVORITOS_EJEMPLO: Vuelo[] = [
  {
    id: 'ejemplo1', numeroVuelo: 'AV010', aerolinea: 'Avianca', estado: 'EN_VUELO',
    latitud: 25, longitud: -50, rumbo: 52, altitud: 10670, velocidad: 880,
    origen: BOG, destino: MAD, horaSalida: '2026-11-20T08:35:00', horaLlegada: '2026-11-20T22:10:00',
  },
  {
    id: 'ejemplo2', numeroVuelo: 'LA533', aerolinea: 'LATAM', estado: 'EN_VUELO',
    latitud: -20, longitud: -75, rumbo: 170, altitud: 10670, velocidad: 880,
    origen: LIM, destino: SCL, horaSalida: '2026-11-20T13:00:00', horaLlegada: '2026-11-20T15:45:00',
  },
  {
    id: 'ejemplo3', numeroVuelo: 'IB6830', aerolinea: 'Iberia', estado: 'ATERRIZANDO',
    latitud: 5, longitud: -74, rumbo: 250, altitud: 10670, velocidad: 880,
    origen: MAD, destino: BOG, horaSalida: '2026-11-20T02:00:00', horaLlegada: '2026-11-20T14:20:00',
  },
];

const RUTAS_EJEMPLO: RutaGuardada[] = [
  { id: 1, nombre: 'Casa → Aeropuerto El Dorado', duracionMin: 32, distanciaKm: 24, modo: 'auto' },
  { id: 2, nombre: 'Oficina → Aeropuerto El Dorado', duracionMin: 48, distanciaKm: 27, modo: 'auto' },
  { id: 3, nombre: 'Casa → Aeropuerto El Dorado', duracionMin: 95, distanciaKm: 24, modo: 'bici' },
];

@Component({
  selector: 'app-perfilcomponent',
  standalone: false,
  styleUrl: './perfilcomponent.css',
  templateUrl: './perfilcomponent.html',
})
export class Perfilcomponent {
  // Pestana que se esta viendo
  pestana = signal<Pestana>('favoritos');

  usuario = signal<Usuario>({ nombre: 'Tu nombre', correo: 'tu@correo.com' });
  favoritos = signal<Vuelo[]>(FAVORITOS_EJEMPLO);
  historial = signal<Vuelo[]>([]);
  rutasGuardadas = signal<RutaGuardada[]>(RUTAS_EJEMPLO);

  cambiarPestana(pestana: Pestana): void {
    this.pestana.set(pestana);
  }

  // "Juan Pérez" -> "JP"
  iniciales(): string {
    return this.usuario()
      .nombre.split(' ')
      .filter((parte) => parte !== '')
      .slice(0, 2)
      .map((parte) => parte[0])
      .join('')
      .toUpperCase();
  }

  // 95 min -> "1 h 35 min"
  formatoTiempo(minutos: number): string {
    if (minutos < 60) {
      return `${minutos} min`;
    }
    return `${Math.floor(minutos / 60)} h ${minutos % 60} min`;
  }

  textoModo(modo: ModoViaje): string {
    if (modo === 'pie') {
      return 'a pie';
    }
    return modo === 'bici' ? 'en bici' : 'en auto';
  }
}
