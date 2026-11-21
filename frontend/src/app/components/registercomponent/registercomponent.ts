import { Component, OnInit, signal } from '@angular/core';
import { VueloResumen } from '../../models/vuelo-resumen';
import { VueloService } from '../../services/vuelo.service';

@Component({
  selector: 'app-registercomponent',
  standalone: false,
  styleUrl: './registercomponent.css',
  templateUrl: './registercomponent.html',
})
export class Registercomponent implements OnInit {
  // Vuelos que dibuja el mapa del panel izquierdo
  vuelos = signal<VueloResumen[]>([]);

  constructor(private vueloService: VueloService) {}

  ngOnInit(): void {
    this.vueloService.getVuelosActivos().subscribe({
      next: (respuesta) => this.vuelos.set(respuesta.vuelos),
      // El mapa es decorativo: si falla, se ve vacio y el formulario sigue funcionando
      error: () => {},
    });
  }
}
