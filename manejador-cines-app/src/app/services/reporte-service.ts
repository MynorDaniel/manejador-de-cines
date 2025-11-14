import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SalaReporte } from '../models/sala-reporte';
import { ReporteGanancias } from '../models/reporte-ganancias';

@Injectable({
  providedIn: 'root'
})
export class ReporteService {
  
  http = inject(HttpClient);
  
  URL = 'http://localhost:8080/manejador-cines-api/api/v1/reportes';

  verReporteAdminCines(fechaInicial?: string, fechaFinal?: string, idCine?: string, tipo?: string): Observable<Blob> {
    let params = new HttpParams();
    if (fechaInicial) params = params.set('fecha-inicial', fechaInicial);
    if (fechaFinal) params = params.set('fecha-final', fechaFinal);
    if (idCine) params = params.set('id-cine', idCine);
    if (tipo) params = params.set('tipo', tipo);

    return this.http.get(`${this.URL}/admin-cines`, {
      params,
      responseType: 'blob'
    });
  }

  verReporteAdminSistema(fechaInicial?: string, fechaFinal?: string, tipo?: string): Observable<Blob> {
    let params = new HttpParams();
    if (fechaInicial) params = params.set('fecha-inicial', fechaInicial);
    if (fechaFinal) params = params.set('fecha-final', fechaFinal);
    if (tipo) params = params.set('tipo', tipo);

    return this.http.get(`${this.URL}/admin-sistema`, {
      params,
      responseType: 'blob'
    });
  }

  verReporteGanancias(pdf: string, fechaInicial?: string, fechaFinal?: string): Observable<any> {
    let params = new HttpParams();
    if (fechaInicial) params = params.set('fecha-inicial', fechaInicial);
    if (fechaFinal) params = params.set('fecha-final', fechaFinal);

    if (pdf === "true") {
      return this.http.get(`${this.URL}/ganancias/${pdf}`, { 
        params, 
        responseType: 'blob' as 'json'
      });
    } else {
      return this.http.get<ReporteGanancias>(`${this.URL}/ganancias/${pdf}`, { params });
    }
  }

  verReporteComentariosSalas(pdf: string, fechaInicial?: string, fechaFinal?: string, idSala?: string): Observable<any> {
    let params = new HttpParams();
    if (fechaInicial) params = params.set('fecha-inicial', fechaInicial);
    if (fechaFinal) params = params.set('fecha-final', fechaFinal);
    if (idSala) params = params.set('id-sala', idSala);

    if (pdf === "true") {
      return this.http.get(`${this.URL}/comentarios-salas/${pdf}`, { 
        params, 
        responseType: 'blob' as 'json'
      });
    } else {
      return this.http.get<SalaReporte[]>(`${this.URL}/comentarios-salas/${pdf}`, { params });
    }
  }

  verReportePeliculasProyectadas(pdf: string, fechaInicial?: string, fechaFinal?: string, idSala?: string): Observable<any> {
    let params = new HttpParams();
    if (fechaInicial) params = params.set('fecha-inicial', fechaInicial);
    if (fechaFinal) params = params.set('fecha-final', fechaFinal);
    if (idSala) params = params.set('id-sala', idSala);

    if (pdf === "true") {
      return this.http.get(`${this.URL}/peliculas-proyectadas/${pdf}`, { 
        params, 
        responseType: 'blob' as 'json'
      });
    } else {
      return this.http.get<SalaReporte[]>(`${this.URL}/peliculas-proyectadas/${pdf}`, { params });
    }
  }

  verReporteSalasMasGustadas(pdf: string, fechaInicial?: string, fechaFinal?: string, idSala?: string): Observable<any> {
    let params = new HttpParams();
    if (fechaInicial) params = params.set('fecha-inicial', fechaInicial);
    if (fechaFinal) params = params.set('fecha-final', fechaFinal);
    if (idSala) params = params.set('id-sala', idSala);

    if (pdf === "true") {
      return this.http.get(`${this.URL}/salas-mas-gustadas/${pdf}`, { 
        params, 
        responseType: 'blob' as 'json'
      });
    } else {
      return this.http.get<SalaReporte[]>(`${this.URL}/salas-mas-gustadas/${pdf}`, { params });
    }
  }

  verReporteBoletoVendidos(pdf: string, fechaInicial?: string, fechaFinal?: string, idSala?: string): Observable<any> {
    let params = new HttpParams();
    if (fechaInicial) params = params.set('fecha-inicial', fechaInicial);
    if (fechaFinal) params = params.set('fecha-final', fechaFinal);
    if (idSala) params = params.set('id-sala', idSala);

    if (pdf === "true") {
      return this.http.get(`${this.URL}/boletos-vendidos/${pdf}`, { 
        params, 
        responseType: 'blob' as 'json'
      });
    } else {
      return this.http.get<SalaReporte[]>(`${this.URL}/boletos-vendidos/${pdf}`, { params });
    }
  }
}