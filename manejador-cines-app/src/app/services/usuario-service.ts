import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { JwtService } from './jwt-service';
import { Usuario } from '../models/usuario';
import { Observable } from 'rxjs';
import { Imagen } from '../models/imagen';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  
  http = inject(HttpClient);
  
  URL = 'http://localhost:8080/manejador-cines-api/api/v1/usuarios';

  public crearUsuario(usuario:Usuario):Observable<void>{
    return this.http.post<void>(this.URL, usuario);
  }

  public obtenerUsuario(id:number):Observable<Usuario>{
    return this.http.get<Usuario>(this.URL + "/" + id);
  }

  public obtenerUsuarios():Observable<Usuario[]>{
    return this.http.get<Usuario[]>(this.URL);
  }

  public editarUsuario(usuario:Usuario):Observable<void>{
    return this.http.put<void>(this.URL, usuario)
  }

  public editarImagen(imagen: Imagen) :Observable<void> {
    return this.http.patch<void>(this.URL, imagen);
  }

  public desactivarCuenta(id:number):Observable<void>{
    return this.http.delete<void>(this.URL + "/" + id);
  }
}
