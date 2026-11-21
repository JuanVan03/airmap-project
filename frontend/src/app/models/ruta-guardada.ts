import { ModoViaje } from './ruta';

// Una ruta terrestre que el usuario guardo
export interface RutaGuardada {
  id: number;
  nombre: string;
  duracionMin: number;
  distanciaKm: number;
  modo: ModoViaje;
}
