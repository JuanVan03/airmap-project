import { Aeropuerto } from './aeropuerto';

export type EstadoVuelo =
  | 'PROGRAMADO'
  | 'EN_VUELO'
  | 'ATERRIZANDO'
  | 'ATERRIZADO'
  | 'CANCELADO';

// Lo que usa la tarjeta y la pagina de informacion: GET /api/vuelos/{id}
export interface Vuelo {
  id: string;
  numeroVuelo: string;
  aerolinea: string | null;
  estado: EstadoVuelo;
  latitud: number;
  longitud: number;
  rumbo: number;
  altitud: number;
  velocidad: number;
  origen: Aeropuerto | null;
  destino: Aeropuerto | null;
  horaSalida: string | null;
  horaLlegada: string | null;
}
