import { Component, inject, OnInit } from '@angular/core';
import { Usuario } from '../../../models/usuario';
import { UsuarioService } from '../../../services/usuario-service';
import { JwtService } from '../../../services/jwt-service';
import { ImagenService } from '../../../services/imagen-service';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-perfil',
  imports: [RouterLink],
  templateUrl: './perfil.html',
  styleUrl: './perfil.css'
})
export class Perfil implements OnInit {

  usuarioService = inject(UsuarioService);
  jwtService = inject(JwtService);
  imagenService = inject(ImagenService);
  route = inject(ActivatedRoute);
  router = inject(Router);

  usuario: Usuario | null = null;
  usuarioActual: Usuario | null = null;
  imagenUrl: string | null = '';
  errorMsg: string | null = null;

  ngOnInit(): void {
    this.usuarioActual = this.jwtService.getUsuarioActual();

    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');

      if (!idParam) {
        this.errorMsg = 'No se proporcionó un ID de usuario.';
        this.usuario = null;
        return;
      }

      const id = Number(idParam);
      this.cargarUsuario(id);
    });
  }

  private cargarUsuario(id: number): void {
    this.errorMsg = null;
    this.usuario = null;
    this.imagenUrl = null;

    this.usuarioService.obtenerUsuario(id).subscribe({
      next: (usuarioResponse) => {
        this.usuario = usuarioResponse;

        if (this.usuario.imagen?.id) {
          this.imagenService.obtenerImagen(this.usuario.imagen.id).subscribe({
            next: (blob) => {
              this.imagenUrl = URL.createObjectURL(blob);
            },
            error: (err) => {
              console.error('No se pudo cargar la imagen', err);
              this.imagenUrl = null;
            }
          });
        }
      },
      error: (error: any) => {
        this.errorMsg = error.error ?? 'Error al buscar el usuario.';
      }
    });
  }

  desactivarCuenta(): void {
    if(this.usuario){
      this.usuarioService.desactivarCuenta(this.usuario.id).subscribe({
        next: () => {
          sessionStorage.removeItem('token');
          this.usuario = null;
          this.router.navigate(['/']);
        },
        error: (err:any) => {
          console.error('No se pudo eliminar la cuenta', err);
          this.errorMsg = err.error ?? 'Error al buscar el usuario.';
        }
      });
    }
  }
}
