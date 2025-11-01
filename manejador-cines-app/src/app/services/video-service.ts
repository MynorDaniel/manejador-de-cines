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

  public obtenerVideo(id: number): Observable<Video> {
    return this.http.get<Video>(`${this.URL}/${id}`);
  }

  public subirVideo(video: Video):Observable<Video>{
    return this.http.post<Video>(this.URL, video);
  }
}
