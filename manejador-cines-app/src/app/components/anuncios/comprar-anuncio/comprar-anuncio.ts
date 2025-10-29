import { Component, inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Pago } from '../../../models/pago';
import { AnuncioI } from '../../../models/anuncio-i';
import { CommonModule } from '@angular/common';
import { PagoForm } from '../../pagos/pago-form/pago-form';
import { AnuncioService } from '../../../services/anuncio-service';
import { TipoAnuncio } from '../../../models/tipo-anuncio';
import { VigenciaAnuncio } from '../../../models/vigencia-anuncio';
import { ImagenService } from '../../../services/imagen-service';
import { map, Observable, of } from 'rxjs';
import { Imagen } from '../../../models/imagen';
import { VideoService } from '../../../services/video-service';
import { Video } from '../../../models/video';

@Component({
  selector: 'app-comprar-anuncio',
  imports: [ReactiveFormsModule, CommonModule, PagoForm],
  templateUrl: './comprar-anuncio.html',
  styleUrl: './comprar-anuncio.css'
})

export class ComprarAnuncio implements OnInit {
  
  anuncioService = inject(AnuncioService);
  imagenService = inject(ImagenService);
  videoService = inject(VideoService);
  
  tipoSeleccionado: string = '';
  imagenSeleccionada: File | null = null;
  imagenPreview: string | null = null;
  errorImagen: string | null = null;
  pagoValido: boolean = false;
  pagoData: Pago | null = null;

  errorMsg: string | null = null;
  infoMsg: string | null = null;

  tipos: TipoAnuncio[] = [];
  vigencias: VigenciaAnuncio[] = [];
  cargandoTipos: boolean = false;
  cargandoVigencias: boolean = false;

  anuncioForm: FormGroup = new FormGroup({
    vigencia: new FormControl('', [Validators.required]),
    tipo: new FormControl('', [Validators.required]),
    texto: new FormControl('', [
      Validators.required, 
      Validators.minLength(1),
      Validators.maxLength(500)
    ]),
    videoLink: new FormControl('')
  });

  ngOnInit(): void {
    this.cargarPrecios();
  }

  cargarPrecios(): void {
    this.cargandoTipos = true;
    this.anuncioService.verPreciosDeTipos().subscribe({
      next: (tipos) => {
        this.tipos = tipos;
        this.cargandoTipos = false;
      },
      error: (error) => {
        console.error('Error al cargar tipos:', error);
        this.cargandoTipos = false;
      }
    });

    this.cargandoVigencias = true;
    this.anuncioService.verPreciosDeVigencias().subscribe({
      next: (vigencias) => {
        this.vigencias = vigencias;
        this.cargandoVigencias = false;
      },
      error: (error) => {
        console.error('Error al cargar vigencias:', error);
        this.cargandoVigencias = false;
      }
    });
  }

  formatearTipo(tipo: string): string {
    const nombres: { [key: string]: string } = {
      'TEXTO': 'Texto simple',
      'TEXTO_IMAGEN': 'Texto con Imagen',
      'TEXTO_VIDEO': 'Texto con Video'
    };
    return nombres[tipo] || tipo;
  }

  onTipoChange(): void {
    const tipo = this.anuncioForm.value.tipo;
    this.tipoSeleccionado = tipo;

    this.imagenSeleccionada = null;
    this.imagenPreview = null;
    this.errorImagen = null;
    this.anuncioForm.patchValue({ videoLink: '' });

    if (tipo === 'TEXTO_VIDEO') {
      this.anuncioForm.controls['videoLink'].setValidators([
        Validators.required
      ]);
    } else {
      this.anuncioForm.controls['videoLink'].clearValidators();
    }
    this.anuncioForm.controls['videoLink'].updateValueAndValidity();
  }

  onImagenSeleccionada(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      
      if (file.size > 5 * 1024 * 1024) {
        this.errorImagen = 'La imagen no puede superar los 5MB';
        this.imagenSeleccionada = null;
        this.imagenPreview = null;
        return;
      }

      if (!file.type.match(/image\/(png|jpeg|jpg|gif)/)) {
        this.errorImagen = 'Solo se permiten imágenes PNG, JPG o GIF';
        this.imagenSeleccionada = null;
        this.imagenPreview = null;
        return;
      }

      this.errorImagen = null;
      this.imagenSeleccionada = file;

      const reader = new FileReader();
      reader.onload = (e) => {
        this.imagenPreview = e.target?.result as string;
      };
      reader.readAsDataURL(file);
    }
  }

  eliminarImagen(): void {
    this.imagenSeleccionada = null;
    this.imagenPreview = null;
    this.errorImagen = null;
    const input = document.getElementById('imagen') as HTMLInputElement;
    if (input) input.value = '';
  }

  onPagoDataChange(pago: Pago): void {
    this.pagoData = pago;
    this.pagoValido = pago != null;
  }

  calcularMontoTotal(): number {
    const tipoSeleccionado = this.anuncioForm.value.tipo;
    const vigenciaSeleccionada = this.anuncioForm.value.vigencia;
    
    let total = 0;
    
    if (tipoSeleccionado) {
      const tipo = this.tipos.find(t => t.tipo === tipoSeleccionado);
      if (tipo) {
        total += parseFloat(tipo.monto);
      }
    }
    
    if (vigenciaSeleccionada) {
      const vigencia = this.vigencias.find(v => v.dias === vigenciaSeleccionada);
      if (vigencia) {
        total += parseFloat(vigencia.monto);
      }
    }
    
    return Math.round(total * 100) / 100;
  }

  comprarAnuncio(): void {
    if (this.anuncioForm.invalid) {
      this.anuncioForm.markAllAsTouched();
      return;
    }

    if (this.tipoSeleccionado === 'TEXTO_IMAGEN' && !this.imagenSeleccionada) {
      this.errorImagen = 'Debes seleccionar una imagen';
      return;
    }

    this.subirMedia().subscribe({
      next: (idMedia) => {
        const anuncio: AnuncioI = {
          vigencia: parseInt(this.anuncioForm.value.vigencia),
          tipo: this.anuncioForm.value.tipo,
          texto: this.anuncioForm.value.texto,
          pago: this.pagoData!,
          idMedia: idMedia
        };

        console.log('Anuncio a crear:', anuncio);

        this.anuncioService.crearAnuncio(anuncio).subscribe({
          next: () => {
            this.infoMsg = 'Anuncio creado';
            
          },
          error: (error) => {
            this.errorMsg = error.error;
          }
        });
        
      },
      error: (error) => {
        console.error('Error al subir media:', error);
        this.errorMsg = error.error;
      }
    });
  }

  subirMedia(): Observable<number | null> {
    if (this.tipoSeleccionado === 'TEXTO_IMAGEN' && this.imagenSeleccionada) {
      return this.imagenService.subirImagen(this.imagenSeleccionada).pipe(
        map((imagen: Imagen) => imagen.id)
      );
    }
    
    if (this.tipoSeleccionado === 'TEXTO_VIDEO') {
      let video:Video = {
        link: this.anuncioForm.value.videoLink
      }
      return this.videoService.subirVideo(video).pipe(
        map((video: Video) => {
          if(video.id){
            return +video.id
          }else{return null}
        })
      );
    }
    
    return of(null);
  }
}
