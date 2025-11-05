import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CineService } from '../../../services/cine-service';
import { CostoGlobalCines } from '../../../models/costo-global';

@Component({
  selector: 'app-costo-global-component',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './costo-global-component.html',
  styleUrl: './costo-global-component.css'
})
export class CostoGlobalComponent implements OnInit {
  cineService = inject(CineService);
  router = inject(Router);
  fb = inject(FormBuilder);

  costoGlobalForm!: FormGroup;
  costoActual: string = '';
  costoActualId: string = '';
  cargando = false;
  cargandoCosto = true;
  mensajeError = '';

  ngOnInit(): void {
    this.inicializarFormulario();
    this.cargarCostoActual();
  }

  inicializarFormulario(): void {
    this.costoGlobalForm = this.fb.group({
      monto: ['', [Validators.required, Validators.min(0.01)]]
    });
  }

  cargarCostoActual(): void {
    this.cargandoCosto = true;
    this.cineService.verCostoGlobal().subscribe({
      next: (costo) => {
        this.costoActual = costo.monto;
        this.costoActualId = costo.id || '';
        this.cargandoCosto = false;
      },
      error: (err) => {
        console.error('Error al cargar costo global:', err);
        this.mensajeError = 'Error al cargar el costo global actual';
        this.cargandoCosto = false;
      }
    });
  }

  guardarCosto(): void {
    if (this.costoGlobalForm.invalid) {
      this.mensajeError = 'Por favor ingrese un monto válido';
      this.costoGlobalForm.markAllAsTouched();
      return;
    }

    this.cargando = true;
    this.mensajeError = '';

    const costoGlobal: CostoGlobalCines = {
      id: this.costoActualId, 
      monto: this.costoGlobalForm.value.monto.toString()
    };

    this.cineService.cambiarCostoGlobal(costoGlobal).subscribe({
      next: () => {
        this.cargarCostoActual();
        this.costoGlobalForm.reset();
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error al actualizar costo global:', err);
        this.mensajeError = err.error;
        this.cargando = false;
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/home']);
  }

  get monto() {
    return this.costoGlobalForm.get('monto');
  }
}