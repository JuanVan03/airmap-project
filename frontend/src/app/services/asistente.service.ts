import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MensajeChat } from '../models/mensaje-chat';
import { RespuestaAsistente } from '../models/respuesta-asistente';

@Injectable({
  providedIn: 'root',
})
export class AsistenteService {
  private readonly url = 'http://localhost:8080/api/asistente';

  constructor(private http: HttpClient) {}

  // Envia la pregunta, lo que se hablo antes y el vuelo en contexto (si hay)
  preguntar(mensaje: string, historial: MensajeChat[], vueloId: string | null): Observable<RespuestaAsistente> {
    const cuerpo = {
      mensaje,
      vueloId,
      // De cada mensaje anterior solo viajan el rol y el texto
      historial: historial.map((m) => ({ rol: m.rol, texto: m.texto })),
    };
    return this.http.post<RespuestaAsistente>(this.url, cuerpo);
  }
}
