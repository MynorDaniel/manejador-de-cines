import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Token } from '../models/token';
import { Credenciales } from '../models/credenciales';
import { Observable } from 'rxjs';
import { JwtService } from './jwt-service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  
  http = inject(HttpClient);
  jwtService = inject(JwtService);

  URL = 'http://localhost:8080/manejador-cines-api/api/v1/auth';

  public iniciarSesion(credenciales:Credenciales):Observable<Token>{
    let headers:HttpHeaders = this.jwtService.getHeaders();
    return this.http.post<Token>(this.URL, credenciales, {headers});
  }
}
