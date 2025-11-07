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

  ngOnInit(): void {
    
  }
}