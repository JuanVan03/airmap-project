import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, signal } from '@angular/core';
import { PREFERENCIAS_POR_DEFECTO, Preferencias } from '../../../models/preferencias';
import { AuthService } from '../../../services/auth.service';
import { COLORES, LETRAS, PreferenciasService } from '../../../services/preferencias.service';

@Component({
  selector: 'app-aparienciacomponent',
  standalone: false,
  styleUrl: './aparienciacomponent.css',
  templateUrl: './aparienciacomponent.html',
})
export class Aparienciacomponent implements OnDestroy {
  readonly temas = [
    { valor: 'OSCURO', nombre: 'Oscuro' },
    { valor: 'CLARO', nombre: 'Claro' },
    { valor: 'ALTO_CONTRASTE', nombre: 'Alto contraste' },
  ];
  readonly letras = [
    { valor: 'INTER', nombre: 'Inter', fuente: LETRAS.INTER },
    { valor: 'POPPINS', nombre: 'Poppins', fuente: LETRAS.POPPINS },
    { valor: 'MONTSERRAT', nombre: 'Montserrat', fuente: LETRAS.MONTSERRAT },
    { valor: 'ATKINSON', nombre: 'Atkinson (lectura fácil)', fuente: LETRAS.ATKINSON },
  ];
  readonly tamanos = [
    { valor: 'PEQUENO', nombre: 'Pequeño' },
    { valor: 'NORMAL', nombre: 'Normal' },
    { valor: 'GRANDE', nombre: 'Grande' },
    { valor: 'MUY_GRANDE', nombre: 'Muy grande' },
  ];
  readonly colores = [
    { valor: 'AZUL', nombre: 'Azul', muestra: COLORES.AZUL.primario },
    { valor: 'VERDE', nombre: 'Verde', muestra: COLORES.VERDE.primario },
    { valor: 'MORADO', nombre: 'Morado', muestra: COLORES.MORADO.primario },
    { valor: 'NARANJA', nombre: 'Naranja', muestra: COLORES.NARANJA.primario },
    { valor: 'ROSA', nombre: 'Rosa', muestra: COLORES.ROSA.primario },
    { valor: 'TURQUESA', nombre: 'Turquesa', muestra: COLORES.TURQUESA.primario },
  ];
  readonly aviones = [
    { valor: 'AVION', nombre: 'Avión', simbolo: '✈︎' },
    { valor: 'FLECHA', nombre: 'Flecha', simbolo: '➤' },
    { valor: 'PUNTO', nombre: 'Punto', simbolo: '●' },
  ];

  borrador: Preferencias;
  guardando = signal(false);
  exito = signal(false);
  error = signal('');

  constructor(
    private preferencias: PreferenciasService,
    private auth: AuthService,
  ) {
    this.borrador = { ...this.preferencias.guardadas() };
  }

  // Si sale sin guardar, vuelve la apariencia que tenia guardada
  ngOnDestroy(): void {
    this.preferencias.aplicar(this.preferencias.guardadas());
  }

  elegir(campo: keyof Preferencias, valor: string): void {
    this.borrador = { ...this.borrador, [campo]: valor } as Preferencias;
    this.preferencias.aplicar(this.borrador);
    this.exito.set(false);
    this.error.set('');
  }

  hayCambios(): boolean {
    return JSON.stringify(this.borrador) !== JSON.stringify(this.preferencias.guardadas());
  }

  guardar(): void {
    this.guardando.set(true);
    this.error.set('');
    this.preferencias.guardar(this.borrador).subscribe({
      next: () => {
        this.exito.set(true);
        this.guardando.set(false);
      },
      error: (e: HttpErrorResponse) => {
        this.error.set(this.auth.mensajeDeError(e));
        this.guardando.set(false);
      },
    });
  }

  restablecer(): void {
    this.borrador = { ...PREFERENCIAS_POR_DEFECTO };
    this.preferencias.aplicar(this.borrador);
    this.guardar();
  }
}
