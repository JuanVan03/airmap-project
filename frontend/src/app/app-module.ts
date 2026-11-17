import { provideHttpClient } from '@angular/common/http';
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
  ],
  imports: [BrowserModule, FormsModule, AppRoutingModule],
  providers: [provideBrowserGlobalErrorListeners(), provideHttpClient()],
  bootstrap: [App],
})
export class AppModule {}
