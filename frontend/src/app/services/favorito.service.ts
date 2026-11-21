import { HttpClient } from '@angular/common/http';
import { Injectable, computed, effect, signal, untracked } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { Favorito } from '../models/favorito';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root',
})
export class FavoritoService {
  private readonly url = 'http://localhost:8080/api/favoritos';

  // Favoritos del usuario que inicio sesion
  readonly favoritos = signal<Favorito[]>([]);
  // Ids de los vuelos favoritos, para saber rapido si una estrella va llena
  private readonly ids = computed(() => new Set(this.favoritos().map((favorito) => favorito.vueloId)));

  constructor(
    private http: HttpClient,
    private auth: AuthService,
  ) {
    // Al iniciar sesion carga la lista; al cerrarla la vacia
    effect(() => {
      if (this.auth.autenticado()) {
        untracked(() => this.cargar());
      } else {
        this.favoritos.set([]);
      }
    });
  }

  esFavorito(vueloId: string): boolean {
    return this.ids().has(vueloId);
  }

  cargar(): void {
    this.http.get<Favorito[]>(this.url).subscribe({
      next: (lista) => this.favoritos.set(lista),
      error: () => {},
    });
  }

  agregar(vueloId: string, numeroVuelo: string): Observable<Favorito> {
    return this.http.post<Favorito>(this.url, { vueloId, numeroVuelo }).pipe(
      tap((favorito) =>
        this.favoritos.update((lista) => [favorito, ...lista.filter((f) => f.vueloId !== favorito.vueloId)]),
      ),
    );
  }

  quitar(vueloId: string): Observable<void> {
    return this.http
      .delete<void>(`${this.url}/${vueloId}`)
      .pipe(tap(() => this.favoritos.update((lista) => lista.filter((f) => f.vueloId !== vueloId))));
  }
}
