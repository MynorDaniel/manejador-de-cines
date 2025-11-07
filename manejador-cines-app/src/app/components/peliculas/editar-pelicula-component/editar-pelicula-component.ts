import { Component, inject, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule, FormArray } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Pelicula } from '../../../models/pelicula';
import { Clasificacion } from '../../../models/clasificacion';
import { Categoria } from '../../../models/categoria';
import { Imagen } from '../../../models/imagen';
import { PeliculaService } from '../../../services/pelicula-service';
import { ImagenService } from '../../../services/imagen-service';

@Component({
  selector: 'app-editar-pelicula-component',
  imports: [ReactiveFormsModule, CommonModule, RouterLink],
  templateUrl: './editar-pelicula-component.html',
  styleUrl: './editar-pelicula-component.css'
})
export class EditarPeliculaComponent implements OnInit {

  errorMsg: string | null = null;
  infoMsg: string | null = null;
  
  peliculaService = inject(PeliculaService);
  imagenService = inject(ImagenService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  
  clasificaciones: Clasificacion[] = [];
  categorias: Categoria[] = [];
  categoriasSeleccionadas: Categoria[] = [];
  
  pelicula?: Pelicula;
  imagenActual?: Imagen;
  imagenSeleccionada: File | null = null;
  imagenPreview: string | null = null;
  cargando = true;
  
  peliculaForm = new FormGroup({
    clasificacion: new FormControl('', [Validators.required]),
    titulo: new FormControl('', [Validators.required, Validators.maxLength(200)]),
    sinopsis: new FormControl('', [Validators.required]),
    fechaEstreno: new FormControl('', [Validators.required]),
    duracion: new FormControl('', [Validators.required, Validators.min(1)]),
    director: new FormControl('', [Validators.required, Validators.maxLength(200)]),
    actores: new FormArray([])
  });
  
  ngOnInit(): void {
    this.cargarClasificaciones();
    this.cargarCategorias();
    this.cargarPelicula();
  }
  
  get actores(): FormArray {
    return this.peliculaForm.get('actores') as FormArray;
  }
  
  cargarPelicula(): void {
    const id = this.route.snapshot.paramMap.get('id');
    
    if (!id) {
      this.errorMsg = 'ID de película no válido';
      this.cargando = false;
      return;
    }
    
    this.peliculaService.verPelicula(id).subscribe({
      next: (pelicula) => {
        this.pelicula = pelicula;
        this.imagenActual = pelicula.imagen;
        this.cargarDatosEnFormulario();
        this.cargarImagenActual();
      },
      error: (err) => {
        this.errorMsg = err.error || 'Error al cargar la película';
        this.cargando = false;
      }
    });
  }
  
  cargarDatosEnFormulario(): void {
    if (!this.pelicula) return;
    
    this.peliculaForm.patchValue({
      clasificacion: this.pelicula.clasificacion.codigo,
      titulo: this.pelicula.titulo,
      sinopsis: this.pelicula.sinopsis,
      fechaEstreno: this.pelicula.fechaEstreno,
      duracion: this.pelicula.duracion,
      director: this.pelicula.director
    });
    
    const actores = this.pelicula.reparto.split(',').map(actor => actor.trim());
    actores.forEach(actor => {
      this.actores.push(new FormControl(actor, [Validators.required, this.validarSinComas]));
    });
    
    if (this.pelicula.categorias) {
      this.categoriasSeleccionadas = [...this.pelicula.categorias];
    }
  }
  
  cargarImagenActual(): void {
    if (!this.imagenActual) {
      this.cargando = false;
      return;
    }
    
    this.imagenService.obtenerImagen(this.imagenActual.id).subscribe({
      next: (blob) => {
        if (blob) {
          this.imagenPreview = URL.createObjectURL(blob);
        }
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error al cargar imagen:', err);
        this.cargando = false;
      }
    });
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
  
  editarPelicula(): void {
    if (this.peliculaForm.invalid) {
      this.infoMsg = 'Complete todos los campos correctamente';
      return;
    }
    
    if (this.categoriasSeleccionadas.length === 0) {
      this.infoMsg = 'Seleccione al menos una categoría';
      return;
    }
    
    if (this.imagenSeleccionada) {
      this.imagenService.subirImagen(this.imagenSeleccionada).subscribe({
        next: (imagen: Imagen) => {
          this.actualizarPeliculaConImagen(imagen);
        },
        error: (err) => {
          this.errorMsg = err.error || 'Error al subir la imagen';
        }
      });
    } else {
      this.actualizarPeliculaConImagen(this.imagenActual!);
    }
  }
  
  private actualizarPeliculaConImagen(imagen: Imagen): void {
    if (!this.pelicula) return;
    
    const reparto = this.actores.value.join(', ');
    
    const peliculaActualizada: Pelicula = {
      id: this.pelicula.id,
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
    
    this.peliculaService.editarPelicula(peliculaActualizada).subscribe({
      next: () => {
        this.infoMsg = 'Película actualizada exitosamente';
      },
      error: (err) => {
        this.errorMsg = err.error || 'Error al actualizar la película';
      }
    });
  }
  
  ngOnDestroy(): void {
    if (this.imagenPreview) {
      URL.revokeObjectURL(this.imagenPreview);
    }
  }
}