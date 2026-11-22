import { Component, Input } from '@angular/core';
import { Transporte } from '../../../models/transporte';

@Component({
  selector: 'app-infotransportecomponent',
  standalone: false,
  styleUrl: './infotransportecomponent.css',
  templateUrl: './infotransportecomponent.html',
})
export class Infotransportecomponent {
  @Input() transporte!: Transporte;
}
