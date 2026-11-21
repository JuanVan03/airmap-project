import { ModoViaje } from './ruta';

// Una ruta terrestre que el usuario guardo: GET /api/rutas-guardadas
export interface RutaGuardada {
  id: number;
  nombre: string;
  // Desde donde salia el usuario
  origenLat: number;
  origenLon: number;
  // Aeropuerto de destino
  destinoCodigo: string;
  destinoNombre: string;
  destinoLat: number;
  destinoLon: number;
  modo: ModoViaje;
  // Resultado de cuando se guardo
  distanciaKm: number;
  duracionMin: number;
}
