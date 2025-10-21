import { HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Usuario } from '../models/usuario';

@Injectable({
  providedIn: 'root'
})
export class JwtService {
  
  public getHeaders(): HttpHeaders {
    const tokenObject = sessionStorage.getItem('token');
    const token = tokenObject ? JSON.parse(tokenObject).token : null;

    let headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }

    return headers;
  }

  public getUsuarioActual():Usuario{
    const tokenObject = sessionStorage.getItem('token');
    const usuario = tokenObject ? JSON.parse(tokenObject).usuario : null;
    return usuario;
  }
}
