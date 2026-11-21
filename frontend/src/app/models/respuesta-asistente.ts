import { Vuelo } from './vuelo';

// Lo que responde POST /api/asistente
export interface RespuestaAsistente {
  respuesta: string;
  vuelos: Vuelo[];
}
