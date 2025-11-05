import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CineService } from '../../../services/cine-service';
import { CostoDiarioCine } from '../../../models/costo-diario';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-costo-diario-component',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './costo-diario-component.html',
  styleUrl: './costo-diario-component.css'
})
export class CostoDiarioComponent implements OnInit {
  cineService = inject(CineService);
  router = inject(Router);
  route = inject(ActivatedRoute);
  fb = inject(FormBuilder);

  costoDiarioForm!: FormGroup;
  idCine = '';
  cargando = false;
  mensajeError = '';
  nombreCine = '';
  fechaCambioAnterior = '';

  ngOnInit(): void {
    this.idCine = this.route.snapshot.paramMap.get('id') || '';
    
    if (!this.idCine) {
      this.mensajeError = 'No se especificó el ID del cine';
      return;
    }

    this.inicializarFormulario();
    this.cargarInfoCine(this.idCine);
  }

  inicializarFormulario(): void {
    this.costoDiarioForm = this.fb.group({
      fechaCambio: ['', [Validators.required]],
      monto: ['', [Validators.required, Validators.min(0.01)]]
    });
  }

  cargarInfoCine(id: string): void {
    this.cineService.verCine(Number(id)).subscribe({
      next: (cine) => {
        this.nombreCine = cine.nombre;
        this.fechaCambioAnterior = cine.fechaUltimoCambioDeCosto;
      },
      error: (err) => {
        console.error('Error al cargar cine:', err);
        this.mensajeError = 'Error al cargar información del cine';
      }
    });
  }

  guardarCosto(): void {
    if (this.costoDiarioForm.invalid) {
      this.mensajeError = 'Por favor complete todos los campos correctamente';
      this.costoDiarioForm.markAllAsTouched();
      return;
    }

    this.cargando = true;
    this.mensajeError = '';

    const costoDiario: CostoDiarioCine = {
      idCine: this.idCine,
      fechaCambio: this.costoDiarioForm.value.fechaCambio,
      monto: this.costoDiarioForm.value.monto.toString()
    };

    this.cineService.crearCostoDiario(costoDiario).subscribe({
      next: () => {
        this.router.navigate(['/ver-cine', this.idCine]);
      },
      error: (err) => {
        console.error('Error al crear costo diario:', err);
        this.mensajeError = err.error;
        this.cargando = false;
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/ver-cine', this.idCine]);
  }

  get fechaCambio() {
    return this.costoDiarioForm.get('fechaCambio');
  }

  get monto() {
    return this.costoDiarioForm.get('monto');
  }
}