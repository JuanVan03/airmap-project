import { Usuario } from './usuario';

// Respuesta de /api/auth/registro, /api/auth/login y /api/auth/google
export interface AuthRespuesta {
  token: string;
  usuario: Usuario;
}
