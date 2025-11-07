import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Sala } from '../../../models/sala';
import { SalaService } from '../../../services/sala-service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-ver-salas-component',
  imports: [CommonModule, RouterLink],
  templateUrl: './ver-salas-component.html',
  styleUrl: './ver-salas-component.css'
})
export class VerSalasComponent implements OnInit {
  
  salaService = inject(SalaService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  
  salas: Sala[] = [];
  cargando = true;
  idCine?: string;
  
  ngOnInit(): void {
    this.idCine = this.route.snapshot.paramMap.get('idCine') || undefined;
    
    if (this.idCine) {
      this.salaService.verSalasPorCine(this.idCine).subscribe({
        next: (salas) => {
          this.salas = salas;
          this.cargando = false;
        },
        error: (err) => {
          console.error('Error al cargar salas del cine:', err);
          this.cargando = false;
        }
      });
    } else {
      this.salaService.verSalas().subscribe({
        next: (salas) => {
          this.salas = salas;
          this.cargando = false;
        },
        error: (err) => {
          console.error('Error al cargar salas:', err);
          this.cargando = false;
        }
      });
    }
  }
  
  obtenerTextoBooleano(valor?: string): string {
    return valor === 'true' ? 'Sí' : 'No';
  }
}