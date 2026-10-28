import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { Iniciocomponent } from './iniciocomponent/iniciocomponent';
import { Navbarcomponent } from './navbarcomponent/navbarcomponent';
import { Footercomponent } from './footercomponent/footercomponent';
import { Notfoundcomponent } from './notfoundcomponent/notfoundcomponent';

@NgModule({
  declarations: [App, Iniciocomponent, Navbarcomponent, Footercomponent, Notfoundcomponent],
  imports: [BrowserModule, AppRoutingModule],
  providers: [provideBrowserGlobalErrorListeners()],
  bootstrap: [App],
})
export class AppModule {}
