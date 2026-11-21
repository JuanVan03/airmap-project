// Datos del usuario que inicio sesion (los devuelve el back)
export interface Usuario {
  nombre: string;
  correo: string;
  // LOCAL = correo y contrasena, GOOGLE = ingreso con Google (sin contrasena)
  proveedor: 'LOCAL' | 'GOOGLE';
}
