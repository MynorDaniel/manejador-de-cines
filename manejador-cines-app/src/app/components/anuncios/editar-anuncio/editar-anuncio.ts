import { CommonModule } from "@angular/common";
import { Component, OnInit, inject } from "@angular/core";
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from "@angular/forms";
import { Observable, map, of } from "rxjs";
import { AnuncioI } from "../../../models/anuncio-i";
import { Imagen } from "../../../models/imagen";
import { Pago } from "../../../models/pago";
import { TipoAnuncio } from "../../../models/tipo-anuncio";
import { Video } from "../../../models/video";
import { VigenciaAnuncio } from "../../../models/vigencia-anuncio";
import { AnuncioService } from "../../../services/anuncio-service";
import { ImagenService } from "../../../services/imagen-service";
import { VideoService } from "../../../services/video-service";
import { ActivatedRoute, Router } from "@angular/router";


@Component({
  selector: 'app-editar-anuncio',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './editar-anuncio.html',
  styleUrl: './editar-anuncio.css'
})
export class EditarAnuncio implements OnInit {

  //html
  //formgroup
  //cargar formgroup
  //enviar formgroup

  anuncio:AnuncioI | null = null;
  imagenSeleccionada: File | null = null;

  route = inject(ActivatedRoute);
  router = inject(Router);
  anuncioService = inject(AnuncioService);
  imagenService = inject(ImagenService);
  videoService = inject(VideoService);

  errorMsg: string | null = null;
  infoMsg: string | null = null;

  anuncioForm: FormGroup = new FormGroup({
    id: new FormControl(''),
    texto: new FormControl('', [
      Validators.required, 
      Validators.minLength(1),
      Validators.maxLength(500)
    ]),
    videoLink: new FormControl('')
  });

  ngOnInit(): void {
    const state = history.state;

    if (state && state['anuncio']) {
      this.anuncio = state['anuncio'];
    }

    console.log('Anuncio recibido:', this.anuncio);

    if(this.anuncio){
      this.anuncioForm.patchValue(this.anuncio);
    }
    
  }


  editarAnuncio(){
    if (this.anuncioForm.invalid) {
      this.anuncioForm.markAllAsTouched();
      return;
    }

    this.subirMedia().subscribe({
      next: (idMedia) => {
        if(this.anuncio){
          const anuncio: AnuncioI = {
            id: this.anuncio?.id,
            vigencia: parseInt(this.anuncioForm.value.vigencia),
            tipo: this.anuncioForm.value.tipo,
            texto: this.anuncioForm.value.texto,
            pago: undefined,
            idMedia: idMedia == null ? this.anuncio.idMedia : idMedia,
            activado: this.anuncio?.activado
          };

          console.log('Anuncio a editar:', anuncio);

          this.anuncioService.editarAnuncio(anuncio).subscribe({
            next: () => {
              this.infoMsg = 'Anuncio editado';
              
            },
            error: (error) => {
              this.errorMsg = error.error;
            }
          });
      }
      },
      error: (error) => {
        console.error('Error al subir media:', error);
        this.errorMsg = error.error;
      }
    });
  }

