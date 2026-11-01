import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Iniciocomponent } from './components/principal/iniciocomponent/iniciocomponent';
import {Logincomponent} from './components/logincomponent/logincomponent';
import {Registercomponent} from './components/registercomponent/registercomponent';

const routes: Routes = [
  { path: 'inicio', component: Iniciocomponent },
  { path: 'login', component: Logincomponent },
  { path: 'register', component: Registercomponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
