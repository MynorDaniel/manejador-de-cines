import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { JwtService } from './jwt-service';
import { Usuario } from '../models/usuario';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  
  http = inject(HttpClient);
  jwtService = inject(JwtService);

  URL = 'http://localhost:8080/manejador-cines-api/api/v1/usuarios';

  public crearUsuario(usuario:Usuario):Observable<void>{
    let headers:HttpHeaders = this.jwtService.getHeaders();
    return this.http.post<void>(this.URL, usuario, {headers});
  }
}
