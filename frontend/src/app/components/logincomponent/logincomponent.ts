import { HttpErrorResponse } from '@angular/common/http';
import { AfterViewInit, Component, ElementRef, OnInit, ViewChild, signal } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { VueloResumen } from '../../models/vuelo-resumen';
import { AuthService } from '../../services/auth.service';
import { GoogleLoginService } from '../../services/google-login.service';
import { VueloService } from '../../services/vuelo.service';

@Component({
  selector: 'app-logincomponent',
  standalone: false,
  styleUrl: './logincomponent.css',
  templateUrl: './logincomponent.html',
})
export class Logincomponent implements OnInit, AfterViewInit {
  // Vuelos que dibuja el mapa del panel izquierdo
  vuelos = signal<VueloResumen[]>([]);

  // Campos del formulario
  correo = '';
  contrasena = '';
  recordar = false;

  // Estado de la pantalla
  intentado = signal(false);
  cargando = signal(false);
  error = signal('');
  googleDisponible = signal(true);
  // El back dijo que falta confirmar el correo: se ofrece reenviar el enlace
  sinVerificar = signal(false);
  aviso = signal('');

  // Contenedor donde Google dibuja su boton
  @ViewChild('botonGoogle') botonGoogle?: ElementRef<HTMLElement>;

  constructor(
    private vueloService: VueloService,
    private auth: AuthService,
    private google: GoogleLoginService,
    private router: Router,
    private ruta: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.vueloService.getVuelosActivos().subscribe({
      next: (respuesta) => this.vuelos.set(respuesta.vuelos),
      // El mapa es decorativo: si falla, se ve vacio y el formulario sigue funcionando
      error: () => {},
    });
  }

  ngAfterViewInit(): void {
    this.google
      .mostrarBoton(this.botonGoogle!.nativeElement, 'continue_with', (credencial) =>
        this.ingresarConGoogle(credencial),
      )
      .then((mostrado) => this.googleDisponible.set(mostrado))
      .catch(() => this.googleDisponible.set(false));
  }

  ingresar(formulario: NgForm): void {
    this.intentado.set(true);
    this.error.set('');
    this.aviso.set('');
    this.sinVerificar.set(false);
    if (formulario.invalid) {
      return;
    }

    this.cargando.set(true);
    this.auth.ingresar({ correo: this.correo, contrasena: this.contrasena }, this.recordar).subscribe({
      next: () => this.irAlDestino(),
      error: (error: HttpErrorResponse) => this.mostrarError(error),
    });
  }

  // Pide que el back envie otra vez el correo de confirmacion
  reenviar(): void {
    this.aviso.set('');
    this.auth.reenviarVerificacion(this.correo).subscribe({
      next: (respuesta) => this.aviso.set(respuesta.mensaje),
      error: (error: HttpErrorResponse) => this.error.set(this.auth.mensajeDeError(error)),
    });
  }

  private ingresarConGoogle(credencial: string): void {
    this.error.set('');
    this.cargando.set(true);
    this.auth.ingresarConGoogle(credencial).subscribe({
      next: () => this.irAlDestino(),
      error: (error: HttpErrorResponse) => this.mostrarError(error),
    });
  }

  private mostrarError(error: HttpErrorResponse): void {
    this.sinVerificar.set(error.error?.codigo === 'CORREO_NO_VERIFICADO');
    this.error.set(this.auth.mensajeDeError(error));
    this.cargando.set(false);
  }

  // Si venia de una pagina protegida (ej. /perfil) vuelve a ella, si no va al inicio
  private irAlDestino(): void {
    // El administrador siempre entra a su panel
    if (this.auth.esAdmin()) {
      this.router.navigateByUrl('/admin');
      return;
    }
    const destino = this.ruta.snapshot.queryParamMap.get('redirigir');
    const esInterno = destino !== null && destino.startsWith('/') && !destino.startsWith('//');
    this.router.navigateByUrl(esInterno ? destino : '/inicio');
  }
}
