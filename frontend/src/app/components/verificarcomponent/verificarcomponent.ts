import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-verificarcomponent',
  standalone: false,
  styleUrl: './verificarcomponent.css',
  templateUrl: './verificarcomponent.html',
})
export class Verificarcomponent implements OnInit {
  // cargando: esperando al back | ok: correo confirmado | error: el enlace no sirvio
  estado = signal<'cargando' | 'ok' | 'error'>('cargando');
  mensaje = signal('');

  constructor(
    private ruta: ActivatedRoute,
    private auth: AuthService,
  ) {}

  ngOnInit(): void {
    // El enlace del correo es /verificar?token=...
    const token = this.ruta.snapshot.queryParamMap.get('token');
    if (!token) {
      this.fallar('El enlace no es válido. Abre el botón del correo que te enviamos.');
      return;
    }

    this.auth.verificarCorreo(token).subscribe({
      next: (respuesta) => {
        this.mensaje.set(respuesta.mensaje);
        this.estado.set('ok');
      },
      error: (error: HttpErrorResponse) => this.fallar(this.auth.mensajeDeError(error)),
    });
  }

  private fallar(mensaje: string): void {
    this.mensaje.set(mensaje);
    this.estado.set('error');
  }
}
