import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Boleto } from '../models/boleto';

@Injectable({
  providedIn: 'root'
})
export class BoletoService {
  http = inject(HttpClient);
  
  URL = 'http://localhost:8080/manejador-cines-api/api/v1/boletos';

  comprarBoleto(boleto: Boleto): Observable<void> {
    return this.http.post<void>(this.URL, boleto);
  }

  verMisBoletos(): Observable<Boleto[]> {
    return this.http.get<Boleto[]>(this.URL);
  }
}
