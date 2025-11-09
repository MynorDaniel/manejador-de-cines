import { Component, inject, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule, FormArray } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Pelicula } from '../../../models/pelicula';
import { Clasificacion } from '../../../models/clasificacion';
import { Categoria } from '../../../models/categoria';
import { Imagen } from '../../../models/imagen';
import { PeliculaService } from '../../../services/pelicula-service';
import { ImagenService } from '../../../services/imagen-service';

@Component({
  selector: 'app-crear-pelicula-component',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './crear-pelicula-component.html',
  styleUrl: './crear-pelicula-component.css'
})
export class CrearPeliculaComponent implements OnInit {

  errorMsg: string | null = null;
  infoMsg: string | null = null;
  
  peliculaService = inject(PeliculaService);
  imagenService = inject(ImagenService);
  router = inject(Router);
  
  clasificaciones: Clasificacion[] = [];
  categorias: Categoria[] = [];
  categoriasSeleccionadas: Categoria[] = [];
  
  imagenSeleccionada: File | null = null;
  imagenPreview: string | null = null;
  
  peliculaForm = new FormGroup({
    clasificacion: new FormControl('', [Validators.required]),
    titulo: new FormControl('', [Validators.required, Validators.maxLength(200)]),
    sinopsis: new FormControl('', [Validators.required]),
    fechaEstreno: new FormControl('', [Validators.required]),
    duracion: new FormControl('', [Validators.required, Validators.min(1)]),
    director: new FormControl('', [Validators.required, Validators.maxLength(200)]),
    actores: new FormArray([
      new FormControl('', [Validators.required, this.validarSinComas])
    ])
  });
  
  ngOnInit(): void {
    this.cargarClasificaciones();
    this.cargarCategorias();
  }
  
  get actores(): FormArray {
    return this.peliculaForm.get('actores') as FormArray;
  }
  
  cargarClasificaciones(): void {
    this.peliculaService.verClasificaciones().subscribe({
      next: (clasificaciones) => {
        this.clasificaciones = clasificaciones;
      },
      error: (err) => {
        this.errorMsg = err.error || 'Error al cargar clasificaciones';
      }
    });
  }
  
  cargarCategorias(): void {
    this.peliculaService.verCategorias().subscribe({
      next: (categorias) => {
        this.categorias = categorias;
      },
      error: (err) => {
        this.errorMsg = err.error || 'Error al cargar categorías';
      }
    });
  }
  
  onImagenSeleccionada(event: Event): void {
    const input = event.target as HTMLInputElement;

    if (input.files && input.files.length > 0) {
      this.imagenSeleccionada = input.files[0];

      const reader = new FileReader();
      reader.onload = () => {
        this.imagenPreview = reader.result as string;
      };
      reader.readAsDataURL(this.imagenSeleccionada);
    }
  }
  
  agregarActor(): void {
    this.actores.push(new FormControl('', [Validators.required, this.validarSinComas]));
  }
  
  eliminarActor(index: number): void {
    if (this.actores.length > 1) {
      this.actores.removeAt(index);
    }
  }
  
  validarSinComas(control: FormControl): { [key: string]: boolean } | null {
    if (control.value && control.value.includes(',')) {
      return { contieneCommas: true };
    }
    return null;
  }
  
  toggleCategoria(categoria: Categoria): void {
    const index = this.categoriasSeleccionadas.findIndex(c => c.id === categoria.id);
    
    if (index === -1) {
      this.categoriasSeleccionadas.push(categoria);
    } else {
      this.categoriasSeleccionadas.splice(index, 1);
    }
  }
  
  estaSeleccionada(categoria: Categoria): boolean {
    return this.categoriasSeleccionadas.some(c => c.id === categoria.id);
  }
  
  crearPelicula(): void {
    if (this.peliculaForm.invalid) {
      this.infoMsg = 'Complete todos los campos correctamente';
      return;
    }
    
    if (this.categoriasSeleccionadas.length === 0) {
      this.infoMsg = 'Seleccione al menos una categoría';
      return;
    }
    
    if (!this.imagenSeleccionada) {
      this.infoMsg = 'Seleccione una imagen para la película';
      return;
    }
    
    this.imagenService.subirImagen(this.imagenSeleccionada).subscribe({
      next: (imagen: Imagen) => {
        this.crearPeliculaConImagen(imagen);
      },
      error: (err) => {
        this.errorMsg = err.error || 'Error al subir la imagen';
      }
    });
  }
  
  private crearPeliculaConImagen(imagen: Imagen): void {
    const reparto = this.actores.value.join(', ');
    
    const pelicula: Pelicula = {
      imagen: imagen,
      clasificacion: {
        codigo: this.peliculaForm.value.clasificacion!,
        descripcion: ''
      },
      titulo: this.peliculaForm.value.titulo!,
      sinopsis: this.peliculaForm.value.sinopsis!,
      fechaEstreno: this.peliculaForm.value.fechaEstreno!,
      duracion: this.peliculaForm.value.duracion!,
      director: this.peliculaForm.value.director!,
      reparto: reparto,
      categorias: this.categoriasSeleccionadas
    };
    
    this.peliculaService.crearPelicula(pelicula).subscribe({
      next: () => {
        this.infoMsg = 'Película creada exitosamente';
        this.peliculaForm.reset();
      },
      error: (err) => {
        this.errorMsg = err.error || 'Error al crear la película';
      }
    });
  }
}