import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RutaGuardada } from '../models/ruta-guardada';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class RutaGuardadaService {
  private readonly url = `${environment.apiUrl}/api/rutas-guardadas`;

  constructor(private http: HttpClient) {}

  listar(): Observable<RutaGuardada[]> {
    return this.http.get<RutaGuardada[]>(this.url);
  }

  // El id lo asigna el back
  guardar(ruta: Omit<RutaGuardada, 'id'>): Observable<RutaGuardada> {
    return this.http.post<RutaGuardada>(this.url, ruta);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
