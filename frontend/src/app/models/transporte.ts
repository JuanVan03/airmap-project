import { Ruta } from './ruta';

export interface Paradero {
  id: string;
  nombre: string;
  latitud: number;
  longitud: number;
  distanciaM: number;
}


export interface RutaBus {
  codigo: string;
  nombre: string;
  bajada: string;
}

export interface Llegada {
  ruta: string;
  minutos: number;
  aproximado: boolean;
}

export interface Bus {
  ruta: string;
  latitud: number;
  longitud: number;
  distanciaM: number;
}

export interface TiempoReal {
  llegadas: Llegada[];
  buses: Bus[];
}

export interface Transporte extends TiempoReal {
  paradero: Paradero;
  caminata: Ruta | null;
  rutas: RutaBus[];
}
