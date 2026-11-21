// Medios de transporte que calcula el back
export type ModoViaje = 'auto' | 'pie' | 'bici';

// Lo que responde GET /api/rutas
export interface Ruta {
  distanciaKm: number;
  duracionMin: number;
  // Puntos de la ruta como [latitud, longitud]
  coordenadas: number[][];
}
