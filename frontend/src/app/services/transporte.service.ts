import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TiempoReal, Transporte } from '../models/transporte';
import { Ubicacion } from '../models/ubicacion';

@Injectable({
  providedIn: 'root',
})
export class TransporteService {
  private readonly url = 'http://localhost:8080/api/transporte';

  constructor(private http: HttpClient) {}

  getTransporte(origen: Ubicacion): Observable<Transporte> {
    const params = new HttpParams().set('lat', origen.latitud).set('lon', origen.longitud);
    return this.http.get<Transporte>(this.url, { params });
  }

  getTiempoReal(idParadero: string): Observable<TiempoReal> {
    const params = new HttpParams().set('paradero', idParadero);
    return this.http.get<TiempoReal>(`${this.url}/tiempo-real`, { params });
  }
}
