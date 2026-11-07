import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { Iniciocomponent } from './components/principal/iniciocomponent/iniciocomponent';
import { Navbarcomponent } from './components/principal/navbarcomponent/navbarcomponent';
import { Footercomponent } from './components/principal/footercomponent/footercomponent';
import { Notfoundcomponent } from './components/principal/notfoundcomponent/notfoundcomponent';
import { Logincomponent } from './components/logincomponent/logincomponent';
import { Registercomponent } from './components/registercomponent/registercomponent';

@NgModule({
  declarations: [
    App,
    Iniciocomponent,
    Navbarcomponent,
    Footercomponent,
    Notfoundcomponent,
    Logincomponent,
    Registercomponent,
  ],
  imports: [BrowserModule, AppRoutingModule],
  providers: [provideBrowserGlobalErrorListeners()],
  bootstrap: [App],
})
export class AppModule {}
