import { Component, inject, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Proyeccion } from '../../../models/proyeccion';
import { Sala } from '../../../models/sala';
import { Pelicula } from '../../../models/pelicula';
import { ProyeccionService } from '../../../services/proyeccion-service';
import { SalaService } from '../../../services/sala-service';
import { PeliculaService } from '../../../services/pelicula-service';

@Component({
  selector: 'app-editar-proyeccion-component',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './editar-proyeccion-component.html',
  styleUrl: './editar-proyeccion-component.css'
})
export class EditarProyeccionComponent implements OnInit {

  errorMsg: string | null = null;
  infoMsg: string | null = null;
  
  proyeccionService = inject(ProyeccionService);
  salaService = inject(SalaService);
  peliculaService = inject(PeliculaService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  
  salas: Sala[] = [];
  peliculas: Pelicula[] = [];
  proyeccion?: Proyeccion;
  idCine: string | null = null;
  cargando = true;
  
  proyeccionForm = new FormGroup({
    idSala: new FormControl('', [Validators.required]),
    idPelicula: new FormControl('', [Validators.required]),
    fecha: new FormControl('', [Validators.required]),
    hora: new FormControl('', [Validators.required])
  });
  
  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    
    if (!id) {
      this.errorMsg = 'ID de proyección no válido';
      this.cargando = false;
      return;
    }
    
    this.cargarProyeccion(id);
  }
  
  cargarProyeccion(id: string): void {
    this.proyeccionService.verProyeccion(id).subscribe({
      next: (proyeccion) => {
        this.proyeccion = proyeccion;
        
        this.salaService.verSala(proyeccion.idSala).subscribe({
          next: (sala) => {
            this.idCine = sala.idCine;
            this.cargarSalas();
            this.cargarPeliculas();
            this.cargarDatosEnFormulario();
          },
          error: (err) => {
            this.errorMsg = err.error || 'Error al cargar información de la sala';
            this.cargando = false;
          }
        });
      },
      error: (err) => {
        this.errorMsg = err.error || 'Error al cargar la proyección';
        this.cargando = false;
      }
    });
  }
  
  cargarDatosEnFormulario(): void {
    if (!this.proyeccion) return;
    
    this.proyeccionForm.patchValue({
      idSala: this.proyeccion.idSala,
      idPelicula: this.proyeccion.idPelicula,
      fecha: this.proyeccion.fecha,
      hora: this.proyeccion.hora
    });
  }
  
  cargarSalas(): void {
    if (!this.idCine) return;
    
    this.salaService.verSalasPorCine(this.idCine).subscribe({
      next: (salas) => {
        this.salas = salas;
        this.verificarCargaCompleta();
      },
      error: (err) => {
        this.errorMsg = err.error || 'Error al cargar las salas';
        this.cargando = false;
      }
    });
  }
  
  cargarPeliculas(): void {
    this.peliculaService.verPeliculas().subscribe({
      next: (peliculas) => {
        this.peliculas = peliculas;
        this.verificarCargaCompleta();
      },
      error: (err) => {
        this.errorMsg = err.error || 'Error al cargar las películas';
        this.cargando = false;
      }
    });
  }
  
  verificarCargaCompleta(): void {
    if (this.salas.length >= 0 && this.peliculas.length >= 0) {
      this.cargando = false;
    }
  }
  
  editarProyeccion(): void {
    if (this.proyeccionForm.invalid) {
      this.infoMsg = 'Complete todos los campos correctamente';
      return;
    }
    
    if (!this.proyeccion?.id) {
      this.errorMsg = 'No se pudo obtener el ID de la proyección';
      return;
    }
    
    const proyeccionActualizada: Proyeccion = {
      id: this.proyeccion.id,
      idSala: this.proyeccionForm.value.idSala!,
      idPelicula: this.proyeccionForm.value.idPelicula!,
      fecha: this.proyeccionForm.value.fecha!,
      hora: this.proyeccionForm.value.hora!
    };
    
    this.proyeccionService.editarProyeccion(proyeccionActualizada).subscribe({
      next: () => {
        this.infoMsg = 'Proyección actualizada exitosamente';
      },
      error: (err) => {
        this.errorMsg = err.error || 'Error al actualizar la proyección';
      }
    });
  }
  
  obtenerFechaMinima(): string {
    return new Date().toISOString().split('T')[0];
  }

  obtenerTituloPelicula(): string {
    return this.peliculas.find(p => p.id === this.proyeccionForm.value.idPelicula)?.titulo || 'N/A';
  }
  
  obtenerNombreSala(): string {
    return this.salas.find(s => s.id === this.proyeccionForm.value.idSala)?.id || 'N/A';
  }
}