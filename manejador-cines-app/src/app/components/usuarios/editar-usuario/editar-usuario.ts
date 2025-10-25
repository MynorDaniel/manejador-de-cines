import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Usuario } from '../../../models/usuario';
import { UsuarioService } from '../../../services/usuario-service';
import { JwtService } from '../../../services/jwt-service';
import { Router } from '@angular/router';
import { ImagenService } from '../../../services/imagen-service';
import { Imagen } from '../../../models/imagen';

@Component({
  selector: 'app-editar-usuario',
  imports: [ReactiveFormsModule],
  templateUrl: './editar-usuario.html',
  styleUrl: './editar-usuario.css'
})
export class EditarUsuario implements OnInit {

  usuarioService = inject(UsuarioService);
  jwtService = inject(JwtService);
  router = inject(Router);
  imagenService = inject(ImagenService);

  errorMsg: string | null = null;
  infoMsg: string | null = null;
  usuarioActual: Usuario | null = null;

  imagenSeleccionada: File | null = null;
  imagenActual: string | null = null;

  editarUsuarioForm: FormGroup = new FormGroup({
    id: new FormControl(''),
    nombre: new FormControl('', [Validators.required, Validators.maxLength(200)]),
    correo: new FormControl('', [Validators.required, Validators.email, Validators.maxLength(350)]),
    clave: new FormControl('')
  });

  ngOnInit(): void {
    const id = this.jwtService.getUsuarioActual().id;

    this.usuarioService.obtenerUsuario(id).subscribe({
      next: (usuario) => {
        this.usuarioActual = usuario;
        this.editarUsuarioForm.patchValue(usuario);
      },
      error: (error: any) => {
        this.errorMsg = error.error ?? 'Error al obtener el usuario.';
      }
    });
  }

  submit() {
    if (this.editarUsuarioForm.invalid) {
      this.editarUsuarioForm.markAllAsTouched();
      return;
    }

    const usuarioEditado = this.editarUsuarioForm.value as Usuario;

    this.usuarioService.editarUsuario(usuarioEditado).subscribe({
      next: () => {
        this.errorMsg = null;
        this.router.navigate(['/perfil/' + this.usuarioActual?.id]);
      },
      error: (error: any) => {
        this.errorMsg = error.error ?? 'Error al editar el usuario.';
      }
    });
  }

   onImagenSeleccionada(event: Event): void {
    const input = event.target as HTMLInputElement;

    if (input.files && input.files.length > 0) {
      this.imagenSeleccionada = input.files[0];

      const reader = new FileReader();
      reader.onload = () => {
        this.imagenActual = reader.result as string;
      };
      reader.readAsDataURL(this.imagenSeleccionada);
    }
  }

  actualizarImagen(): void {
    if(this.imagenSeleccionada){
      this.imagenService.subirImagen(this.imagenSeleccionada).subscribe({
        next: (imagen:Imagen) => {
          console.log(imagen);
          this.usuarioService.editarImagen(imagen).subscribe({
          next: () => {
            this.router.navigate(['perfil/' + this.usuarioActual?.id])
          },
          error: (error: any) => {
            this.errorMsg = error.error ?? 'Error al subir la imagen.';
          }
        });
        },
        error: (error: any) => {
          this.errorMsg = error.error ?? 'Error al subir la imagen.';
        }
      });
    }
  }
}

