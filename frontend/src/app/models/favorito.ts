// Un vuelo marcado como favorito: GET /api/favoritos
export interface Favorito {
  id: number;
  // Id del vuelo, el mismo que usa /api/vuelos/{id}
  vueloId: string;
  numeroVuelo: string;
}
