// Opciones de apariencia. Son las mismas que acepta el back (no hay valores libres)
export type Tema = 'OSCURO' | 'CLARO' | 'ALTO_CONTRASTE';
export type Letra = 'INTER' | 'POPPINS' | 'MONTSERRAT' | 'ATKINSON';
export type Tamano = 'PEQUENO' | 'NORMAL' | 'GRANDE' | 'MUY_GRANDE';
export type ColorPrincipal = 'AZUL' | 'VERDE' | 'MORADO' | 'NARANJA' | 'ROSA' | 'TURQUESA';
export type IconoAvion = 'AVION' | 'FLECHA' | 'PUNTO';

export interface Preferencias {
  tema: Tema;
  letra: Letra;
  tamano: Tamano;
  color: ColorPrincipal;
  avion: IconoAvion;
}

export const PREFERENCIAS_POR_DEFECTO: Preferencias = {
  tema: 'OSCURO',
  letra: 'INTER',
  tamano: 'NORMAL',
  color: 'AZUL',
  avion: 'AVION',
};
