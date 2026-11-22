import { Component, signal } from '@angular/core';
import { PreferenciasService } from './services/preferencias.service';

@Component({
  selector: 'app-root',
  standalone: false,
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App {
  protected readonly title = signal('airmap-project');

  constructor(private preferencias: PreferenciasService) {}
}
