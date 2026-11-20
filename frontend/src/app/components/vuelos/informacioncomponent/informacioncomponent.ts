import { Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Vuelo } from '../../../models/vuelo';
import { VueloResumen } from '../../../models/vuelo-resumen';
import { VueloService } from '../../../services/vuelo.service';

// Datos de la barra de progreso del vuelo
interface Progreso {
  recorridos: number;
  restantes: number;
  porcentaje: number;
  tiempo: string;
}

@Component({
  selector: 'app-informacioncomponent',
  standalone: false,
  styleUrl: './informacioncomponent.css',
  templateUrl: './informacioncomponent.html',
})
export class Informacioncomponent implements OnInit {
  // Vuelo que se muestra en la pagina
  vuelo = signal<Vuelo | null>(null);
  // Punto del mapa pequeño (solo este vuelo)
  puntos = signal<VueloResumen[]>([]);
  // Solo se calcula si se conocen el origen y el destino
  progreso = signal<Progreso | null>(null);
  // Texto de aviso o error
  mensaje = signal('');

  constructor(
    private route: ActivatedRoute,
    private vueloService: VueloService,
  ) {}

  ngOnInit(): void {
    // El id llega en la URL: /informacion/:id
    const id = this.route.snapshot.paramMap.get('id');

    if (!id) {
      this.mensaje.set('Elige un vuelo en el mapa para ver su información.');
      return;
    }

    this.vueloService.getVuelo(id).subscribe({
      next: (vuelo) => {
        this.vuelo.set(vuelo);
        this.puntos.set([
          {
            id: vuelo.id,
            numeroVuelo: vuelo.numeroVuelo,
            latitud: vuelo.latitud,
            longitud: vuelo.longitud,
            rumbo: vuelo.rumbo,
            enTierra: false,
          },
        ]);
        this.calcularProgreso(vuelo);
      },
      error: () => this.mensaje.set('No se pudo cargar el vuelo. Puede que ya no esté en el aire.'),
    });
  }

  // Convierte el rumbo en grados a un punto cardinal
  rumboCardinal(rumbo: number): string {
    const puntos = ['N', 'NE', 'E', 'SE', 'S', 'SO', 'O', 'NO'];
    return puntos[Math.round(rumbo / 45) % 8];
  }

  private calcularProgreso(vuelo: Vuelo): void {
    if (!vuelo.origen || !vuelo.destino) {
      return;
    }

    // Distancia ya recorrida y distancia que falta, en linea recta
    const recorridos = this.distanciaKm(vuelo.origen.latitud, vuelo.origen.longitud, vuelo.latitud, vuelo.longitud);
    const restantes = this.distanciaKm(vuelo.latitud, vuelo.longitud, vuelo.destino.latitud, vuelo.destino.longitud);

    if (recorridos + restantes === 0) {
      return;
    }

    // Tiempo que falta a la velocidad actual
    let tiempo = '';
    if (vuelo.velocidad > 0) {
      const minutos = Math.round((restantes / vuelo.velocidad) * 60);
      tiempo = `${Math.floor(minutos / 60)} h ${minutos % 60} min`;
    }

    this.progreso.set({
      recorridos: Math.round(recorridos),
      restantes: Math.round(restantes),
      porcentaje: Math.round((recorridos / (recorridos + restantes)) * 100),
      tiempo,
    });
  }

  // Distancia en km entre dos coordenadas (formula de Haversine)
  private distanciaKm(lat1: number, lon1: number, lat2: number, lon2: number): number {
    const aRadianes = (grados: number) => (grados * Math.PI) / 180;
    const dLat = aRadianes(lat2 - lat1);
    const dLon = aRadianes(lon2 - lon1);

    const a =
      Math.sin(dLat / 2) ** 2 +
      Math.cos(aRadianes(lat1)) * Math.cos(aRadianes(lat2)) * Math.sin(dLon / 2) ** 2;

    return 6371 * 2 * Math.asin(Math.sqrt(a));
  }
}
