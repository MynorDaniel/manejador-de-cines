import { Component, DoCheck, inject, OnInit } from '@angular/core';
import { NavigationEnd, Router, RouterLink, RouterModule } from '@angular/router';
import { Usuario } from '../../../models/usuario';
import { ImagenService } from '../../../services/imagen-service';
import { filter } from 'rxjs';
import { UsuarioService } from '../../../services/usuario-service';

@Component({
  selector: 'app-header',
  imports: [RouterModule, RouterLink],
  templateUrl: './header.html',
  styleUrl: './header.css'
})
export class Header implements OnInit {

  router:Router = inject(Router);
  imagenService = inject(ImagenService);
  usuario:Usuario | null = null;
  imagenUrl:string|null = '';
  usuarioService = inject(UsuarioService);

  ngOnInit(): void {
    this.actualizarUsuario();

    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        this.actualizarUsuario();
      });
  }

  logout(): void {
    sessionStorage.removeItem('token');
    this.usuario = null;
    this.router.navigate(['/']);
  }

  private actualizarUsuario(): void {
    const token = sessionStorage.getItem('token');
    if (token) {
      this.usuario = JSON.parse(token).usuario as Usuario;
      this.usuarioService.obtenerUsuario(this.usuario.id).subscribe({
          next: (usuario) => {
            this.usuario = usuario
            if (this.usuario.imagen?.id) {
            this.imagenService.obtenerImagen(this.usuario.imagen.id).subscribe({
              next: (blob) => this.imagenUrl = URL.createObjectURL(blob),
              error: (err) => {
                console.error('No se pudo cargar la imagen', err);
                this.imagenUrl = null;
              }
            });
          } else {
            this.imagenUrl = null;
          }
          },
          error: (err) => {
            console.error('No se pudo cargar el usuario', err);
          }
        });
    } else {
      this.usuario = null;
      this.imagenUrl = null;
    }
  }

}