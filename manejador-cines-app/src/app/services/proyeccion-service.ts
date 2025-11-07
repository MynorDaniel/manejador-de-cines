import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Proyeccion } from '../models/proyeccion';

@Injectable({
  providedIn: 'root'
})
export class ProyeccionService {
  
  http = inject(HttpClient);
  
  URL = 'http://localhost:8080/manejador-cines-api/api/v1/proyecciones';

  public crearProyeccion(proyeccion: Proyeccion): Observable<void> {
    return this.http.post<void>(this.URL, proyeccion);
  }

  public verProyeccion(id: string): Observable<Proyeccion> {
    return this.http.get<Proyeccion>(`${this.URL}/${id}`);
  }

  public editarProyeccion(proyeccion: Proyeccion): Observable<void> {
    return this.http.put<void>(this.URL, proyeccion);
  }
}