import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Iniciocomponent } from './components/principal/iniciocomponent/iniciocomponent';
import {Logincomponent} from './components/logincomponent/logincomponent';
import {Registercomponent} from './components/registercomponent/registercomponent';
import { Vuelocomponent } from './components/vuelos/vuelocomponent/vuelocomponent';
import { Informacioncomponent } from './components/vuelos/informacioncomponent/informacioncomponent';
import { Viajesterrestrescomponent } from './components/viajes/viajesterrestrescomponent/viajesterrestrescomponent';
import { Asistentecomponent } from './components/asistente/asistentecomponent/asistentecomponent';
import { Perfilcomponent } from './components/perfil/perfilcomponent/perfilcomponent';
import { Recuperarcomponent } from './components/recuperarcomponent/recuperarcomponent';
import { Restablecercomponent } from './components/restablecercomponent/restablecercomponent';
import { Verificarcomponent } from './components/verificarcomponent/verificarcomponent';
import { Admincomponent } from './components/admincomponent/admincomponent';
import { adminGuard, authGuard, invitadoGuard } from './guards/auth.guard';
import { Notfoundcomponent } from './components/principal/notfoundcomponent/notfoundcomponent';

const routes: Routes = [
  { path: '', component: Iniciocomponent },
  { path: 'inicio', component: Iniciocomponent },
  { path: 'login', component: Logincomponent, canActivate: [invitadoGuard] },
  { path: 'register', component: Registercomponent, canActivate: [invitadoGuard] },
  { path: 'vuelos', component: Vuelocomponent },
  { path: 'informacion', component: Informacioncomponent },
  { path: 'informacion/:id', component: Informacioncomponent },
  // Calcular rutas pide sesion: sin ella manda al login y despues vuelve aqui
  { path: 'viajes-terrestres', component: Viajesterrestrescomponent, canActivate: [authGuard] },
  { path: 'asistente', component: Asistentecomponent, canActivate: [authGuard] },
  { path: 'perfil', component: Perfilcomponent, canActivate: [authGuard] },
  // Panel del administrador (auditoria): solo con el rol ADMIN
  { path: 'admin', component: Admincomponent, canActivate: [adminGuard] },
  { path: 'recuperar', component: Recuperarcomponent },
  // Enlace que llega por correo para crear una contraseña nueva
  { path: 'restablecer', component: Restablecercomponent },
  // Enlace que llega por correo para confirmar la cuenta
  { path: 'verificar', component: Verificarcomponent },
  // Cualquier otra direccion muestra la pagina 404
  { path: '**', component: Notfoundcomponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
