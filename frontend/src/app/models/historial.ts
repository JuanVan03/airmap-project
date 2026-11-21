// Un vuelo que el usuario abrió: GET /api/historial
export interface HistorialVuelo {
  // Id del vuelo, el mismo que usa /api/vuelos/{id}
  vueloId: string;
  numeroVuelo: string;
  // Última vez que lo abrió, en formato ISO (2026-11-21T15:30:00)
  fechaVista: string;
}
