// Datos del usuario que inicio sesion (los devuelve el back)
export interface Usuario {
  nombre: string;
  correo: string;
  // LOCAL = correo y contrasena, GOOGLE = ingreso con Google (sin contrasena)
  proveedor: 'LOCAL' | 'GOOGLE';
  // ADMIN es el administrador (ve la auditoria). Las sesiones guardadas antes de este campo no lo traen.
  rol?: 'USUARIO' | 'ADMIN';
}
