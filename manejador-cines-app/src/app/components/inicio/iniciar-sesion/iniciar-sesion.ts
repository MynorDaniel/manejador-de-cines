import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../services/auth-service';
import { Credenciales } from '../../../models/credenciales';
import { Router } from '@angular/router';

@Component({
  selector: 'app-iniciar-sesion',
  imports: [ReactiveFormsModule],
  templateUrl: './iniciar-sesion.html',
  styleUrl: './iniciar-sesion.css'
})
export class IniciarSesion {

  authService:AuthService = inject(AuthService);
  router:Router = inject(Router);
  errorMsg: string | null = null;



  iniciarSesionForm:FormGroup = new FormGroup({
    correo: new FormControl('', [Validators.required, Validators.email, Validators.max(350)]),
    clave: new FormControl('', [Validators.required])
  })

  credenciales!: Credenciales;

  submit() {
    this.credenciales = this.iniciarSesionForm.value as Credenciales;
    this.authService.iniciarSesion(this.credenciales).subscribe({
      next: (token) => {
          sessionStorage.setItem('token', JSON.stringify(token))
          this.router.navigate(['/home']);
      },
      error: (error: any) => {
          this.errorMsg = error.error;
      }
    });
  }

}