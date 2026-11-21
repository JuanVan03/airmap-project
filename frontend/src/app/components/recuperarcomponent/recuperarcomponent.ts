import { Component, signal } from '@angular/core';

@Component({
  selector: 'app-recuperarcomponent',
  standalone: false,
  styleUrl: './recuperarcomponent.css',
  templateUrl: './recuperarcomponent.html',
})
export class Recuperarcomponent {
  // Cuando es true se muestra el mensaje de "enlace enviado"
  enviado = signal(false);

  // Por ahora solo cambia la vista; cuando exista el back aqui se llama al endpoint
  enviar(): void {
    this.enviado.set(true);
  }
}
