import { Component, inject, Input, OnChanges, OnDestroy, OnInit, SimpleChanges, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { AnuncioI } from '../../../models/anuncio-i';
import { ImagenService } from '../../../services/imagen-service';
import { VideoService } from '../../../services/video-service';
import { DomSanitizer, SafeResourceUrl, SafeUrl } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';
import { take } from 'rxjs/operators';

@Component({
  selector: 'app-anuncios-widget',
  imports: [CommonModule],
  templateUrl: './anuncios-widget.html',
  styleUrls: ['./anuncios-widget.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AnunciosWidget implements OnInit, OnChanges, OnDestroy {
  @Input() anuncios: AnuncioI[] = [];

  private imagenService = inject(ImagenService);
  private videoService = inject(VideoService);
  private sanitizer = inject(DomSanitizer);
  private cdr = inject(ChangeDetectorRef);

  mediaMap = new Map<number, SafeUrl | SafeResourceUrl>();
  private anunciosCargados = new Set<number>();
  private objectUrls: string[] = [];

  ngOnInit(): void {
    this.cargarMedias();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['anuncios']) {
      this.cargarMedias();
    }
  }

  private cargarMedias(): void {
    if (!Array.isArray(this.anuncios)) return;

    this.anuncios.forEach(anuncio => {
      if (!anuncio?.id || this.anunciosCargados.has(anuncio.id)) return;
      if (!anuncio.idMedia) return;

      if (anuncio.tipo === 'TEXTO_IMAGEN') {
        this.imagenService.obtenerImagen(anuncio.idMedia).pipe(take(1)).subscribe({
          next: blob => {
            const objectURL = URL.createObjectURL(blob);
            console.log("imagen", objectURL);
            
            this.objectUrls.push(objectURL);
            const safeUrl = this.sanitizer.bypassSecurityTrustUrl(objectURL);
            this.mediaMap.set(anuncio.id!, safeUrl);
            this.anunciosCargados.add(anuncio.id!);
            this.cdr.markForCheck(); // Notificar cambio a Angular
          },
          error: err => console.error('Error al obtener imagen:', err)
        });
      }

      if (anuncio.tipo === 'TEXTO_VIDEO') {
        this.videoService.obtenerVideo(anuncio.idMedia).pipe(take(1)).subscribe({
          next: video => {
            const videoUrl = (video.link || '').trim();
            console.log("video", videoUrl);
            
            let videoId = '';
            if (videoUrl.includes('youtu.be/')) {
              videoId = videoUrl.split('youtu.be/')[1].split('?')[0];
            } else if (videoUrl.includes('watch?v=')) {
              videoId = videoUrl.split('v=')[1].split('&')[0];
            }
            if (!videoId) return;
            const embedUrl = `https://www.youtube.com/embed/${videoId}?autoplay=0&mute=1&vq=small&rel=0`;
            const safeUrl = this.sanitizer.bypassSecurityTrustResourceUrl(embedUrl);
            this.mediaMap.set(anuncio.id!, safeUrl);
            this.anunciosCargados.add(anuncio.id!);
            this.cdr.markForCheck(); // Notificar cambio a Angular
          },
          error: err => console.error('Error al obtener video:', err)
        });
      }
    });
  }

  ngOnDestroy(): void {
    this.objectUrls.forEach(url => {
      try { URL.revokeObjectURL(url); } catch { }
    });
    this.objectUrls = [];
    this.mediaMap.clear();
    this.anunciosCargados.clear();
  }
}