import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, signal } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-restablecercomponent',
  standalone: false,
  styleUrl: './restablecercomponent.css',
  templateUrl: './restablecercomponent.html',
})
export class Restablecercomponent implements OnInit {
  // Campos del formulario
  contrasena = '';
  confirmar = '';

  // Token que viene en el enlace del correo: /restablecer?token=...
  private token = '';

  // Estado de la pantalla
  intentado = signal(false);
  cargando = signal(false);
  error = signal('');
  // El enlace no trae token (se abrio la pagina sin pasar por el correo)
  enlaceInvalido = signal(false);
  // La contraseña ya se cambio
  listo = signal(false);

  // Minimo 8 caracteres, una mayuscula y un numero (igual que en el back)
  private readonly formatoContrasena = /^(?=.*[A-Z])(?=.*\d).{8,}$/;

  constructor(
    private ruta: ActivatedRoute,
    private auth: AuthService,
  ) {}

  ngOnInit(): void {
    this.token = this.ruta.snapshot.queryParamMap.get('token') ?? '';
    this.enlaceInvalido.set(this.token === '');
  }

  contrasenaValida(): boolean {
    return this.formatoContrasena.test(this.contrasena);
  }

  contrasenasCoinciden(): boolean {
    return this.contrasena === this.confirmar;
  }

  cambiar(formulario: NgForm): void {
    this.intentado.set(true);
    this.error.set('');
    if (formulario.invalid || !this.contrasenaValida() || !this.contrasenasCoinciden()) {
      return;
    }

    this.cargando.set(true);
    this.auth.restablecerContrasena(this.token, this.contrasena).subscribe({
      next: () => {
        this.listo.set(true);
        this.cargando.set(false);
      },
      error: (error: HttpErrorResponse) => {
        this.error.set(this.auth.mensajeDeError(error));
        this.cargando.set(false);
      },
    });
  }
}
