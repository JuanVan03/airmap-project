// Un renglon de la auditoria (lo que pasó, quién lo hizo y cuándo)
export interface Auditoria {
  id: number;
  // Fecha y hora en formato ISO, por ejemplo 2026-11-21T15:30:00
  fecha: string;
  usuario: string;
  // Código de la acción: LOGIN_EXITOSO, CAMBIO_CONTRASENA...
  accion: string;
  resultado: 'EXITO' | 'FALLO';
  detalle: string | null;
  ip: string | null;
  navegador: string | null;
}

// Una pagina de resultados
export interface PaginaAuditoria {
  contenido: Auditoria[];
  pagina: number;
  tamano: number;
  totalElementos: number;
  totalPaginas: number;
}

// Filtros que escribe el administrador (vacío = no filtrar)
export interface FiltrosAuditoria {
  usuario: string;
  accion: string;
  resultado: string;
  // Fechas en formato yyyy-MM-dd
  desde: string;
  hasta: string;
}
