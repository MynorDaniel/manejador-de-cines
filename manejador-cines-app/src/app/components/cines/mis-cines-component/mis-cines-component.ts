import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Cine } from '../../../models/cine';
import { CineService } from '../../../services/cine-service';
import { JwtService } from '../../../services/jwt-service';
import { Usuario } from '../../../models/usuario';

@Component({
  selector: 'app-mis-cines-component',
  imports: [],
  templateUrl: './mis-cines-component.html',
  styleUrl: './mis-cines-component.css'
})
export class MisCinesComponent implements OnInit {

  cines: Cine[] = [];
  cargando = true;

  cineService = inject(CineService);
  router = inject(Router);
  jwtService = inject(JwtService);

  usuario: Usuario = this.jwtService.getUsuarioActual();

  ngOnInit(): void {
    this.cineService.verCinesPorUsuario(this.usuario.id).subscribe({
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
