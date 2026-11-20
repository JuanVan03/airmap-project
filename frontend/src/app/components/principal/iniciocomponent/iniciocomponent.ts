import { Component, OnInit, signal } from '@angular/core';
import { Vuelo } from '../../../models/vuelo';
import { VueloResumen } from '../../../models/vuelo-resumen';
import { VueloService } from '../../../services/vuelo.service';

@Component({
  selector: 'app-iniciocomponent',
  standalone: false,
  styleUrl: './iniciocomponent.css',
  templateUrl: './iniciocomponent.html',
})
export class Iniciocomponent implements OnInit {
  // Vuelos que dibuja el mapa
  vuelos = signal<VueloResumen[]>([]);
  // Vuelo que se muestra en la tarjeta
  vueloSeleccionado = signal<Vuelo | null>(null);
  // Texto de aviso o error
  mensaje = signal('');
  // Texto que escribe el usuario en el buscador
  texto = '';

  constructor(private vueloService: VueloService) {}

  ngOnInit(): void {
    this.vueloService.getVuelosActivos().subscribe({
      next: (respuesta) => this.vuelos.set(respuesta.vuelos),
      error: () => this.mensaje.set('No se pudo conectar con el servidor.'),
    });
  }

  // Se llama cuando el usuario hace clic en un punto del mapa
  seleccionar(id: string): void {
    this.vueloService.getVuelo(id).subscribe({
      next: (vuelo) => {
        this.vueloSeleccionado.set(vuelo);
        this.mensaje.set('');
      },
      error: () => this.mensaje.set('No se pudo cargar la información del vuelo.'),
    });
  }

  // Se llama cuando el usuario envia el buscador
  buscar(): void {
    if (this.texto.trim() === '') {
      return;
    }

    this.vueloService.buscar(this.texto.trim()).subscribe({
      next: (lista) => {
        if (lista.length > 0) {
          // Se pide el detalle del primer resultado para que traiga origen y destino
          this.seleccionar(lista[0].id);
        } else {
          this.mensaje.set('No se encontró ningún vuelo con ese número.');
        }
      },
      error: () => this.mensaje.set('No se pudo conectar con el servidor.'),
    });
  }
}
