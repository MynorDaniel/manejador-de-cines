import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
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

  mostrarAnuncios = signal(true);

  setMostrarAnuncios(valor: boolean) {
    this.mostrarAnuncios.set(valor);
  }
  
  public verPreciosDeTipos() : Observable<TipoAnuncio[]> {
    return this.http.get<TipoAnuncio[]>(this.URL + '/precios/tipos');
  }

  public verPreciosDeVigencias() : Observable<VigenciaAnuncio[]> {
    return this.http.get<VigenciaAnuncio[]>(this.URL + '/precios/vigencias');
  }

  public crearAnuncio(anuncio: AnuncioI) : Observable<void> {
    return this.http.post<void>(this.URL, anuncio);
  }

  public verAnuncios() : Observable<AnuncioI[]> {
    return this.http.get<AnuncioI[]>(this.URL);
  }

  public verMostrables() : Observable<AnuncioI[]> {
    return this.http.get<AnuncioI[]>(this.URL + '/mostrables');
  }

  public verPropios() : Observable<AnuncioI[]> {
    return this.http.get<AnuncioI[]>(this.URL + '/propios');
  }

  public editarAnuncio(anuncio: AnuncioI) : Observable<void> {
    return this.http.put<void>(this.URL, anuncio);
  }

  public cambiarPrecioTipoAnuncio(tipo: TipoAnuncio) : Observable<void> {
    return this.http.put<void>(this.URL + '/precio-tipo', tipo);
  }

  public cambiarPrecioVigenciaAnuncio(vigencia: VigenciaAnuncio) : Observable<void> {
    return this.http.put<void>(this.URL + '/precio-vigencia', vigencia);
  }

}
