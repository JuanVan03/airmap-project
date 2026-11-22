import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Aeropuerto } from '../models/aeropuerto';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AeropuertoService {
  private readonly url = `${environment.apiUrl}/api/aeropuertos`;

  constructor(private http: HttpClient) {}

  // Busca por codigo, nombre o ciudad
  buscar(texto: string): Observable<Aeropuerto[]> {
    const params = new HttpParams().set('q', texto);
    return this.http.get<Aeropuerto[]>(this.url, { params });
  }
}
