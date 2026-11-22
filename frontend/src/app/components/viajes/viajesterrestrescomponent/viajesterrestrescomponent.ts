import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Aeropuerto } from '../../../models/aeropuerto';
import { ModoViaje, Ruta } from '../../../models/ruta';
import { Transporte } from '../../../models/transporte';
import { Ubicacion } from '../../../models/ubicacion';
import { Vuelo } from '../../../models/vuelo';
import { AeropuertoService } from '../../../services/aeropuerto.service';
import { AuthService } from '../../../services/auth.service';
import { RutaGuardadaService } from '../../../services/ruta-guardada.service';
import { RutaService } from '../../../services/ruta.service';
import { TransporteService } from '../../../services/transporte.service';
import { VueloService } from '../../../services/vuelo.service';

@Component({
  selector: 'app-viajesterrestrescomponent',
  standalone: false,
  styleUrl: './viajesterrestrescomponent.css',
  templateUrl: './viajesterrestrescomponent.html',
})
export class Viajesterrestrescomponent implements OnInit, OnDestroy {

  vuelo = signal<Vuelo | null>(null);

  ubicacion = signal<Ubicacion | null>(null);
  estadoUbicacion = signal('Detectando tu ubicación...');

  destino = signal<Aeropuerto | null>(null);
  sugerencias = signal<Aeropuerto[]>([]);
  modo = signal<ModoViaje>('auto');
  ruta = signal<Ruta | null>(null);
  cargando = signal(false);
  mensaje = signal('');
  texto = '';
  origenGuardado = signal(false);
  guardando = signal(false);
  rutaGuardada = signal(false);
  nombreRuta = '';
  transmilenio = signal(false);
  transporte = signal<Transporte | null>(null);
  private temporizador?: ReturnType<typeof setInterval>;

  constructor(
    private route: ActivatedRoute,
    private vueloService: VueloService,
    private aeropuertoService: AeropuertoService,
    private rutaService: RutaService,
    private transporteService: TransporteService,
    private rutaGuardadaService: RutaGuardadaService,
    private auth: AuthService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    const params = this.route.snapshot.queryParamMap;
    const origenLat = Number(params.get('origenLat'));
    const origenLon = Number(params.get('origenLon'));
    const codigo = params.get('destino');
    if (params.get('origenLat') && params.get('origenLon') && codigo && Number.isFinite(origenLat) && Number.isFinite(origenLon)) {
      this.abrirRutaGuardada({ latitud: origenLat, longitud: origenLon }, codigo, params.get('modo'));
      return;
    }

    this.pedirUbicacion();

    const idVuelo = params.get('vuelo');
    if (idVuelo) {
      this.cargarVuelo(idVuelo);
    }
  }

  ngOnDestroy(): void {
    this.detenerTiempoReal();
  }

  private abrirRutaGuardada(origen: Ubicacion, codigo: string, modo: string | null): void {
    this.ubicacion.set(origen);
    this.origenGuardado.set(true);
    this.estadoUbicacion.set('Ubicación de tu ruta guardada');
    if (modo === 'auto' || modo === 'pie' || modo === 'bici') {
      this.modo.set(modo);
    }

    this.aeropuertoService.buscar(codigo).subscribe({
      next: (lista) => {
        const aeropuerto = lista.find((a) => a.codigo === codigo.toUpperCase());
        if (aeropuerto) {
          this.destino.set(aeropuerto);
          this.calcularRuta();
        } else {
          this.mensaje.set('No se encontró el aeropuerto de esta ruta. Elige uno a mano.');
        }
      },
      error: () => this.mensaje.set('No se pudo conectar con el servidor.'),
    });
  }

