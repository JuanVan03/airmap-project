import { HttpErrorResponse } from '@angular/common/http';
import { Component, signal } from '@angular/core';
import { NgForm } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-recuperarcomponent',
  standalone: false,
  styleUrl: './recuperarcomponent.css',
  templateUrl: './recuperarcomponent.html',
})
export class Recuperarcomponent {
  // Campo del formulario
  correo = '';

  // Estado de la pantalla
  intentado = signal(false);
  cargando = signal(false);
  error = signal('');
  // Cuando es true se muestra el mensaje de "enlace enviado"
  enviado = signal(false);

  constructor(private auth: AuthService) {}

  enviar(formulario: NgForm): void {
    this.intentado.set(true);
    this.error.set('');
    if (formulario.invalid) {
      return;
    }

    this.cargando.set(true);
    this.auth.recuperarContrasena(this.correo).subscribe({
      next: () => {
        this.enviado.set(true);
        this.cargando.set(false);
      },
      error: (error: HttpErrorResponse) => {
        this.error.set(this.auth.mensajeDeError(error));
        this.cargando.set(false);
      },
    });
  }
}
