import { Vuelo } from './vuelo';

// Un mensaje de la conversacion con el asistente
export interface MensajeChat {
  rol: 'usuario' | 'asistente';
  texto: string;
  // Vuelos que el asistente quiere mostrar junto a su respuesta
  vuelos?: Vuelo[];
}
