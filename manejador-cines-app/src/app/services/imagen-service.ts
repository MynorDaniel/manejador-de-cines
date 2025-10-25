import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Imagen } from '../models/imagen';

@Injectable({
  providedIn: 'root'
})
export class ImagenService {
  
  http = inject(HttpClient);

  URL = 'http://localhost:8080/manejador-cines-api/api/v1/imagenes';

  public obtenerImagen(id: number): Observable<Blob> {
    return this.http.get(`${this.URL}/${id}`, { responseType: 'blob' });
  }

  public subirImagen(imagen:File):Observable<Imagen>{
    const formData = new FormData();
    formData.append('file', imagen, imagen.name);
    return this.http.post<Imagen>(this.URL, formData);
  }
}
