import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Video } from '../models/video';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class VideoService {
  http = inject(HttpClient);

  URL = 'http://localhost:8080/manejador-cines-api/api/v1/videos';

  //public obtenerImagen(id: number): Observable<Blob> {
    //return this.http.get(`${this.URL}/${id}`, { responseType: 'blob' });
  //}

  public subirVideo(video: Video):Observable<Video>{
    return this.http.post<Video>(this.URL, video);
  }
}
