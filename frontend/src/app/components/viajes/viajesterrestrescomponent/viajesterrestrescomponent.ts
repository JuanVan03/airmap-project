import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Aeropuerto } from '../../../models/aeropuerto';
import { ModoViaje, Ruta } from '../../../models/ruta';
import { Ubicacion } from '../../../models/ubicacion';
import { Vuelo } from '../../../models/vuelo';
import { AeropuertoService } from '../../../services/aeropuerto.service';
import { RutaService } from '../../../services/ruta.service';
import { VueloService } from '../../../services/vuelo.service';

@Component({
  selector: 'app-viajesterrestrescomponent',
  standalone: false,
  styleUrl: './viajesterrestrescomponent.css',
  templateUrl: './viajesterrestrescomponent.html',
})
export class Viajesterrestrescomponent implements OnInit {
  // Vuelo del que venimos (si el usuario llego desde un vuelo)
  vuelo = signal<Vuelo | null>(null);
  // Donde esta el usuario, segun el navegador
  ubicacion = signal<Ubicacion | null>(null);
  estadoUbicacion = signal('Detectando tu ubicación...');
  // Aeropuerto al que quiere llegar
  destino = signal<Aeropuerto | null>(null);
  // Aeropuertos que devuelve el buscador
  sugerencias = signal<Aeropuerto[]>([]);
  modo = signal<ModoViaje>('auto');
  ruta = signal<Ruta | null>(null);
  cargando = signal(false);
  // Texto de aviso o error
  mensaje = signal('');
  // Texto que escribe el usuario en el buscador de aeropuertos
  texto = '';

  constructor(
    private route: ActivatedRoute,
    private vueloService: VueloService,
    private aeropuertoService: AeropuertoService,
    private rutaService: RutaService,
  ) {}

  ngOnInit(): void {
    this.pedirUbicacion();

    // Si llegamos desde un vuelo, la URL trae su id: /viajes-terrestres?vuelo=ID
    const idVuelo = this.route.snapshot.queryParamMap.get('vuelo');
    if (idVuelo) {
      this.cargarVuelo(idVuelo);
    }
  }

  // Pide permiso al navegador para conocer la ubicacion
  pedirUbicacion(): void {
    if (!navigator.geolocation) {
      this.estadoUbicacion.set('Tu navegador no permite obtener la ubicación.');
      return;
    }

    this.estadoUbicacion.set('Detectando tu ubicación...');
    navigator.geolocation.getCurrentPosition(
      (posicion) => {
        this.ubicacion.set({ latitud: posicion.coords.latitude, longitud: posicion.coords.longitude });
        this.estadoUbicacion.set('Ubicación detectada');
        this.calcularRuta();
      },
      () => this.estadoUbicacion.set('No pudimos obtener tu ubicación. Revisa el permiso del navegador.'),
    );
  }

  private cargarVuelo(id: string): void {
    this.vueloService.getVuelo(id).subscribe({
      next: (vuelo) => {
        this.vuelo.set(vuelo);

        // Con destino conocido se usa solo; si no, el usuario lo elige
        if (vuelo.destino) {
          this.destino.set(vuelo.destino);
          this.calcularRuta();
        } else {
          this.mensaje.set('Este vuelo no tiene destino conocido. Busca el aeropuerto al que quieres llegar.');
        }
      },
      error: () => this.mensaje.set('No se pudo cargar el vuelo. Puedes elegir el aeropuerto a mano.'),
    });
  }

  buscarAeropuerto(): void {
    if (this.texto.trim() === '') {
      return;
    }

    this.aeropuertoService.buscar(this.texto.trim()).subscribe({
      next: (lista) => {
        this.sugerencias.set(lista);
        this.mensaje.set(lista.length === 0 ? 'No se encontró ningún aeropuerto con ese nombre.' : '');
      },
      error: () => this.mensaje.set('No se pudo conectar con el servidor.'),
    });
  }

  elegirAeropuerto(aeropuerto: Aeropuerto): void {
    this.destino.set(aeropuerto);
    this.sugerencias.set([]);
    this.texto = '';
    this.mensaje.set('');
    this.calcularRuta();
  }

  cambiarModo(modo: ModoViaje): void {
    this.modo.set(modo);
    this.calcularRuta();
  }

  // Pide la ruta cuando ya se conocen la ubicacion y el destino
  private calcularRuta(): void {
    const origen = this.ubicacion();
    const destino = this.destino();
    if (!origen || !destino) {
      return;
    }

    this.cargando.set(true);
    this.rutaService.getRuta(origen, destino.latitud, destino.longitud, this.modo()).subscribe({
      next: (ruta) => {
        this.ruta.set(ruta);
        this.mensaje.set('');
        this.cargando.set(false);
      },
      error: (e: HttpErrorResponse) => {
        this.ruta.set(null);
        // El back explica que paso en el campo mensaje
        this.mensaje.set(e.error?.mensaje ?? 'No se pudo calcular la ruta.');
        this.cargando.set(false);
      },
    });
  }

  // 95 min -> "1 h 35 min"
  formatoTiempo(minutos: number): string {
    if (minutos < 60) {
      return `${minutos} min`;
    }
    return `${Math.floor(minutos / 60)} h ${minutos % 60} min`;
  }
}
