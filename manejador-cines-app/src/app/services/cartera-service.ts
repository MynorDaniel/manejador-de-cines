import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { CarteraI } from '../models/cartera';

@Injectable({
  providedIn: 'root'
})
export class CarteraService {

  http = inject(HttpClient);
  
  URL = 'http://localhost:8080/manejador-cines-api/api/v1/cartera';
  
  public editarCartera(cartera: CarteraI) : Observable<void> {
    return this.http.put<void>(this.URL, cartera);
  }

  public obtenerCartera():Observable<CarteraI>{
    return this.http.get<CarteraI>(this.URL);
  }
}
