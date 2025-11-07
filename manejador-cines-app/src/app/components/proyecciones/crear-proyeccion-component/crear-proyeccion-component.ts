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
  selector: 'app-crear-proyeccion-component',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './crear-proyeccion-component.html',
  styleUrl: './crear-proyeccion-component.css'
})
export class CrearProyeccionComponent implements OnInit {

  errorMsg: string | null = null;
  infoMsg: string | null = null;
  
  proyeccionService = inject(ProyeccionService);
  salaService = inject(SalaService);
  peliculaService = inject(PeliculaService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  
  salas: Sala[] = [];
  peliculas: Pelicula[] = [];
  idCine: string | null = null;
  cargando = true;
  
  proyeccionForm = new FormGroup({
    idSala: new FormControl('', [Validators.required]),
    idPelicula: new FormControl('', [Validators.required]),
    fecha: new FormControl('', [Validators.required]),
    hora: new FormControl('', [Validators.required])
  });
  
  ngOnInit(): void {
    this.idCine = this.route.snapshot.paramMap.get('idCine');
    
    if (!this.idCine) {
      this.errorMsg = 'ID de cine no válido';
      this.cargando = false;
      return;
    }
    
    this.cargarSalas();
    this.cargarPeliculas();
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
  
  crearProyeccion(): void {
    if (this.proyeccionForm.invalid) {
      this.infoMsg = 'Complete todos los campos correctamente';
      return;
    }
    
    const proyeccion: Proyeccion = {
      idSala: this.proyeccionForm.value.idSala!,
      idPelicula: this.proyeccionForm.value.idPelicula!,
      fecha: this.proyeccionForm.value.fecha!,
      hora: this.proyeccionForm.value.hora!
    };
    
    this.proyeccionService.crearProyeccion(proyeccion).subscribe({
      next: () => {
        this.infoMsg = 'Proyección creada exitosamente';
        this.proyeccionForm.reset();
      },
      error: (err) => {
        this.errorMsg = err.error || 'Error al crear la proyección';
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