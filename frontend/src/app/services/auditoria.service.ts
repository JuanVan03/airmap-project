import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FiltrosAuditoria, PaginaAuditoria } from '../models/auditoria';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuditoriaService {
  private readonly url = `${environment.apiUrl}/api/admin/auditoria`;

  constructor(private http: HttpClient) {}

  // Pide una pagina de la auditoria; solo manda los filtros que tienen algo escrito
  listar(filtros: FiltrosAuditoria, pagina: number, tamano: number): Observable<PaginaAuditoria> {
    let params = new HttpParams().set('pagina', pagina).set('tamano', tamano);
    for (const [nombre, valor] of Object.entries(filtros)) {
      if (valor.trim() !== '') {
        params = params.set(nombre, valor.trim());
      }
    }
    return this.http.get<PaginaAuditoria>(this.url, { params });
  }

  // Acciones que ya aparecen en el registro (para el filtro)
  acciones(): Observable<string[]> {
    return this.http.get<string[]>(`${this.url}/acciones`);
  }
}
