import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RespuestaVuelos } from '../models/respuesta-vuelos';
import { Vuelo } from '../models/vuelo';

@Injectable({
  providedIn: 'root',
})
export class VueloService {
  private readonly url = 'http://localhost:8080/api/vuelos';

  constructor(private http: HttpClient) {}

  getVuelosActivos(): Observable<RespuestaVuelos> {
    return this.http.get<RespuestaVuelos>(this.url);
  }

  getVuelo(id: string): Observable<Vuelo> {
    return this.http.get<Vuelo>(`${this.url}/${id}`);
  }

  buscar(texto: string): Observable<Vuelo[]> {
    const params = new HttpParams().set('q', texto);
    return this.http.get<Vuelo[]>(this.url, { params });
  }
}
