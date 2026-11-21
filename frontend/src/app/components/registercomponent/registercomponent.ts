import { HttpErrorResponse } from '@angular/common/http';
import { AfterViewInit, Component, ElementRef, OnInit, ViewChild, signal } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { VueloResumen } from '../../models/vuelo-resumen';
import { AuthService } from '../../services/auth.service';
import { GoogleLoginService } from '../../services/google-login.service';
import { VueloService } from '../../services/vuelo.service';

@Component({
  selector: 'app-registercomponent',
  standalone: false,
  styleUrl: './registercomponent.css',
  templateUrl: './registercomponent.html',
})
export class Registercomponent implements OnInit, AfterViewInit {
  // Vuelos que dibuja el mapa del panel izquierdo
  vuelos = signal<VueloResumen[]>([]);

  // Campos del formulario
  nombre = '';
  correo = '';
  contrasena = '';
  confirmar = '';
  terminos = false;

  // Estado de la pantalla
  intentado = signal(false);
  cargando = signal(false);
  error = signal('');
  googleDisponible = signal(true);

  // Minimo 8 caracteres, una mayuscula y un numero (igual que en el back)
  private readonly formatoContrasena = /^(?=.*[A-Z])(?=.*\d).{8,}$/;

  // Contenedor donde Google dibuja su boton
  @ViewChild('botonGoogle') botonGoogle?: ElementRef<HTMLElement>;

  constructor(
    private vueloService: VueloService,
    private auth: AuthService,
    private google: GoogleLoginService,
    private router: Router,
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
      .mostrarBoton(this.botonGoogle!.nativeElement, 'signup_with', (credencial) =>
        this.registrarConGoogle(credencial),
      )
      .then((mostrado) => this.googleDisponible.set(mostrado))
      .catch(() => this.googleDisponible.set(false));
  }

  contrasenaValida(): boolean {
    return this.formatoContrasena.test(this.contrasena);
  }

  contrasenasCoinciden(): boolean {
    return this.contrasena === this.confirmar;
  }

  registrar(formulario: NgForm): void {
    this.intentado.set(true);
    this.error.set('');
    if (formulario.invalid || !this.contrasenaValida() || !this.contrasenasCoinciden()) {
      return;
    }

    this.cargando.set(true);
    this.auth
      .registrar({ nombre: this.nombre, correo: this.correo, contrasena: this.contrasena })
      .subscribe({
        next: () => this.router.navigateByUrl('/inicio'),
        error: (error: HttpErrorResponse) => this.mostrarError(error),
      });
  }

  // Con Google el back crea la cuenta si no existe
  private registrarConGoogle(credencial: string): void {
    this.error.set('');
    this.cargando.set(true);
    this.auth.ingresarConGoogle(credencial).subscribe({
      next: () => this.router.navigateByUrl('/inicio'),
      error: (error: HttpErrorResponse) => this.mostrarError(error),
    });
  }

  private mostrarError(error: HttpErrorResponse): void {
    this.error.set(this.auth.mensajeDeError(error));
    this.cargando.set(false);
  }
}