  pedirUbicacion(): void {
    if (!navigator.geolocation) {
      this.estadoUbicacion.set('Tu navegador no permite obtener la ubicación.');
      return;
    }

    this.estadoUbicacion.set('Detectando tu ubicación...');
    navigator.geolocation.getCurrentPosition(
      (posicion) => {
        this.ubicacion.set({ latitud: posicion.coords.latitude, longitud: posicion.coords.longitude });
        this.origenGuardado.set(false);
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
    this.transmilenio.set(false);
    this.modo.set(modo);
    this.calcularRuta();
  }

  usarTransmilenio(): void {
    this.transmilenio.set(true);
    this.calcularRuta();
  }

  private calcularRuta(): void {
    const origen = this.ubicacion();
    const destino = this.destino();
    if (!origen || !destino) {
      return;
    }

    if (this.transmilenio() && destino.codigo === 'BOG') {
      this.calcularTransporte(origen);
      return;
    }
    this.transmilenio.set(false);
    this.detenerTiempoReal();
    this.transporte.set(null);

    this.cargando.set(true);
    this.guardando.set(false);
    this.rutaGuardada.set(false);
    this.rutaService.getRuta(origen, destino.latitud, destino.longitud, this.modo()).subscribe({
      next: (ruta) => {
        this.ruta.set(ruta);
        this.mensaje.set('');
        this.cargando.set(false);
      },
      error: (e: HttpErrorResponse) => {
        this.ruta.set(null);
        this.mensaje.set(e.error?.mensaje ?? 'No se pudo calcular la ruta.');
        this.cargando.set(false);
      },
    });
  }

  private calcularTransporte(origen: Ubicacion): void {
    this.detenerTiempoReal();
    this.ruta.set(null);
    this.guardando.set(false);
    this.cargando.set(true);
    this.transporteService.getTransporte(origen).subscribe({
      next: (transporte) => {
        this.transporte.set(transporte);
        this.mensaje.set('');
        this.cargando.set(false);
        this.temporizador = setInterval(() => this.actualizarBuses(), 30000);
      },
      error: (e: HttpErrorResponse) => {
        this.transporte.set(null);
        this.mensaje.set(e.error?.mensaje ?? 'No se pudo consultar TransMilenio.');
        this.cargando.set(false);
      },
    });
  }

  private actualizarBuses(): void {
    const actual = this.transporte();
    if (!actual) {
      return;
    }
    this.transporteService.getTiempoReal(actual.paradero.id).subscribe({
      next: (vivo) => this.transporte.set({ ...actual, llegadas: vivo.llegadas, buses: vivo.buses }),
      error: () => {},
    });
  }

  private detenerTiempoReal(): void {
    if (this.temporizador) {
      clearInterval(this.temporizador);
      this.temporizador = undefined;
    }
  }

  abrirGuardado(): void {
    if (!this.auth.haySesion()) {
      this.router.navigate(['/login'], { queryParams: { redirigir: this.enlaceDeEstaRuta() } });
      return;
    }
    this.nombreRuta = `Mi ubicación → ${this.destino()?.nombre ?? ''}`.slice(0, 100);
    this.guardando.set(true);
  }

  confirmarGuardado(): void {
    const origen = this.ubicacion();
    const destino = this.destino();
    const ruta = this.ruta();
    const nombre = this.nombreRuta.trim();
    if (!origen || !destino || !ruta || nombre === '') {
      this.mensaje.set('Escribe un nombre para la ruta.');
      return;
    }

    this.rutaGuardadaService
      .guardar({
        nombre,
        origenLat: origen.latitud,
        origenLon: origen.longitud,
        destinoCodigo: destino.codigo,
        destinoNombre: destino.nombre,
        destinoLat: destino.latitud,
        destinoLon: destino.longitud,
        modo: this.modo(),
        distanciaKm: ruta.distanciaKm,
        duracionMin: ruta.duracionMin,
      })
      .subscribe({
        next: () => {
          this.guardando.set(false);
          this.rutaGuardada.set(true);
        },
        error: (e: HttpErrorResponse) => this.mensaje.set(e.error?.mensaje ?? 'No se pudo guardar la ruta.'),
      });
  }

  private enlaceDeEstaRuta(): string {
    const origen = this.ubicacion();
    const destino = this.destino();
    if (!origen || !destino) {
      return this.router.url;
    }
    return this.router
      .createUrlTree(['/viajes-terrestres'], {
        queryParams: { origenLat: origen.latitud, origenLon: origen.longitud, destino: destino.codigo, modo: this.modo() },
      })
      .toString();
  }

  formatoTiempo(minutos: number): string {
    if (minutos < 60) {
      return `${minutos} min`;
    }
    return `${Math.floor(minutos / 60)} h ${minutos % 60} min`;
  }
}
