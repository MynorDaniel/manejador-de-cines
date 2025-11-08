import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Pelicula } from '../../../models/pelicula';
import { Calificacion } from '../../../models/calificacion';
import { PeliculaService } from '../../../services/pelicula-service';
import { ImagenService } from '../../../services/imagen-service';
import { JwtService } from '../../../services/jwt-service';
import { Usuario } from '../../../models/usuario';

@Component({
  selector: 'app-ver-pelicula-component',
  imports: [CommonModule],
  templateUrl: './ver-pelicula-component.html',
  styleUrl: './ver-pelicula-component.css'
})
export class VerPeliculaComponent implements OnInit {

  errorMsg: string | null = null;
  infoMsg: string | null = null;
  
  peliculaService = inject(PeliculaService);
  imagenService = inject(ImagenService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  jwtService = inject(JwtService);
  
  pelicula?: Pelicula;
  imagenUrl: string | null = null;
  cargando = true;
  usuarioActual: Usuario = this.jwtService.getUsuarioActual();
  
  ngOnInit(): void {
    this.cargarPelicula();
  }
  
  cargarPelicula(): void {
    const id = this.route.snapshot.paramMap.get('id');
    
    if (id) {
      this.peliculaService.verPelicula(id).subscribe({
        next: (pelicula) => {
          this.pelicula = pelicula;
          this.cargarImagen();
        },
        error: (err) => {
          this.errorMsg = err.error || 'Error al cargar la película';
          this.cargando = false;
        }
      });
    }
  }
  
  cargarImagen(): void {
    if (!this.pelicula) return;
    
    this.imagenService.obtenerImagen(this.pelicula.imagen.id).subscribe({
      next: (blob) => {
        if (blob) {
          this.imagenUrl = URL.createObjectURL(blob);
        }
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error al cargar imagen:', err);
        this.cargando = false;
      }
    });
  }
  
  calcularPromedioCalificaciones(): number {
    if (!this.pelicula?.calificaciones || this.pelicula.calificaciones.length === 0) {
      return 0;
    }
    
    const suma = this.pelicula.calificaciones.reduce((ac, cal) => {
      return ac + Number(cal.valor);
    }, 0);
    
    return Math.round(suma / this.pelicula.calificaciones.length);
  }
  
  obtenerCalificacionUsuario(): number {
    if (!this.pelicula?.calificaciones || !this.usuarioActual.id) {
      return 0;
    }
    
    const calificacion = this.pelicula.calificaciones.find(
      cal => cal.idUsuario === this.usuarioActual.id?.toString()
    );
    
    return calificacion ? Number(calificacion.valor) : 0;
  }
  
  obtenerEstrellas(calificacion: number): number[] {
    return Array(5).fill(0).map((_, i) => i < calificacion ? 1 : 0);
  }
  
  seleccionarCalificacion(valor: number): void {
    if (!this.pelicula) return;
    
    if (valor >= 1 && valor <= 5) {
      this.guardarCalificacion(valor);
    }
  }
  
  private guardarCalificacion(valor: number): void {
    if (!this.pelicula) return;
    
    const calificacion: Calificacion = {
      valor: valor.toString()
    };
    
    this.peliculaService.calificarPelicula(this.pelicula.id!, calificacion).subscribe({
      next: () => {
        this.infoMsg = 'Calificación guardada exitosamente';
        this.cargarPelicula();
        
      },
      error: (err) => {
        this.errorMsg = err.error || 'Error al guardar la calificación';
      }
    });
  }
  
  editarPelicula(): void {
    if (this.pelicula) {
      this.router.navigate(['/editar-pelicula', this.pelicula.id]);
    }
  }
  
  convertirDuracion(minutos: string): string {
    const min = Number(minutos);
    const horas = Math.floor(min / 60);
    const minutosRestantes = min % 60;
    
    if (horas === 0) {
      return `${minutosRestantes} min`;
    }
    
    return `${horas}h ${minutosRestantes}min`;
  }
  
  ngOnDestroy(): void {
    if (this.imagenUrl) {
      URL.revokeObjectURL(this.imagenUrl);
    }
  }
}