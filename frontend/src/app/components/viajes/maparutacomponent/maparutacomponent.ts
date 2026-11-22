import {
  AfterViewInit,
  Component,
  ElementRef,
  Input,
  OnChanges,
  OnDestroy,
  SimpleChanges,
  ViewChild,
} from '@angular/core';
import * as L from 'leaflet';
import { Aeropuerto } from '../../../models/aeropuerto';
import { Bus, Paradero } from '../../../models/transporte';
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
  // TransMilenio: paradero donde se sube y buses en vivo
  @Input() paradero: Paradero | null = null;
  @Input() buses: Bus[] = [];

  @ViewChild('contenedor') contenedor!: ElementRef<HTMLDivElement>;

  private mapa!: L.Map;
  private capa = L.layerGroup();

  ngAfterViewInit(): void {
    this.mapa = L.map(this.contenedor.nativeElement).setView([20, 0], 2);

    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors',
    }).addTo(this.mapa);

    this.capa.addTo(this.mapa);
    this.dibujar(true);
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (this.mapa) {
      // Si solo se movieron los buses no se mueve la vista, para no molestar al usuario
      const soloBuses = Object.keys(changes).length === 1 && changes['buses'] !== undefined;
      this.dibujar(!soloBuses);
    }
  }

  ngOnDestroy(): void {
    this.mapa.remove();
  }

  private dibujar(ajustarVista: boolean): void {
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

    // Punto rojo: paradero de TransMilenio
    if (this.paradero) {
      L.circleMarker([this.paradero.latitud, this.paradero.longitud], {
        radius: 9,
        color: '#F1F5F9',
        fillColor: '#EF4444',
        fillOpacity: 1,
      })
        .bindTooltip('Paradero: ' + this.paradero.nombre)
        .addTo(this.capa);
    }

    // Puntos verdes: buses en vivo de las rutas que llegan al aeropuerto
    for (const bus of this.buses) {
      L.circleMarker([bus.latitud, bus.longitud], { radius: 6, color: '#0B1220', fillColor: '#34D399', fillOpacity: 1 })
        .bindTooltip(`Bus ${bus.ruta} · a ${bus.distanciaM} m del paradero`)
        .addTo(this.capa);
    }

    // Linea de la ruta (en TransMilenio es la caminata hasta el paradero)
    const linea = L.polyline(this.ruta as L.LatLngExpression[], { color: '#2F80ED', weight: 5 }).addTo(this.capa);

    if (!ajustarVista) {
      return;
    }

    // TransMilenio: el mapa muestra la caminata, el paradero y los buses cercanos
    if (this.paradero) {
      const vista = L.latLngBounds([[this.paradero.latitud, this.paradero.longitud]]);
      puntos.forEach((p) => vista.extend(p));
      this.buses.forEach((b) => vista.extend([b.latitud, b.longitud]));
      this.mapa.fitBounds(vista, { padding: [40, 40] });
    } else if (this.ruta.length > 0) {
      // Con ruta, el mapa se ajusta para mostrarla completa
      this.mapa.fitBounds(linea.getBounds(), { padding: [40, 40] });
    } else if (puntos.length === 2) {
      this.mapa.fitBounds(L.latLngBounds(puntos), { padding: [40, 40] });
    } else if (puntos.length === 1) {
      this.mapa.setView(puntos[0], 10);
    }
  }
}
