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
import { authGuard, invitadoGuard } from './guards/auth.guard';
import { Notfoundcomponent } from './components/principal/notfoundcomponent/notfoundcomponent';

const routes: Routes = [
  { path: '', component: Iniciocomponent },
  { path: 'inicio', component: Iniciocomponent },
  { path: 'login', component: Logincomponent, canActivate: [invitadoGuard] },
  { path: 'register', component: Registercomponent, canActivate: [invitadoGuard] },
  { path: 'vuelos', component: Vuelocomponent },
  { path: 'informacion', component: Informacioncomponent },
  { path: 'informacion/:id', component: Informacioncomponent },
  { path: 'viajes-terrestres', component: Viajesterrestrescomponent },
  { path: 'asistente', component: Asistentecomponent },
  { path: 'perfil', component: Perfilcomponent, canActivate: [authGuard] },
  { path: 'recuperar', component: Recuperarcomponent },
  // Cualquier otra direccion muestra la pagina 404
  { path: '**', component: Notfoundcomponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
