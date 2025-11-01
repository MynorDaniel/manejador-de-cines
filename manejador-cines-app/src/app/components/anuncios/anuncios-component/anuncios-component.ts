import { Component, inject, OnInit } from '@angular/core';
import { AnuncioService } from '../../../services/anuncio-service';
import { AnuncioI } from '../../../models/anuncio-i';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-anuncios-component',
  imports: [CommonModule],
  templateUrl: './anuncios-component.html',
  styleUrl: './anuncios-component.css'
})
export class AnunciosComponent implements OnInit {

  anuncioService = inject(AnuncioService);
  route = inject(ActivatedRoute);
  router = inject(Router);

  anuncios: AnuncioI[] = [];
  cargando = false;
  errorMsg: string | null = null;
  procesando: number | null = null;
  infoMsg: string | null = null;
  propios = false;

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.propios = params.get('propios') === 'true';
      this.cargarAnuncios();
    });
  }

  cargarAnuncios(): void {
    this.cargando = true;
    this.errorMsg = null;

    const fuente = this.propios
      ? this.anuncioService.verPropios()
      : this.anuncioService.verAnuncios();

    fuente.subscribe({
      next: (anuncios) => {
        this.anuncios = anuncios;
        this.cargando = false;
      },
      error: (error) => {
        this.errorMsg = 'Error al cargar los anuncios';
        this.cargando = false;
        console.error('Error:', error);
      }
    });
  }

  cambiarEstado(anuncio: AnuncioI): void {
    if (!anuncio.id) return;

    this.procesando = anuncio.id;

    const anuncioActualizado: AnuncioI = {
      id: anuncio.id,
      vigencia: anuncio.vigencia,
      tipo: anuncio.tipo,
      texto: anuncio.texto,
      activado: !anuncio.activado,
      pago: anuncio.pago,
      idMedia: anuncio.tipo !== 'TEXTO' ? anuncio.idMedia : null
    };

    this.anuncioService.editarAnuncio(anuncioActualizado).subscribe({
      next: () => {
        anuncio.activado = !anuncio.activado;
        this.procesando = null;
      },
      error: (error) => {
        this.errorMsg = error.error;
        this.procesando = null;
        console.error('Error:', error);
      }
    });
  }

  confirmarCambioEstado(anuncio: AnuncioI) {
    const confirmado = window.confirm('¿Estás seguro de que deseas desactivar este anuncio?');
    if (confirmado) {
      this.cambiarEstado(anuncio);
    }
  }

  editarAnuncio(anuncio: AnuncioI): void {
    console.log("Anuncio: ", anuncio);
    
    this.router.navigate(['/editar-anuncio'], { state: { anuncio } });
  }

}