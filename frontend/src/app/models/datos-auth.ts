// Lo que se envia al back al crear una cuenta
export interface DatosRegistro {
  nombre: string;
  correo: string;
  contrasena: string;
}

// Lo que se envia al back al ingresar
export interface DatosLogin {
  correo: string;
  contrasena: string;
}
