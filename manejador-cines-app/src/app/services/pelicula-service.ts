import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Calificacion } from '../models/calificacion';
import { Categoria } from '../models/categoria';
import { Clasificacion } from '../models/clasificacion';
import { FiltrosPelicula } from '../models/filtros-pelicula';
import { Pelicula } from '../models/pelicula';

@Injectable({
  providedIn: 'root'
})
export class PeliculaService {
  
  http = inject(HttpClient);
  
  URL = 'http://localhost:8080/manejador-cines-api/api/v1/peliculas';

  // POST /peliculas - Crear película
  public crearPelicula(pelicula: Pelicula): Observable<void> {
    return this.http.post<void>(this.URL, pelicula);
  }

  public verPeliculas(filtros?: FiltrosPelicula): Observable<Pelicula[]> {
    let params = new HttpParams();
    
    if (filtros?.id) {
      params = params.set('id', filtros.id);
    }
    if (filtros?.titulo) {
      params = params.set('titulo', filtros.titulo);
    }
    if (filtros?.idCategoria) {
      params = params.set('idCategoria', filtros.idCategoria);
    }

    return this.http.get<Pelicula[]>(this.URL, { params });
  }

  public verPelicula(id: string): Observable<Pelicula> {
    return this.http.get<Pelicula>(`${this.URL}/${id}`);
  }

  public editarPelicula(pelicula: Pelicula): Observable<void> {
    return this.http.put<void>(this.URL, pelicula);
  }

  public eliminarPelicula(id: string): Observable<void> {
    return this.http.delete<void>(`${this.URL}/${id}`);
  }

  public calificarPelicula(id: string, calificacion: Calificacion): Observable<void> {
    return this.http.post<void>(`${this.URL}/calificacion/${id}`, calificacion);
  }

  public verClasificaciones(): Observable<Clasificacion[]> {
    return this.http.get<Clasificacion[]>(`${this.URL}/clasificaciones`);
  }

  public verCategorias(): Observable<Categoria[]> {
    return this.http.get<Categoria[]>(`${this.URL}/categorias`);
  }
}
