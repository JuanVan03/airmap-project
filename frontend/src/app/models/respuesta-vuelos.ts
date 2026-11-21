import { VueloResumen } from './vuelo-resumen';

// Respuesta completa de GET /api/vuelos
export interface RespuestaVuelos {
  actualizadoEn: string;
  total: number;
  vuelos: VueloResumen[];
}
