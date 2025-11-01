import { Component, inject, Input, OnChanges, OnDestroy, OnInit, SimpleChanges } from '@angular/core';
import { AnuncioI } from '../../../models/anuncio-i';
import { ImagenService } from '../../../services/imagen-service';
import { VideoService } from '../../../services/video-service';
import { DomSanitizer, SafeResourceUrl, SafeUrl } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-anuncios-widget',
  imports: [CommonModule],
  templateUrl: './anuncios-widget.html',
  styleUrl: './anuncios-widget.css'
})
export class AnunciosWidget implements OnInit, OnChanges {

  @Input() anuncios: AnuncioI[] = [];

  private imagenService = inject(ImagenService);
  private videoService = inject(VideoService);
  private sanitizer = inject(DomSanitizer);

  mediaMap = new Map<number, SafeUrl | SafeResourceUrl>();

  ngOnInit(): void {
    this.cargarMedias();
    console.log('Anuncios iniciales:', this.anuncios);
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['anuncios'] && this.anuncios?.length > 0) {
      this.cargarMedias();
    }
  }

  private cargarMedias(): void {
    this.anuncios.forEach(anuncio => {
      if (!anuncio.idMedia) return;

      if (anuncio.tipo === 'TEXTO_IMAGEN') {
        this.imagenService.obtenerImagen(anuncio.idMedia).subscribe({
          next: blob => {
            const objectURL = URL.createObjectURL(blob);
            const safeUrl = this.sanitizer.bypassSecurityTrustUrl(objectURL);
            this.mediaMap.set(anuncio.id!, safeUrl);
            console.log('Imagen cargada:', anuncio.id);
          },
          error: err => console.error('Error al obtener imagen:', err)
        });
      }

      if (anuncio.tipo === 'TEXTO_VIDEO') {
        this.videoService.obtenerVideo(anuncio.idMedia).subscribe({
          next: video => {
            let videoUrl = video.link.trim();

            let videoId = '';
            if (videoUrl.includes('youtu.be/')) {
              videoId = videoUrl.split('youtu.be/')[1].split('?')[0];
            } else if (videoUrl.includes('watch?v=')) {
              videoId = videoUrl.split('v=')[1].split('&')[0];
            }

            const embedUrl = `https://www.youtube.com/embed/${videoId}?autoplay=0&mute=1&vq=small&rel=0`;

            const safeUrl = this.sanitizer.bypassSecurityTrustResourceUrl(embedUrl);
            this.mediaMap.set(anuncio.id!, safeUrl);

            console.log('Video embed:', embedUrl);
          },
          error: err => console.error('Error al obtener video:', err)
        });
      }


    });
  }
}
