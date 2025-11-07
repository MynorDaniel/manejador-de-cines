import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Sala } from '../../../models/sala';
import { SalaService } from '../../../services/sala-service';
import { CommonModule } from '@angular/common';
import { JwtService } from '../../../services/jwt-service';
import { Usuario } from '../../../models/usuario';
import { Calificacion } from '../../../models/calificacion';
import { AnuncioService } from '../../../services/anuncio-service';
import { CineService } from '../../../services/cine-service';
import { Cine } from '../../../models/cine';

@Component({
  selector: 'app-ver-sala-component',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './ver-sala-component.html',
  styleUrl: './ver-sala-component.css'
})
export class VerSalaComponent implements OnInit {

  errorMsg: string | null = null;
  infoMsg: string | null = null;
  
  salaService = inject(SalaService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  jwtService = inject(JwtService);

  cineService = inject(CineService);
  anuncioService = inject(AnuncioService);
  cine?: Cine;
  
  sala?: Sala;
  cargando = true;
  usuarioActual: Usuario = this.jwtService.getUsuarioActual();
  
  ngOnInit(): void {
    this.cargarSala();

    
  }

  ngOnDestroy() {
    this.anuncioService.setMostrarAnuncios(true);
  }
  
  cargarSala(): void {
    const id = this.route.snapshot.paramMap.get('id');
    
    if (id) {
      this.salaService.verSala(id).subscribe({
        next: (sala) => {
          this.sala = sala;
          console.log(sala);
          
          this.cargando = false;

          this.cineService.verCine(Number(this.sala?.idCine)).subscribe({
            next: (cine) => {
              this.cine = cine;
              console.log("cine en sala: ", this.cine);
              
              if (cine.bloqueoActivo != undefined) {
                this.anuncioService.setMostrarAnuncios(!cine.bloqueoActivo);
              }
            },
            error: (err) => {
              console.error('Error al cargar cine:', err);
            }
          });
        },
        error: (err) => {
          this.errorMsg = err.error || 'Error al cargar la sala';
          this.cargando = false;
        }
      });
    }
  }
  
  obtenerTextoBooleano(valor?: string): string {
    return valor === 'true' ? 'Sí' : 'No';
  }
  
  obtenerEstrellas(calificacion?: number): number[] {
    const valor = calificacion || 0;
    return Array(5).fill(0).map((_, i) => i < valor ? 1 : 0);
  }
  
  editarSala(sala: Sala) {
    this.router.navigate(['/editar-sala', sala.id]);
  }
  
  seleccionarCalificacion(valor: number): void {
    if (!this.sala || this.sala.calificacionesBloqueadas === 'true') {
      return;
    }
    
    if (valor === 0) {
      this.eliminarCalificacion();
    } 
    else if (valor >= 1 && valor <= 5) {
      this.guardarCalificacion(valor);
    }
  }
  
  private guardarCalificacion(valor: number): void {
    if (!this.sala) {
      return;
    }
    
    const calificacion: Calificacion = {
      valor: valor.toString(),
      idSala: this.sala.id
    };
    
    this.salaService.calificarSala(calificacion).subscribe({
      next: () => {
        this.infoMsg = 'Calificación guardada exitosamente';
        this.cargarSala();
      },
      error: (err) => {
        this.errorMsg = err.error || 'Error al guardar la calificación';
      }
    });
  }
  
  private eliminarCalificacion(): void {
    if (!this.sala || !this.sala.idCalificacionUsuario) {
      this.errorMsg = 'No hay calificación para eliminar';
      return;
    }
    
    const calificacion: Calificacion = {
      id: String(this.sala.idCalificacionUsuario),
      idUsuario: this.usuarioActual.id?.toString(),
      valor: '0',
      idSala: this.sala.id
    };
    
    this.salaService.eliminarCalificacion(calificacion).subscribe({
      next: () => {
        this.infoMsg = 'Calificación eliminada exitosamente';
        this.cargarSala();
      },
      error: (err) => {
        this.errorMsg = err.error || 'Error al eliminar la calificación';
      }
    });
  }
}