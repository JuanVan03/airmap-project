import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../../../services/auth.service';
import { FavoritoService } from '../../../services/favorito.service';

@Component({
  selector: 'app-favoritocomponent',
  standalone: false,
  styleUrl: './favoritocomponent.css',
  templateUrl: './favoritocomponent.html',
})
export class Favoritocomponent {
  // Solo hacen falta el id y el numero del vuelo
  @Input() vuelo!: { id: string; numeroVuelo: string };

  constructor(
    private favoritos: FavoritoService,
    private auth: AuthService,
    private router: Router,
  ) {}

  esFavorito(): boolean {
    return this.favoritos.esFavorito(this.vuelo.id);
  }

  alternar(): void {
    // Sin sesion se manda al login y luego se vuelve a esta pagina
    if (!this.auth.haySesion()) {
      this.router.navigate(['/login'], { queryParams: { redirigir: this.router.url } });
      return;
    }

    const peticion: Observable<unknown> = this.esFavorito()
      ? this.favoritos.quitar(this.vuelo.id)
      : this.favoritos.agregar(this.vuelo.id, this.vuelo.numeroVuelo);
    peticion.subscribe({ error: () => {} });
  }
}
