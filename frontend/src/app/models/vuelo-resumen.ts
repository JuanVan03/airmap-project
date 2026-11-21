// Lo que usa el mapa: GET /api/vuelos
export interface VueloResumen {
  id: string;
  numeroVuelo: string;
  latitud: number;
  longitud: number;
  rumbo: number;
  enTierra: boolean;
}
