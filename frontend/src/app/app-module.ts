import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { Iniciocomponent } from './components/principal/iniciocomponent/iniciocomponent';
import { Navbarcomponent } from './components/principal/navbarcomponent/navbarcomponent';
import { Footercomponent } from './components/principal/footercomponent/footercomponent';
import { Notfoundcomponent } from './components/principal/notfoundcomponent/notfoundcomponent';
import { Logincomponent } from './components/logincomponent/logincomponent';
import { Registercomponent } from './components/registercomponent/registercomponent';
import { Tarjetavuelocomponent } from './components/vuelos/tarjetavuelocomponent/tarjetavuelocomponent';
import { Mapacomponent } from './components/vuelos/mapacomponent/mapacomponent';
import { Vuelocomponent } from './components/vuelos/vuelocomponent/vuelocomponent';
import { Informacioncomponent } from './components/vuelos/informacioncomponent/informacioncomponent';
import { Viajesterrestrescomponent } from './components/viajes/viajesterrestrescomponent/viajesterrestrescomponent';
import { Maparutacomponent } from './components/viajes/maparutacomponent/maparutacomponent';
import { Perfilcomponent } from './components/perfil/perfilcomponent/perfilcomponent';
import { Recuperarcomponent } from './components/recuperarcomponent/recuperarcomponent';
import { Admincomponent } from './components/admincomponent/admincomponent';
import { Restablecercomponent } from './components/restablecercomponent/restablecercomponent';
import { Verificarcomponent } from './components/verificarcomponent/verificarcomponent';
import { authInterceptor } from './interceptors/auth.interceptor';
import { Favoritocomponent } from './components/vuelos/favoritocomponent/favoritocomponent';
import { Asistentecomponent } from './components/asistente/asistentecomponent/asistentecomponent';

@NgModule({
  declarations: [
    App,
    Iniciocomponent,
    Navbarcomponent,
    Footercomponent,
    Notfoundcomponent,
    Logincomponent,
    Registercomponent,
    Tarjetavuelocomponent,
    Mapacomponent,
    Vuelocomponent,
    Informacioncomponent,
    Viajesterrestrescomponent,
    Maparutacomponent,
    Asistentecomponent,
    Perfilcomponent,
    Recuperarcomponent,
    Verificarcomponent,
    Restablecercomponent,
    Admincomponent,
    Favoritocomponent,
  ],
  imports: [BrowserModule, FormsModule, AppRoutingModule],
  providers: [provideBrowserGlobalErrorListeners(), provideHttpClient(withInterceptors([authInterceptor]))],
  bootstrap: [App],
})
export class AppModule {}
