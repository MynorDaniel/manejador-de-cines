import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FiltrosReporteAdminCines } from '../models/filtros-reporte-admin-cines';
import { FiltrosReporteAdminSistema } from '../models/filtros-reporte-admin-sistema';

@Injectable({
  providedIn: 'root'
})
export class ReporteService {
  
  http = inject(HttpClient);
  
  URL = 'http://localhost:8080/manejador-cines-api/api/v1/reportes';

  verReporteAdminCines(filtros: FiltrosReporteAdminCines, tipo: string): Observable<Blob> {
    return this.http.post(`${this.URL}/admin-cines/${tipo}`, filtros, {
      responseType: 'blob'
    });
  }

  verReporteAdminSistema(filtros: FiltrosReporteAdminSistema, tipo: string): Observable<Blob> {
    return this.http.post(`${this.URL}/admin-sistema/${tipo}`, filtros, {
      responseType: 'blob'
    });
  }
}
