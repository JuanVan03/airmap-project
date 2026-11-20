import { Component, OnInit, computed, signal } from '@angular/core';
import { Vuelo } from '../../../models/vuelo';
import { VueloResumen } from '../../../models/vuelo-resumen';
import { VueloService } from '../../../services/vuelo.service';

@Component({
  selector: 'app-vuelocomponent',
  standalone: false,
  styleUrl: './vuelocomponent.css',
  templateUrl: './vuelocomponent.html',
})
export class Vuelocomponent implements OnInit {
  // Vuelos que dibuja el mapa
  vuelos = signal<VueloResumen[]>([]);
  // Resultados del buscador
  resultados = signal<Vuelo[]>([]);
  // Vuelo que se muestra en la tarjeta del mapa
  vueloSeleccionado = signal<Vuelo | null>(null);
  // Texto de aviso o error
  mensaje = signal('');
  // Texto que escribe el usuario en el buscador
  texto = '';

  // Puntos del mapa: si el vuelo buscado no esta entre ellos lo agregamos
  puntos = computed(() => {
    const lista = this.vuelos();
    const vuelo = this.vueloSeleccionado();

    if (vuelo && !lista.some((v) => v.id === vuelo.id)) {
      return [
        ...lista,
        {
          id: vuelo.id,
          numeroVuelo: vuelo.numeroVuelo,
          latitud: vuelo.latitud,
          longitud: vuelo.longitud,
          rumbo: vuelo.rumbo,
          enTierra: false,
        },
      ];
    }
    return lista;
  });

  constructor(private vueloService: VueloService) {}

  ngOnInit(): void {
    this.vueloService.getVuelosActivos().subscribe({
      next: (respuesta) => this.vuelos.set(respuesta.vuelos),
      error: () => this.mensaje.set('No se pudo conectar con el servidor.'),
    });
  }

  // Se llama al hacer clic en un punto del mapa o en un resultado
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
        this.resultados.set(lista);
        this.mensaje.set(lista.length === 0 ? 'No se encontró ningún vuelo con ese indicativo.' : '');
      },
      error: () => this.mensaje.set('No se pudo conectar con el servidor.'),
    });
  }
}
