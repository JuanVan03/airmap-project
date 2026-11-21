import {
  AfterViewInit,
  Component,
  ElementRef,
  Input,
  OnChanges,
  OnDestroy,
  ViewChild,
} from '@angular/core';
import * as L from 'leaflet';
import { Aeropuerto } from '../../../models/aeropuerto';
import { Ubicacion } from '../../../models/ubicacion';

@Component({
  selector: 'app-maparutacomponent',
  standalone: false,
  styleUrl: './maparutacomponent.css',
  templateUrl: './maparutacomponent.html',
})
export class Maparutacomponent implements AfterViewInit, OnChanges, OnDestroy {
  @Input() origen: Ubicacion | null = null;
  @Input() destino: Aeropuerto | null = null;
  // Puntos de la ruta como [latitud, longitud]
  @Input() ruta: number[][] = [];

  @ViewChild('contenedor') contenedor!: ElementRef<HTMLDivElement>;

  private mapa!: L.Map;
  private capa = L.layerGroup();

  ngAfterViewInit(): void {
    this.mapa = L.map(this.contenedor.nativeElement).setView([20, 0], 2);

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
    const puntos: L.LatLngExpression[] = [];

    // Punto azul claro: donde esta el usuario
    if (this.origen) {
      const punto: L.LatLngExpression = [this.origen.latitud, this.origen.longitud];
      L.circleMarker(punto, { radius: 8, color: '#F1F5F9', fillColor: '#22D3EE', fillOpacity: 1 })
        .bindTooltip('Tu ubicación')
        .addTo(this.capa);
      puntos.push(punto);
    }

    // Punto amarillo: el aeropuerto de destino
    if (this.destino) {
      const punto: L.LatLngExpression = [this.destino.latitud, this.destino.longitud];
      L.circleMarker(punto, { radius: 8, color: '#F1F5F9', fillColor: '#FBBF24', fillOpacity: 1 })
        .bindTooltip(this.destino.nombre)
        .addTo(this.capa);
      puntos.push(punto);
    }

    // Con ruta, el mapa se ajusta para mostrarla completa
    if (this.ruta.length > 0) {
      const linea = L.polyline(this.ruta as L.LatLngExpression[], { color: '#2F80ED', weight: 5 }).addTo(this.capa);
      this.mapa.fitBounds(linea.getBounds(), { padding: [40, 40] });
    } else if (puntos.length === 2) {
      this.mapa.fitBounds(L.latLngBounds(puntos), { padding: [40, 40] });
    } else if (puntos.length === 1) {
      this.mapa.setView(puntos[0], 10);
    }
  }
}
