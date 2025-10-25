import { Component, inject, OnInit } from '@angular/core';
import { Usuario } from '../../../models/usuario';
import { UsuarioService } from '../../../services/usuario-service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-usuarios',
  imports: [RouterLink],
  templateUrl: './usuarios.html',
  styleUrl: './usuarios.css'
})
export class Usuarios implements OnInit{

  usuarioService = inject(UsuarioService);
  usuarios: Usuario[] | null = null;

  errorMsg: string | null = null;

  ngOnInit(): void {
    this.usuarioService.obtenerUsuarios().subscribe({
      next: (usuariosResponse) => {
        this.usuarios = usuariosResponse;
      },
      error: (error: any) => {
        this.errorMsg = error.error ?? 'Error al buscar los usuarios.';
      }
    });
  }
}
