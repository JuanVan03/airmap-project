import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RespuestaVuelos } from '../models/respuesta-vuelos';
import { Vuelo } from '../models/vuelo';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class VueloService {
  private readonly url = `${environment.apiUrl}/api/vuelos`;

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
