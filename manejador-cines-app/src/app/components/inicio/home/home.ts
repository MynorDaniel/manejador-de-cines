import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormGroup, FormControl, ReactiveFormsModule } from '@angular/forms';
import { PeliculaService } from '../../../services/pelicula-service';
import { ImagenService } from '../../../services/imagen-service';
import { Pelicula } from '../../../models/pelicula';
import { Categoria } from '../../../models/categoria';
import { FiltrosPelicula } from '../../../models/filtros-pelicula';

@Component({
  selector: 'app-home',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit {

  peliculaService = inject(PeliculaService);
  imagenService = inject(ImagenService);
  router = inject(Router);
  
  peliculas: Pelicula[] = [];
  categorias: Categoria[] = [];
  imagenesUrl: Map<number, string> = new Map();
  errorMsg: string | null = null;
  cargando: boolean = true;

  filtrosForm = new FormGroup({
    titulo: new FormControl(''),
    idCategoria: new FormControl('')
  });

  ngOnInit(): void {
    this.cargarCategorias();
    this.cargarPeliculas();
  }

  cargarCategorias(): void {
    this.peliculaService.verCategorias().subscribe({
      next: (categorias) => {
        this.categorias = categorias;
      },
      error: (err) => {
        console.error('Error al cargar categorías:', err);
      }
    });
  }

  cargarPeliculas(): void {
    this.cargando = true;
    this.imagenesUrl.clear();
    this.peliculas = [];
    
    const filtros: FiltrosPelicula = {};
    
    const titulo = this.filtrosForm.value.titulo?.trim();
    const idCategoria = this.filtrosForm.value.idCategoria;
    
    if (titulo) {
      filtros.titulo = titulo;
    }
    if (idCategoria) {
      filtros.idCategoria = idCategoria;
    }
    
    this.peliculaService.verPeliculas(filtros).subscribe({
      next: (peliculas) => {
        this.peliculas = peliculas;
        this.errorMsg = null;
        this.cargarImagenes();
      },
      error: (err) => {
        this.peliculas = [];
        this.errorMsg = err.error || 'Error al cargar las películas';
        this.cargando = false;
      }
    });
  }

  buscar(): void {
    this.cargarPeliculas();
  }

  limpiarFiltros(): void {
    this.filtrosForm.reset();
    this.cargarPeliculas();
  }

  cargarImagenes(): void {
    if (this.peliculas.length === 0) {
      this.cargando = false;
      return;
    }

    const promesas = this.peliculas.map(pelicula => {
      return this.imagenService.obtenerImagen(pelicula.imagen.id).toPromise()
        .then(blob => {
          if (blob) {
            const url = URL.createObjectURL(blob);
            this.imagenesUrl.set(pelicula.imagen.id, url);
          }
        })
        .catch(err => {
          console.error(`Error al cargar imagen ${pelicula.imagen.id}:`, err);
        });
    });

    Promise.all(promesas).then(() => {
      this.cargando = false;
    });
  }

  obtenerUrlImagen(idImagen: number): string | undefined {
    return this.imagenesUrl.get(idImagen);
  }

  verDetalle(id: string): void {
    this.router.navigate(['/ver-pelicula', id]);
  }

  ngOnDestroy(): void {
    this.imagenesUrl.forEach(url => URL.revokeObjectURL(url));
  }
}