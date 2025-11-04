import { Component, inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Cine } from '../../../models/cine';
import { CineService } from '../../../services/cine-service';

@Component({
  selector: 'app-crear-cine-component',
  imports: [ReactiveFormsModule],
  templateUrl: './crear-cine-component.html',
  styleUrl: './crear-cine-component.css'
})
export class CrearCineComponent {

  cineService = inject(CineService);

  errorMsg: string | null = null;
  infoMsg: string | null = null;

  cineForm = new FormGroup({
    nombre: new FormControl('', Validators.required),
    ubicacion: new FormControl('', Validators.required),
    fechaCreacion: new FormControl('', Validators.required)
  });

  guardarCine() {
    if (this.cineForm.valid) {
      const nuevoCine:Cine = this.cineForm.value as Cine;
      console.log('Cine creado:', nuevoCine);

      this.cineService.crearCine(nuevoCine).subscribe({
        next: () => {
          this.infoMsg = 'Cine creado exitosamente';
          this.errorMsg = null;
          this.cineForm.reset();
        },
        error: (error) => {
          this.errorMsg = error.error;
          console.error('Error:', error);
        }
      })


    } else {
      this.errorMsg = 'Datos incorrectos'
    }
  }

}
