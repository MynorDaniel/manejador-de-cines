import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Comentario } from '../models/comentario';
import { ComentarioSala } from '../models/comentario-sala';
import { ComentarioPelicula } from '../models/comentario-pelicula';

@Injectable({
  providedIn: 'root'
})
export class ComentarioService {

  http = inject(HttpClient);

  URL = 'http://localhost:8080/manejador-cines-api/api/v1/comentarios';

  crearComentarioSala(comentarioSala: ComentarioSala): Observable<void> {
    return this.http.post<void>(`${this.URL}/sala`, comentarioSala);
  }

  crearComentarioPelicula(comentarioPelicula: ComentarioPelicula): Observable<void> {
    return this.http.post<void>(`${this.URL}/pelicula`, comentarioPelicula);
  }

  editarComentario(comentario: Comentario): Observable<void> {
    return this.http.patch<void>(this.URL, comentario);
  }

  eliminarComentario(id: number): Observable<void> {
    return this.http.delete<void>(`${this.URL}/${id}`);
  }

  verComentario(id: number): Observable<Comentario> {
    return this.http.get<Comentario>(`${this.URL}/${id}`);
  }

  verComentariosSala(idSala: number): Observable<ComentarioSala[]> {
    return this.http.get<ComentarioSala[]>(`${this.URL}/sala/${idSala}`);
  }

  verComentariosPelicula(idPelicula: number): Observable<ComentarioPelicula[]> {
    return this.http.get<ComentarioPelicula[]>(`${this.URL}/pelicula/${idPelicula}`);
  }
}