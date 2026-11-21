import { Component, computed, inject } from '@angular/core';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-navbarcomponent',
  standalone: false,
  styleUrl: './navbarcomponent.css',
  templateUrl: './navbarcomponent.html',
})
export class Navbarcomponent {
  private auth = inject(AuthService);

  autenticado = this.auth.autenticado;
  esAdmin = this.auth.esAdmin;

  // "Juan Pérez" -> "Juan"
  primerNombre = computed(() => this.auth.usuario()?.nombre.split(' ')[0] ?? '');

  cerrarSesion(): void {
    this.auth.cerrarSesion();
  }
}
