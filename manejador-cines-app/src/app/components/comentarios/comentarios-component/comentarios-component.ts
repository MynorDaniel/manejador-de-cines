import { Component, inject, Input, OnInit } from '@angular/core';
import { ComentarioService } from '../../../services/comentario-service';
import { ComentarioSala } from '../../../models/comentario-sala';
import { ComentarioPelicula } from '../../../models/comentario-pelicula';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { JwtService } from '../../../services/jwt-service';
import { Comentario } from '../../../models/comentario';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-comentarios-component',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './comentarios-component.html',
  styleUrl: './comentarios-component.css'
})
export class ComentariosComponent implements OnInit {

  @Input() tipo: 'sala' | 'pelicula' = 'sala';
  @Input() entidadId!: number;
  @Input() comentariosBloqueados: boolean = false;

  comentarioService = inject(ComentarioService);
  jwtService = inject(JwtService);

  comentarios: (ComentarioSala | ComentarioPelicula)[] = [];
  nuevoComentario: string = '';
  cargando: boolean = false;
  errorMsg: string | null = null;
  infoMsg: string | null = null;
  
  comentarioEditando: string | null = null;
  contenidoEditando: string = '';

  ngOnInit(): void {
    this.cargarComentarios();
  }

  cargarComentarios(comentarioIdParaEnfocar?: string): void {
    this.cargando = true;
    this.errorMsg = null;

    if (this.tipo === 'sala') {
      this.comentarioService.verComentariosSala(this.entidadId).subscribe({
        next: (comentarios) => {
          this.comentarios = comentarios;
          this.cargando = false;
          if (comentarioIdParaEnfocar) {
            this.enfocarComentario(comentarioIdParaEnfocar);
          }
        },
        error: (err) => {
          this.errorMsg = err.error || 'Error al cargar los comentarios';
          this.cargando = false;
        }
      });
    } else {
      this.comentarioService.verComentariosPelicula(this.entidadId).subscribe({
        next: (comentarios) => {
          this.comentarios = comentarios;
          this.cargando = false;
          if (comentarioIdParaEnfocar) {
            this.enfocarComentario(comentarioIdParaEnfocar);
          }
        },
        error: (err) => {
          this.errorMsg = err.error || 'Error al cargar los comentarios';
          this.cargando = false;
        }
      });
    }
  }

  enfocarComentario(comentarioId: string): void {
    setTimeout(() => {
      const elemento = document.getElementById(`comentario-${comentarioId}`);
      if (elemento) {
        elemento.scrollIntoView({ behavior: 'smooth', block: 'center' });
        elemento.classList.add('comentario-destacado');
        setTimeout(() => {
          elemento.classList.remove('comentario-destacado');
        }, 2000);
      }
    }, 100);
  }

  agregarComentario(): void {
    if (!this.nuevoComentario.trim()) {
      this.errorMsg = 'El comentario no puede estar vacío';
      return;
    }

    if (this.comentariosBloqueados) {
      this.errorMsg = 'Los comentarios están bloqueados';
      return;
    }

    this.errorMsg = null;
    this.infoMsg = null;

    if (this.tipo === 'sala') {
      const comentarioSala: ComentarioSala = {
        comentario: {
          contenido: this.nuevoComentario
        },
        idSala: this.entidadId
      };

      this.comentarioService.crearComentarioSala(comentarioSala).subscribe({
        next: () => {
          this.infoMsg = 'Comentario agregado exitosamente';
          this.nuevoComentario = '';
          this.cargarComentarios();
        },
        error: (err) => {
          this.errorMsg = err.error || 'Error al agregar el comentario';
        }
      });
    } else {
      const comentarioPelicula: ComentarioPelicula = {
        comentario: {
          contenido: this.nuevoComentario
        },
        idPelicula: this.entidadId
      };

      this.comentarioService.crearComentarioPelicula(comentarioPelicula).subscribe({
        next: () => {
          this.infoMsg = 'Comentario agregado exitosamente';
          this.nuevoComentario = '';
          this.cargarComentarios();
        },
        error: (err) => {
          this.errorMsg = err.error || 'Error al agregar el comentario';
        }
      });
    }
  }

  iniciarEdicion(comentario: ComentarioSala | ComentarioPelicula): void {
    this.comentarioEditando = comentario.comentario.id!;
    this.contenidoEditando = comentario.comentario.contenido;
    this.errorMsg = null;
    this.infoMsg = null;
  }

  cancelarEdicion(): void {
    this.comentarioEditando = null;
    this.contenidoEditando = '';
  }

  guardarEdicion(): void {
    if (!this.contenidoEditando.trim()) {
      this.errorMsg = 'El comentario no puede estar vacío';
      return;
    }

    const comentarioId = this.comentarioEditando!;
    const comentarioEditado: Comentario = {
      id: comentarioId,
      contenido: this.contenidoEditando
    };

    this.comentarioService.editarComentario(comentarioEditado).subscribe({
      next: () => {
        this.infoMsg = 'Comentario editado exitosamente';
        this.comentarioEditando = null;
        this.contenidoEditando = '';
        this.cargarComentarios(comentarioId);
      },
      error: (err) => {
        this.errorMsg = err.error || 'Error al editar el comentario';
      }
    });
  }

  eliminarComentario(comentarioId: string): void {
    if (!confirm('¿Estás seguro de que deseas eliminar este comentario?')) {
      return;
    }

    this.comentarioService.eliminarComentario(Number(comentarioId)).subscribe({
      next: () => {
        this.infoMsg = 'Comentario eliminado exitosamente';
        this.cargarComentarios();
      },
      error: (err) => {
        this.errorMsg = err.error || 'Error al eliminar el comentario';
      }
    });
  }

  esComentarioDelUsuario(comentario: ComentarioSala | ComentarioPelicula): boolean {
    const usuarioActual = this.jwtService.getUsuarioActual();
    return comentario.comentario.idUsuario === usuarioActual.id?.toString();
  }

  formatearFecha(fecha?: string): string {
    if (!fecha) return '';
    const date = new Date(fecha);
    return date.toLocaleDateString('es-ES', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  }
}