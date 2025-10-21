import { Component, inject, OnInit } from '@angular/core';
import { UsuarioService } from '../../../services/usuario-service';
import { Router } from '@angular/router';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Usuario } from '../../../models/usuario';
import { JwtService } from '../../../services/jwt-service';

@Component({
  selector: 'app-registro',
  imports: [ReactiveFormsModule],
  templateUrl: './registro.html',
  styleUrl: './registro.css'
})
export class Registro implements OnInit{
  
  usuarioService: UsuarioService = inject(UsuarioService);
  jwtService = inject(JwtService);
  router: Router = inject(Router);
  errorMsg: string | null = null;
  infoMsg: string | null = null;
  usuarioActualAdmin: Boolean = false;

  registrarUsuarioForm: FormGroup = new FormGroup({
    nombre: new FormControl('', [Validators.required, Validators.maxLength(200)]),
    rol: new FormControl('', [Validators.required]),
    correo: new FormControl('', [Validators.required, Validators.email, Validators.maxLength(350)]),
    clave: new FormControl('', [Validators.required])
  });

  usuarioEntrada!: Usuario;

  ngOnInit(): void {
    let usuarioActual = this.jwtService.getUsuarioActual();
    if(!usuarioActual) return;

    if(usuarioActual.rol == 'ADMINISTRADOR_SISTEMA'){
      this.usuarioActualAdmin = true;
    }
  }

  submit() {
    if (this.registrarUsuarioForm.invalid) {
      this.registrarUsuarioForm.markAllAsTouched();
      return;
    }

    this.usuarioEntrada = this.registrarUsuarioForm.value as Usuario;

    this.usuarioService.crearUsuario(this.usuarioEntrada).subscribe({
      next: () => {
        this.errorMsg = null;
        this.infoMsg = 'Usuario creado';
        this.registrarUsuarioForm.reset();
      },
      error: (error: any) => {
        this.errorMsg = error.error ?? 'Error al registrar el usuario.';
      }
    });
  }
}
