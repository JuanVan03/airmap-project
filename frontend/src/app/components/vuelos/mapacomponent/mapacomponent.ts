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
} from '@angular/core';
import * as L from 'leaflet';
import { VueloResumen } from '../../../models/vuelo-resumen';

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

  ngAfterViewInit(): void {
    this.mapa = L.map(this.contenedor.nativeElement, { preferCanvas: true }).setView([20, 0], 2);

    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors',
    }).addTo(this.mapa);

    this.capa.addTo(this.mapa);
    this.dibujar();
  }

  ngOnChanges(): void {
    if (this.mapa) {
      this.dibujar();
    }
  }

  ngOnDestroy(): void {
    this.mapa.remove();
  }

  private dibujar(): void {
    this.capa.clearLayers();

    for (const vuelo of this.vuelos) {
      const seleccionado = vuelo.id === this.vueloSeleccionado;

      const punto = L.circleMarker([vuelo.latitud, vuelo.longitud], {
        radius: seleccionado ? 9 : 4,
        color: seleccionado ? '#F1F5F9' : '#22D3EE',
        fillColor: seleccionado ? '#2F80ED' : '#22D3EE',
        fillOpacity: 0.9,
      });

      punto.on('click', () => this.vueloClick.emit(vuelo.id));
      this.capa.addLayer(punto);
    }
  }
}
