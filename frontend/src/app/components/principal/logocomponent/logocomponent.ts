import { Component } from '@angular/core';

/**
 * Logo de AirMap dibujado en SVG dentro de la página. Así sus colores usan las variables del tema
 * (--texto y --acento) y cambian cuando el usuario elige otro tema o color en Apariencia.
 */
@Component({
  selector: 'app-logocomponent',
  standalone: false,
  styleUrl: './logocomponent.css',
  templateUrl: './logocomponent.html',
})
export class Logocomponent {}
