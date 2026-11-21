import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, computed, effect, signal, untracked } from '@angular/core';
import { Favorito } from '../../../models/favorito';
import { HistorialVuelo } from '../../../models/historial';
import { ModoViaje } from '../../../models/ruta';
import { RutaGuardada } from '../../../models/ruta-guardada';
import { Vuelo } from '../../../models/vuelo';
import { AuthService } from '../../../services/auth.service';
import { FavoritoService } from '../../../services/favorito.service';
import { HistorialService } from '../../../services/historial.service';
import { RutaGuardadaService } from '../../../services/ruta-guardada.service';
import { VueloService } from '../../../services/vuelo.service';

type Pestana = 'favoritos' | 'historial' | 'rutas' | 'ajustes';

@Component({
  selector: 'app-perfilcomponent',
  standalone: false,
  styleUrl: './perfilcomponent.css',
  templateUrl: './perfilcomponent.html',
})
export class Perfilcomponent implements OnInit {
  // Pestana que se esta viendo
  pestana = signal<Pestana>('favoritos');

  // Usuario que inicio sesion (el guard evita llegar aqui sin sesion)
  usuario = computed(() => this.auth.usuario() ?? { nombre: '', correo: '', proveedor: 'LOCAL' as const });

  // Ajustes: nombre que se esta editando y estado del guardado
  nombreEditado = '';
  guardandoAjustes = signal(false);
  exitoAjustes = signal(false);
  errorAjustes = signal('');

  // Favoritos: la lista sale del FavoritoService
  favoritos = computed(() => this.favoritosServ.favoritos());
  // Datos en vivo de cada favorito: el Vuelo si sigue en el aire, null si ya no, y nada mientras carga
  vuelosVivos = signal<Record<string, Vuelo | null>>({});
  private pidiendo = new Set<string>();

  // Rutas guardadas
  rutasGuardadas = signal<RutaGuardada[]>([]);
  cargandoRutas = signal(true);
  errorRutas = signal('');

  // Historial de vuelos abiertos
  historial = signal<HistorialVuelo[]>([]);
  cargandoHistorial = signal(true);
  errorHistorial = signal('');

  constructor(
    private auth: AuthService,
    private favoritosServ: FavoritoService,
    private rutaGuardadaServ: RutaGuardadaService,
    private historialServ: HistorialService,
    private vueloService: VueloService,
  ) {
    // Cada vez que cambia la lista de favoritos se piden los datos en vivo de los nuevos
    effect(() => {
      const lista = this.favoritosServ.favoritos();
      untracked(() => lista.forEach((favorito) => this.cargarVuelo(favorito.vueloId)));
    });
  }

  ngOnInit(): void {
    this.historialServ.listar().subscribe({
      next: (lista) => {
        this.historial.set(lista);
        this.cargandoHistorial.set(false);
      },
      error: () => {
        this.errorHistorial.set('No se pudo cargar tu historial.');
        this.cargandoHistorial.set(false);
      },
    });

    this.rutaGuardadaServ.listar().subscribe({
      next: (lista) => {
        this.rutasGuardadas.set(lista);
        this.cargandoRutas.set(false);
      },
      error: () => {
        this.errorRutas.set('No se pudieron cargar tus rutas guardadas.');
        this.cargandoRutas.set(false);
      },
    });
  }

  private cargarVuelo(id: string): void {
    if (this.pidiendo.has(id)) {
      return;
    }
    this.pidiendo.add(id);
    this.vueloService.getVuelo(id).subscribe({
      next: (vuelo) => this.vuelosVivos.update((datos) => ({ ...datos, [id]: vuelo })),
      error: () => this.vuelosVivos.update((datos) => ({ ...datos, [id]: null })),
    });
  }

  // Vuelo en vivo del favorito: Vuelo, null (ya no esta en el aire) o undefined (cargando)
  vueloDe(favorito: Favorito): Vuelo | null | undefined {
    return this.vuelosVivos()[favorito.vueloId];
  }

  quitarFavorito(favorito: Favorito): void {
    this.favoritosServ.quitar(favorito.vueloId).subscribe({ error: () => {} });
  }

  eliminarRuta(ruta: RutaGuardada): void {
    this.errorRutas.set('');
    this.rutaGuardadaServ.eliminar(ruta.id).subscribe({
      next: () => this.rutasGuardadas.update((lista) => lista.filter((r) => r.id !== ruta.id)),
      error: () => this.errorRutas.set('No se pudo eliminar la ruta.'),
    });
  }

  quitarDelHistorial(vuelo: HistorialVuelo): void {
    this.errorHistorial.set('');
    this.historialServ.quitar(vuelo.vueloId).subscribe({
      next: () => this.historial.update((lista) => lista.filter((v) => v.vueloId !== vuelo.vueloId)),
      error: () => this.errorHistorial.set('No se pudo quitar el vuelo del historial.'),
    });
  }

  vaciarHistorial(): void {
    this.errorHistorial.set('');
    this.historialServ.vaciar().subscribe({
      next: () => this.historial.set([]),
      error: () => this.errorHistorial.set('No se pudo borrar el historial.'),
    });
  }

  cerrarSesion(): void {
    this.auth.cerrarSesion();
  }

  cambiarPestana(pestana: Pestana): void {
    // Al abrir Ajustes el campo arranca con el nombre actual
    if (pestana === 'ajustes') {
      this.nombreEditado = this.usuario().nombre;
      this.exitoAjustes.set(false);
      this.errorAjustes.set('');
    }
    this.pestana.set(pestana);
  }

  guardarAjustes(): void {
    this.exitoAjustes.set(false);
    this.errorAjustes.set('');

    const nombre = this.nombreEditado.trim();
    if (nombre === '') {
      this.errorAjustes.set('Ingresa tu nombre.');
      return;
    }
    if (nombre.length > 100) {
      this.errorAjustes.set('El nombre es muy largo.');
      return;
    }

    this.guardandoAjustes.set(true);
    this.auth.actualizarPerfil(nombre).subscribe({
      next: (usuario) => {
        this.nombreEditado = usuario.nombre;
        this.exitoAjustes.set(true);
        this.guardandoAjustes.set(false);
      },
      error: (e: HttpErrorResponse) => {
        this.errorAjustes.set(this.auth.mensajeDeError(e));
        this.guardandoAjustes.set(false);
      },
    });
  }

  // "Juan Pérez" -> "JP"
  iniciales(): string {
    return this.usuario()
      .nombre.split(' ')
      .filter((parte) => parte !== '')
      .slice(0, 2)
      .map((parte) => parte[0])
      .join('')
      .toUpperCase();
  }

  // 95 min -> "1 h 35 min"
  formatoTiempo(minutos: number): string {
    if (minutos < 60) {
      return `${minutos} min`;
    }
    return `${Math.floor(minutos / 60)} h ${minutos % 60} min`;
  }

  textoModo(modo: ModoViaje): string {
    if (modo === 'pie') {
      return 'a pie';
    }
    return modo === 'bici' ? 'en bici' : 'en auto';
  }
}
