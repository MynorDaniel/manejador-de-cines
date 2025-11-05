import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CineService } from '../../../services/cine-service';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { Cine } from '../../../models/cine';

@Component({
  selector: 'app-editar-cine-component',
  imports: [ReactiveFormsModule],
  templateUrl: './editar-cine-component.html',
  styleUrl: './editar-cine-component.css'
})
export class EditarCineComponent implements OnInit {

  route = inject(ActivatedRoute);
  router = inject(Router);
  cineService = inject(CineService);

  cineForm!: FormGroup;
  cine!: Cine;
  cargando = true;

  errorMsg: string | null = null;
  infoMsg: string | null = null;

  ngOnInit(): void {
    this.cineForm = new FormGroup({
      nombre: new FormControl('', Validators.required),
      ubicacion: new FormControl('', Validators.required),
      activado: new FormControl('', Validators.required)
    });

    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.cargarCine(id);
  }

  cargarCine(id: number): void {
    this.cineService.verCine(id).subscribe({
      next: (cine) => {
        this.cine = cine;
        this.cargando = false;
        this.cineForm.patchValue({
          nombre: cine.nombre,
          ubicacion: cine.ubicacion,
          activado: cine.activado
        });
      },
      error: (err) => {
        console.error('Error al cargar el cine:', err);
        this.errorMsg = err.error;
        this.cargando = false;
      }
    });
  }

  guardarCambios(): void {
    if (this.cineForm.invalid) return;

    const cineActualizado: Cine = {
      ...this.cine,
      ...this.cineForm.value
    };

    this.cineService.editarCine(cineActualizado).subscribe({
      next: () => {
        this.infoMsg = 'Cine actualizado correctamente';
        this.router.navigate(['/ver-cine', cineActualizado.id]);
      },
      error: (err) => this.errorMsg = err.error
    });
  }
}
