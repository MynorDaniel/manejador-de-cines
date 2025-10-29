import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TipoAnuncio } from '../models/tipo-anuncio';
import { VigenciaAnuncio } from '../models/vigencia-anuncio';
import { AnuncioI } from '../models/anuncio-i';

@Injectable({
  providedIn: 'root'
})
export class AnuncioService {
  http = inject(HttpClient);
  
  URL = 'http://localhost:8080/manejador-cines-api/api/v1/anuncios';
  
  public verPreciosDeTipos() : Observable<TipoAnuncio[]> {
    return this.http.get<TipoAnuncio[]>(this.URL + '/precios/tipos');
  }

  public verPreciosDeVigencias() : Observable<VigenciaAnuncio[]> {
    return this.http.get<VigenciaAnuncio[]>(this.URL + '/precios/vigencias');
  }

  public crearAnuncio(anuncio: AnuncioI) : Observable<void> {
    return this.http.post<void>(this.URL, anuncio);
  }
}
