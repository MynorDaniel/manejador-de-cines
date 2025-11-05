import { Component, inject } from '@angular/core';
import { CineService } from '../../../services/cine-service';
import { Cine } from '../../../models/cine';
import { Router } from '@angular/router';
import { AnuncioService } from '../../../services/anuncio-service';

@Component({
  selector: 'app-ver-cines-component',
  imports: [],
  templateUrl: './ver-cines-component.html',
  styleUrl: './ver-cines-component.css'
})
export class VerCinesComponent {

  cines: Cine[] = [];
  cargando = true;

  cineService = inject(CineService);
  router = inject(Router);
  anuncioService = inject(AnuncioService);

  ngOnInit(): void {
    this.cineService.verCines().subscribe({
      next: (cines) => {
        this.cines = cines;
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error al cargar cines', err);
        this.cargando = false;
      }
    });
  }

  verCine(cine: Cine): void {
    console.log('Cine seleccionado:', cine);
    this.router.navigate(['/ver-cine', cine.id]);
  }

}
