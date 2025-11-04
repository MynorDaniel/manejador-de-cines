import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Cine } from '../models/cine';
import { Observable } from 'rxjs';
import { BloqueoAnunciosCine } from '../models/bloqueo-anuncios-cine';
import { CostoDiarioCine } from '../models/costo-diario';
import { CostoGlobalCines } from '../models/costo-global';
import { Pago } from '../models/pago';

@Injectable({
  providedIn: 'root'
})
export class CineService {
  http = inject(HttpClient);
  
  URL = 'http://localhost:8080/manejador-cines-api/api/v1/cines';
  
  public crearCine(cine: Cine) : Observable<void> {
    return this.http.post<void>(this.URL, cine);
  }

  public verCines() : Observable<Cine[]> {
    return this.http.get<Cine[]>(this.URL);
  }

  public verCine(id:number) : Observable<Cine> {
    return this.http.get<Cine>(this.URL + '/' + id);
  }

  public verCinesPorUsuario(id:number) : Observable<Cine[]> {
    return this.http.get<Cine[]>(this.URL + '/usuario/' + id);
  }

  public editarCine(cine: Cine) : Observable<void> {
    return this.http.put<void>(this.URL, cine);
  }

  public bloquearAnuncios(bloqueo: BloqueoAnunciosCine) : Observable<void> {
    return this.http.post<void>(this.URL + '/bloqueo-anuncios', bloqueo);
  }

  public verBloqueoAnuncios(idCine: number) : Observable<BloqueoAnunciosCine> {
    return this.http.get<BloqueoAnunciosCine>(this.URL + '/bloqueo-anuncios/' + idCine);
  }

  public crearCostoDiario(costoDiario:CostoDiarioCine) : Observable<void> {
    return this.http.post<void>(this.URL + '/costo-diario', costoDiario);
  }

  public cambiarCostoGlobal(costoGlobal:CostoGlobalCines) : Observable<void> {
    return this.http.put<void>(this.URL + '/costo-global', costoGlobal);
  }

  public verCostoGlobal() : Observable<CostoGlobalCines> {
    return this.http.get<CostoGlobalCines>(this.URL + '/costo-global');
  }

  public pagarCostoDiario(idCine:number) : Observable<void> {
    return this.http.post<void>(this.URL + '/pago/' + idCine, null);
  }

  public verDeuda(idCine:number) : Observable<Pago> {
    return this.http.get<Pago>(this.URL + '/deuda/' + idCine);
  }
}