  subirMedia(): Observable<number | null> {
    if (this.anuncio?.tipo === 'TEXTO_IMAGEN' && this.imagenSeleccionada) {
      return this.imagenService.subirImagen(this.imagenSeleccionada).pipe(
        map((imagen: Imagen) => imagen.id)
      );
    }
    
    if (this.anuncio?.tipo === 'TEXTO_VIDEO') {
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

  onImagenSeleccionada(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      this.imagenSeleccionada = file;
    }
  }
  
  // anuncioService = inject(AnuncioService);
  // imagenService = inject(ImagenService);
  // videoService = inject(VideoService);
  // route = inject(ActivatedRoute)

  // tipoSeleccionado: string = '';
  
  // imagenSeleccionada: File | null = null;
  // imagenPreview: string | null = null;
  // errorImagen: string | null = null;

  // anuncio: AnuncioI | null = null;

  // errorMsg: string | null = null;
  // infoMsg: string | null = null;

  // anuncioForm: FormGroup = new FormGroup({
  //   id: new FormControl(''),
  //   texto: new FormControl('', [
  //     Validators.required, 
  //     Validators.minLength(1),
  //     Validators.maxLength(500)
  //   ]),
  //   videoLink: new FormControl('')
  // });


  // ngOnInit(): void {
  //   this.route.paramMap.subscribe(params => {
  //     const idParam = params.get('id');

  //     if (!idParam) {
  //       this.errorMsg = 'No se proporcionó un ID de anuncio.';
  //       this.anuncio = null;
  //       return;
  //     }

  //     const id = Number(idParam);
  //     this.cargarAnuncio(id);
  //   });
  // }

  // cargarAnuncio(id:number){

  // }

  // onImagenSeleccionada(event: Event): void {
  //   const input = event.target as HTMLInputElement;
  //   if (input.files && input.files.length > 0) {
  //     const file = input.files[0];
      
  //     if (file.size > 5 * 1024 * 1024) {
  //       this.errorImagen = 'La imagen no puede superar los 5MB';
  //       this.imagenSeleccionada = null;
  //       this.imagenPreview = null;
  //       return;
  //     }

  //     if (!file.type.match(/image\/(png|jpeg|jpg|gif)/)) {
  //       this.errorImagen = 'Solo se permiten imágenes PNG, JPG o GIF';
  //       this.imagenSeleccionada = null;
  //       this.imagenPreview = null;
  //       return;
  //     }

  //     this.errorImagen = null;
  //     this.imagenSeleccionada = file;

  //     const reader = new FileReader();
  //     reader.onload = (e) => {
  //       this.imagenPreview = e.target?.result as string;
  //     };
  //     reader.readAsDataURL(file);
  //   }
  // }

  // eliminarImagen(): void {
  //   this.imagenSeleccionada = null;
  //   this.imagenPreview = null;
  //   this.errorImagen = null;
  //   const input = document.getElementById('imagen') as HTMLInputElement;
  //   if (input) input.value = '';
  // }

  // editarAnuncio(): void {
  //   if (this.anuncioForm.invalid) {
  //     this.anuncioForm.markAllAsTouched();
  //     return;
  //   }

  //   this.subirMedia().subscribe({
  //     next: (idMedia) => {
  //       const anuncio: AnuncioI = {
  //         vigencia: parseInt(this.anuncioForm.value.vigencia),
  //         tipo: this.anuncioForm.value.tipo,
  //         texto: this.anuncioForm.value.texto,
  //         idMedia: idMedia,
  //         pago: undefined
  //       };

  //       console.log('Anuncio a editar:', anuncio);

  //       this.anuncioService.crearAnuncio(anuncio).subscribe({
  //         next: () => {
  //           this.infoMsg = 'Anuncio creado';
            
  //         },
  //         error: (error) => {
  //           this.errorMsg = error.error;
  //         }
  //       });
        
  //     },
  //     error: (error) => {
  //       console.error('Error al subir media:', error);
  //       this.errorMsg = error.error;
  //     }
  //   });
  // }

  // subirMedia(): Observable<number | null> {
  //   if (this.tipoSeleccionado === 'TEXTO_IMAGEN' && this.imagenSeleccionada) {
  //     return this.imagenService.subirImagen(this.imagenSeleccionada).pipe(
  //       map((imagen: Imagen) => imagen.id)
  //     );
  //   }
    
  //   if (this.tipoSeleccionado === 'TEXTO_VIDEO') {
  //     let video:Video = {
  //       link: this.anuncioForm.value.videoLink
  //     }
  //     return this.videoService.subirVideo(video).pipe(
  //       map((video: Video) => {
  //         if(video.id){
  //           return +video.id
  //         }else{return null}
  //       })
  //     );
  //   }
    
  //   return of(null);
  // }
}
