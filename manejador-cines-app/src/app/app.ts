import { Component, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Header } from './components/inicio/header/header';
import { Footer } from './components/inicio/footer/footer';
import { AnuncioService } from './services/anuncio-service';
import { Observable } from 'rxjs';
import { AnuncioI } from './models/anuncio-i';
import { CommonModule } from '@angular/common';
import { AnunciosWidget } from './components/anuncios/anuncios-widget/anuncios-widget';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Header, Footer, CommonModule, AnunciosWidget],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('manejador-cines-app');

  anuncioService = inject(AnuncioService);

  anunciosIzquierda: AnuncioI[] = [];
  anunciosDerecha: AnuncioI[] = [];

  ngOnInit(): void {
    this.cargarAnuncios();
  }

  cargarAnuncios(): void {
    this.anuncioService.verMostrables().subscribe({
      next: (anuncios) => {
        this.anunciosIzquierda = anuncios.slice(0, 4);
        this.anunciosDerecha = anuncios.slice(4, 8);
        
      },
      error: (error) => {
        console.error('Error al cargar anuncios:', error);
      }
    });
  }
}
