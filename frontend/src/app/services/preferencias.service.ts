import { HttpClient } from '@angular/common/http';
import { Injectable, effect, signal, untracked } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { ColorPrincipal, Letra, PREFERENCIAS_POR_DEFECTO, Preferencias, Tamano, Tema } from '../models/preferencias';
import { AuthService } from './auth.service';

export const TEMAS: Record<Tema, Record<string, string>> = {
  OSCURO: {
    '--fondo': '#0B1220',
    '--superficie': '#121A2B',
    '--elevado': '#1A2438',
    '--borde': '#263248',
    '--texto': '#F1F5F9',
    '--texto-suave': '#94A3B8',
  },
  CLARO: {
    '--fondo': '#F1F5F9',
    '--superficie': '#FFFFFF',
    '--elevado': '#E2E8F0',
    '--borde': '#CBD5E1',
    '--texto': '#0F172A',
    '--texto-suave': '#475569',
  },
  ALTO_CONTRASTE: {
    '--fondo': '#000000',
    '--superficie': '#0A0A0A',
    '--elevado': '#1C1C1C',
    '--borde': '#FFFFFF',
    '--texto': '#FFFFFF',
    '--texto-suave': '#E5E5E5',
  },
};

export const COLORES: Record<ColorPrincipal, { primario: string; acento: string }> = {
  AZUL: { primario: '#2F80ED', acento: '#22D3EE' },
  VERDE: { primario: '#16A34A', acento: '#4ADE80' },
  MORADO: { primario: '#7C3AED', acento: '#C084FC' },
  NARANJA: { primario: '#EA580C', acento: '#FB923C' },
  ROSA: { primario: '#DB2777', acento: '#F472B6' },
  TURQUESA: { primario: '#0D9488', acento: '#2DD4BF' },
};

export const LETRAS: Record<Letra, string> = {
  INTER: "'Inter', Arial, sans-serif",
  POPPINS: "'Poppins', Arial, sans-serif",
  MONTSERRAT: "'Montserrat', Arial, sans-serif",
  ATKINSON: "'Atkinson Hyperlegible', Arial, sans-serif",
};

export const TAMANOS: Record<Tamano, number> = {
  PEQUENO: 0.9,
  NORMAL: 1,
  GRANDE: 1.1,
  MUY_GRANDE: 1.2,
};

@Injectable({
  providedIn: 'root',
})
export class PreferenciasService {
  private readonly url = 'http://localhost:8080/api/preferencias';

  readonly guardadas = signal<Preferencias>(PREFERENCIAS_POR_DEFECTO);
  readonly actual = signal<Preferencias>(PREFERENCIAS_POR_DEFECTO);

  constructor(
    private http: HttpClient,
    private auth: AuthService,
  ) {
    effect(() => {
      const conSesion = this.auth.autenticado();
      untracked(() => {
        if (conSesion) {
          this.cargar();
        } else {
          this.guardadas.set(PREFERENCIAS_POR_DEFECTO);
          this.aplicar(PREFERENCIAS_POR_DEFECTO);
        }
      });
    });
  }

  private cargar(): void {
    this.http.get<Preferencias>(this.url).subscribe({
      next: (preferencias) => {
        this.guardadas.set(preferencias);
        this.aplicar(preferencias);
      },
      error: () => {},
    });
  }

  guardar(preferencias: Preferencias): Observable<Preferencias> {
    return this.http.put<Preferencias>(this.url, preferencias).pipe(
      tap((guardadas) => {
        this.guardadas.set(guardadas);
        this.aplicar(guardadas);
      }),
    );
  }

  aplicar(preferencias: Preferencias): void {
    const raiz = document.documentElement.style;
    for (const [variable, valor] of Object.entries(TEMAS[preferencias.tema])) {
      raiz.setProperty(variable, valor);
    }

    const color = COLORES[preferencias.color];
    raiz.setProperty('--primario', color.primario);
    // En el tema claro el acento claro casi no se lee sobre blanco, por eso se usa el principal
    raiz.setProperty('--acento', preferencias.tema === 'CLARO' ? color.primario : color.acento);

    raiz.setProperty('--fuente', LETRAS[preferencias.letra]);
    raiz.setProperty('--escala', String(TAMANOS[preferencias.tamano]));
    this.actual.set(preferencias);
  }
}
