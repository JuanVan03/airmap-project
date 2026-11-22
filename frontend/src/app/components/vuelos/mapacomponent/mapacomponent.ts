import {
  AfterViewInit,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnChanges,
  OnDestroy,
  Output,
  ViewChild,
  effect,
} from '@angular/core';
import * as L from 'leaflet';
import { VueloResumen } from '../../../models/vuelo-resumen';
import { PreferenciasService } from '../../../services/preferencias.service';

@Component({
  selector: 'app-mapacomponent',
  standalone: false,
  styleUrl: './mapacomponent.css',
  templateUrl: './mapacomponent.html',
})
export class Mapacomponent implements AfterViewInit, OnChanges, OnDestroy {
  @Input() vuelos: VueloResumen[] = [];
  @Input() vueloSeleccionado: string | null = null;
  @Output() vueloClick = new EventEmitter<string>();

  @ViewChild('contenedor') contenedor!: ElementRef<HTMLDivElement>;

  private mapa!: L.Map;
  private capa = L.layerGroup();

  constructor(private preferencias: PreferenciasService) {
    effect(() => {
      this.preferencias.actual();
      if (this.mapa) {
        this.dibujar();
      }
    });
  }

  ngAfterViewInit(): void {
    this.mapa = L.map(this.contenedor.nativeElement, { preferCanvas: true }).setView([20, 0], 2);

    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors',
    }).addTo(this.mapa);

    this.capa.addTo(this.mapa);
    this.mapa.on('moveend', () => {
      if (this.preferencias.actual().avion !== 'PUNTO') {
        this.dibujar();
      }
    });
    this.dibujar();
    this.centrar();
  }

  ngOnChanges(): void {
    if (this.mapa) {
      this.dibujar();
      this.centrar();
    }
  }

  ngOnDestroy(): void {
    this.mapa.remove();
  }

  private dibujar(): void {
    this.capa.clearLayers();

    const estilos = getComputedStyle(document.documentElement);
    const primario = estilos.getPropertyValue('--primario').trim() || '#2F80ED';
    const acento = estilos.getPropertyValue('--acento').trim() || '#22D3EE';

    const icono = this.preferencias.actual().avion;
    const vista = this.mapa.getBounds();
    // Viendo el mundo entero hay miles de vuelos: con zoom bajo se usan puntos para que el mapa no se ponga lento
    const verIconos = icono !== 'PUNTO' && this.mapa.getZoom() >= 5;

    for (const vuelo of this.vuelos) {
      const seleccionado = vuelo.id === this.vueloSeleccionado;
      let marca: L.Layer;

      if (verIconos || (icono !== 'PUNTO' && seleccionado)) {
        if (!seleccionado && !vista.contains([vuelo.latitud, vuelo.longitud])) {
          continue;
        }
        marca = L.marker([vuelo.latitud, vuelo.longitud], {
          icon: this.crearIcono(icono === 'AVION' ? '✈︎' : '➤', vuelo.rumbo, seleccionado),
        });
      } else {
        marca = L.circleMarker([vuelo.latitud, vuelo.longitud], {
          radius: seleccionado ? 8 : 2,
          weight: seleccionado ? 2 : 1,
          color: seleccionado ? '#F1F5F9' : acento,
          fillColor: seleccionado ? primario : acento,
          fillOpacity: 0.9,
        });
      }

      marca.on('click', () => this.vueloClick.emit(vuelo.id));
      this.capa.addLayer(marca);
    }
  }

  private crearIcono(simbolo: string, rumbo: number, seleccionado: boolean): L.DivIcon {
    // Los dos simbolos apuntan a la derecha (este = 90°), por eso se resta 90 al rumbo
    return L.divIcon({
      className: seleccionado ? 'icono-vuelo seleccionado' : 'icono-vuelo',
      html: `<span style="transform: rotate(${rumbo - 90}deg)">${simbolo}</span>`,
      iconSize: [24, 24],
      iconAnchor: [12, 12],
    });
  }

  private centrar(): void {
    const vuelo = this.vuelos.find((v) => v.id === this.vueloSeleccionado);
    if (vuelo) {
      this.mapa.setView([vuelo.latitud, vuelo.longitud], Math.max(this.mapa.getZoom(), 4));
    }
  }
}
