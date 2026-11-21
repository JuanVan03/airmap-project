import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ModoViaje, Ruta } from '../models/ruta';
import { Ubicacion } from '../models/ubicacion';

@Injectable({
  providedIn: 'root',
})
export class RutaService {
  private readonly url = 'http://localhost:8080/api/rutas';

  constructor(private http: HttpClient) {}

  // Ruta terrestre desde la ubicacion del usuario hasta el aeropuerto
  getRuta(origen: Ubicacion, latDestino: number, lonDestino: number, modo: ModoViaje): Observable<Ruta> {
    const params = new HttpParams()
      .set('latOrigen', origen.latitud)
      .set('lonOrigen', origen.longitud)
      .set('latDestino', latDestino)
      .set('lonDestino', lonDestino)
      .set('modo', modo);
    return this.http.get<Ruta>(this.url, { params });
  }
}
