import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { BoletoService } from '../../../services/boleto-service';
import { ProyeccionService } from '../../../services/proyeccion-service';
import { CineService } from '../../../services/cine-service';
import { SalaService } from '../../../services/sala-service';
import { PeliculaService } from '../../../services/pelicula-service';
import { Boleto } from '../../../models/boleto';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-mis-boletos-component',
  imports: [CommonModule, RouterLink],
  templateUrl: './mis-boletos-component.html',
  styleUrl: './mis-boletos-component.css'
})
export class MisBoletosComponent implements OnInit {

  boletoService = inject(BoletoService);
  proyeccionService = inject(ProyeccionService);
  cineService = inject(CineService);
  salaService = inject(SalaService);
  peliculaService = inject(PeliculaService);

  boletos: Boleto[] = [];
  cargando = true;
  errorMsg: string | null = null;

  ngOnInit(): void {
    this.cargarBoletos();
  }

  cargarBoletos(): void {
    this.boletoService.verMisBoletos().subscribe({
      next: (boletos) => {
        this.boletos = boletos;
        this.cargando = false;
        console.log(boletos);
        
      },
      error: (err) => {
        this.errorMsg = err.error || 'Error al cargar los boletos';
        this.cargando = false;
      }
    });
  }

  formatearFecha(fecha: string): string {
    const date = new Date(fecha);
    return date.toLocaleDateString('es-GT', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  }

  formatearHora(hora: string): string {
    return hora.substring(0, 5);
  }
}