import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, signal } from '@angular/core';
import { FiltrosAuditoria, PaginaAuditoria } from '../../models/auditoria';
import { AuditoriaService } from '../../services/auditoria.service';
import { AuthService } from '../../services/auth.service';

// Nombre en español de cada acción que guarda el back
const ETIQUETAS: Record<string, string> = {
  ADMIN_CLAVE_ACTUALIZADA: 'Contraseña de admin actualizada',
  ADMIN_CREADO: 'Administrador creado',
  BUSQUEDA_AEROPUERTO: 'Búsqueda de aeropuerto',
  BUSQUEDA_VUELO: 'Búsqueda de vuelo',
  CAMBIO_CONTRASENA: 'Cambio de contraseña',
  CIERRE_SESION: 'Cierre de sesión',
  CONSULTA_ASISTENTE: 'Pregunta al asistente',
  CORREO_NO_ENVIADO: 'Correo no enviado',
  CORREO_VERIFICADO: 'Correo confirmado',
  FAVORITO_AGREGADO: 'Favorito agregado',
  FAVORITO_ELIMINADO: 'Favorito eliminado',
  HISTORIAL_ELIMINADO: 'Vuelo quitado del historial',
  HISTORIAL_VACIADO: 'Historial borrado',
  LOGIN_EXITOSO: 'Inicio de sesión',
  LOGIN_FALLIDO: 'Inicio de sesión fallido',
  LOGIN_GOOGLE: 'Inicio de sesión con Google',
  PERFIL_ACTUALIZADO: 'Perfil actualizado',
  REENVIO_VERIFICACION: 'Reenvío de confirmación',
  REGISTRO: 'Registro de cuenta',
  REGISTRO_GOOGLE: 'Registro con Google',
  RESTABLECER_FALLIDO: 'Cambio de contraseña fallido',
  RUTA_CALCULADA: 'Ruta calculada',
  RUTA_ELIMINADA: 'Ruta eliminada',
  RUTA_GUARDADA: 'Ruta guardada',
  SOLICITUD_RESTABLECER: 'Solicitud de nueva contraseña',
  VERIFICACION_FALLIDA: 'Confirmación de correo fallida',
  VUELO_CONSULTADO: 'Vuelo consultado',
};

@Component({
  selector: 'app-admincomponent',
  standalone: false,
  styleUrl: './admincomponent.css',
  templateUrl: './admincomponent.html',
})
export class Admincomponent implements OnInit {
  // Filtros del formulario (vacío = no filtrar)
  filtros: FiltrosAuditoria = { usuario: '', accion: '', resultado: '', desde: '', hasta: '' };
  tamano = 20;

  // Estado de la pantalla
  resultados = signal<PaginaAuditoria | null>(null);
  acciones = signal<string[]>([]);
  cargando = signal(false);
  error = signal('');

  constructor(
    private auditoria: AuditoriaService,
    private auth: AuthService,
  ) {}

  ngOnInit(): void {
    this.auditoria.acciones().subscribe({
      next: (lista) => this.acciones.set(lista),
      // El filtro de acciones es una ayuda: si falla, la tabla igual funciona
      error: () => {},
    });
    this.cargar(0);
  }

  // Pide una pagina al back con los filtros actuales
  cargar(pagina: number): void {
    this.cargando.set(true);
    this.error.set('');
    this.auditoria.listar(this.filtros, pagina, this.tamano).subscribe({
      next: (respuesta) => {
        this.resultados.set(respuesta);
        this.cargando.set(false);
      },
      error: (e: HttpErrorResponse) => {
        this.error.set(this.auth.mensajeDeError(e));
        this.cargando.set(false);
      },
    });
  }

  // Un filtro nuevo siempre empieza en la primera pagina
  buscar(): void {
    this.cargar(0);
  }

  limpiar(): void {
    this.filtros = { usuario: '', accion: '', resultado: '', desde: '', hasta: '' };
    this.cargar(0);
  }

  irA(pagina: number): void {
    const total = this.resultados()?.totalPaginas ?? 0;
    if (pagina >= 0 && pagina < total) {
      this.cargar(pagina);
    }
  }

  etiqueta(codigo: string): string {
    return ETIQUETAS[codigo] ?? codigo;
  }
}
