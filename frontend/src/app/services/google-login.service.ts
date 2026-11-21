import { Injectable } from '@angular/core';

// Client ID de Google Cloud (es publico, no es un secreto): pegalo aqui
const GOOGLE_CLIENT_ID = '';

type TextoBoton = 'signin_with' | 'signup_with' | 'continue_with';

@Injectable({
  providedIn: 'root',
})
export class GoogleLoginService {
  // El script de Google se descarga una sola vez
  private script: Promise<void> | null = null;

  // Dibuja el boton oficial de Google. Devuelve false si no hay Client ID configurado
  async mostrarBoton(
    contenedor: HTMLElement,
    texto: TextoBoton,
    alRecibir: (credencial: string) => void,
  ): Promise<boolean> {
    if (!GOOGLE_CLIENT_ID) {
      return false;
    }
    await this.cargarScript();

    const google = (window as any).google;
    google.accounts.id.initialize({
      client_id: GOOGLE_CLIENT_ID,
      callback: (respuesta: { credential: string }) => alRecibir(respuesta.credential),
    });
    google.accounts.id.renderButton(contenedor, {
      type: 'standard',
      theme: 'filled_black',
      size: 'large',
      text: texto,
      shape: 'rectangular',
      locale: 'es',
      width: Math.min(contenedor.clientWidth || 320, 400),
    });
    return true;
  }

  private cargarScript(): Promise<void> {
    if (!this.script) {
      this.script = new Promise((resolver, rechazar) => {
        const etiqueta = document.createElement('script');
        etiqueta.src = 'https://accounts.google.com/gsi/client';
        etiqueta.async = true;
        etiqueta.onload = () => resolver();
        etiqueta.onerror = () => {
          this.script = null;
          rechazar(new Error('No se pudo cargar el script de Google'));
        };
        document.head.appendChild(etiqueta);
      });
    }
    return this.script;
  }
}
