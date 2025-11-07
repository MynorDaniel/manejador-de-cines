import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Sala } from '../models/sala';
import { Calificacion } from '../models/calificacion';

@Injectable({
  providedIn: 'root'
})
export class SalaService {
  
  http = inject(HttpClient);
  
  URL = 'http://localhost:8080/manejador-cines-api/api/v1/salas';
  
  public crearSala(sala: Sala): Observable<void> {
    return this.http.post<void>(this.URL, sala);
  }
  
  public verSalas(): Observable<Sala[]> {
    return this.http.get<Sala[]>(this.URL);
  }
  
  public verSala(id: string): Observable<Sala> {
    return this.http.get<Sala>(`${this.URL}/${id}`);
  }
  
  public verSalasPorCine(idCine: string): Observable<Sala[]> {
    return this.http.get<Sala[]>(`${this.URL}/cine/${idCine}`);
  }
  
  public editarSala(sala: Sala): Observable<void> {
    return this.http.put<void>(this.URL, sala);
  }
  
  public calificarSala(calificacion: Calificacion): Observable<void> {
    return this.http.put<void>(`${this.URL}/calificacion`, calificacion);
  }
  
  public eliminarCalificacion(calificacion: Calificacion): Observable<void> {
    return this.http.delete<void>(`${this.URL}/calificacion`, { 
      body: calificacion
    });
  }
}