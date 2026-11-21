import { HttpErrorResponse } from '@angular/common/http';
import { Component, ElementRef, OnInit, ViewChild, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MensajeChat } from '../../../models/mensaje-chat';
import { Vuelo } from '../../../models/vuelo';
import { AsistenteService } from '../../../services/asistente.service';
import { VueloService } from '../../../services/vuelo.service';

@Component({
  selector: 'app-asistentecomponent',
  standalone: false,
  styleUrl: './asistentecomponent.css',
  templateUrl: './asistentecomponent.html',
})
export class Asistentecomponent implements OnInit {
  // Mensajes de la conversacion (el saludo inicial va fijo en el html)
  mensajes = signal<MensajeChat[]>([]);
  // Vuelo del que venimos, el asistente lo tiene en cuenta
  vuelo = signal<Vuelo | null>(null);
  // Es true mientras esperamos la respuesta
  escribiendo = signal(false);
  // Texto que escribe el usuario
  texto = '';

  // Preguntas para empezar con un clic
  sugerencias = [
    '¿Qué vuelos hay de Bogotá a Madrid?',
    '¿Cómo llego al aeropuerto El Dorado?',
    '¿Qué aerolíneas vuelan a Panamá?',
    '¿Qué equipaje puedo llevar en un vuelo?',
  ];

  @ViewChild('chat') chat!: ElementRef<HTMLDivElement>;

  constructor(
    private route: ActivatedRoute,
    private asistenteService: AsistenteService,
    private vueloService: VueloService,
  ) {}

  ngOnInit(): void {
    // Si llegamos desde un vuelo, la URL trae su id: /asistente?vuelo=ID
    const idVuelo = this.route.snapshot.queryParamMap.get('vuelo');
    if (idVuelo) {
      this.vueloService.getVuelo(idVuelo).subscribe({
        next: (vuelo) => this.vuelo.set(vuelo),
        // El vuelo en contexto es opcional: si falla, el chat funciona igual
        error: () => {},
      });
    }
  }

  nuevaConversacion(): void {
    this.mensajes.set([]);
    this.texto = '';
  }

  enviar(texto: string): void {
    const pregunta = texto.trim();
    if (pregunta === '' || this.escribiendo()) {
      return;
    }

    // El historial es lo que se hablo antes de esta pregunta
    const historial = this.mensajes();
    this.mensajes.update((lista) => [...lista, { rol: 'usuario', texto: pregunta }]);
    this.texto = '';
    this.escribiendo.set(true);
    this.bajarChat();

    this.asistenteService.preguntar(pregunta, historial, this.vuelo()?.id ?? null).subscribe({
      next: (respuesta) => {
        this.mensajes.update((lista) => [
          ...lista,
          { rol: 'asistente', texto: respuesta.respuesta, vuelos: respuesta.vuelos },
        ]);
        this.escribiendo.set(false);
        this.bajarChat();
      },
      error: (e: HttpErrorResponse) => {
        // Si el back explica el error lo mostramos, si no usamos un texto general
        const aviso = e.error?.mensaje ?? 'No pude responder en este momento. Intenta de nuevo en unos segundos.';
        this.mensajes.update((lista) => [...lista, { rol: 'asistente', texto: aviso }]);
        this.escribiendo.set(false);
        this.bajarChat();
      },
    });
  }

  // Espera a que Angular dibuje el mensaje y baja hasta el final de la conversacion
  private bajarChat(): void {
    setTimeout(() => {
      this.chat.nativeElement.scrollTop = this.chat.nativeElement.scrollHeight;
    });
  }
}
