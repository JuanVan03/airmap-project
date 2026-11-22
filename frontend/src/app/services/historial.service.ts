import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HistorialVuelo } from '../models/historial';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class HistorialService {
  private readonly url = `${environment.apiUrl}/api/historial`;

  constructor(private http: HttpClient) {}

  // Del vuelo más reciente al más viejo
  listar(): Observable<HistorialVuelo[]> {
    return this.http.get<HistorialVuelo[]>(this.url);
  }

  // Anota que el usuario abrió un vuelo (si ya estaba, el back solo actualiza la fecha)
  registrar(vueloId: string, numeroVuelo: string): Observable<void> {
    return this.http.post<void>(this.url, { vueloId, numeroVuelo });
  }

  quitar(vueloId: string): Observable<void> {
    return this.http.delete<void>(`${this.url}/${vueloId}`);
  }

  vaciar(): Observable<void> {
    return this.http.delete<void>(this.url);
  }
}
