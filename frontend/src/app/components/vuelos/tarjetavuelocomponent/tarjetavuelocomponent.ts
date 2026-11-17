import { Component, Input } from '@angular/core';
import { Vuelo } from '../../../models/vuelo';

@Component({
  selector: 'app-tarjetavuelocomponent',
  standalone: false,
  styleUrl: './tarjetavuelocomponent.css',
  templateUrl: './tarjetavuelocomponent.html',
})
export class Tarjetavuelocomponent {
  @Input() vuelo!: Vuelo;
  @Input() verDetalles = true;
}
