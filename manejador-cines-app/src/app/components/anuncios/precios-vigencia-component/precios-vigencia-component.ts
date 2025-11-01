import { Component, inject, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { VigenciaAnuncio } from '../../../models/vigencia-anuncio';
import { AnuncioService } from '../../../services/anuncio-service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-precios-vigencia-component',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './precios-vigencia-component.html',
  styleUrl: './precios-vigencia-component.css'
})
export class PreciosVigenciaComponent implements OnInit {
  
  anuncioService = inject(AnuncioService);
  
  errorMsg: string | null = null;
  infoMsg: string | null = null;
  vigencias: VigenciaAnuncio[] = [];
  cargandoVigencias: boolean = false;
  procesando: boolean = false;
  vigenciaSeleccionada: VigenciaAnuncio | null = null;
  montoActual: number = 0;

  vigenciaForm: FormGroup = new FormGroup({
    dias: new FormControl('', [Validators.required]),
    monto: new FormControl('', [Validators.required, Validators.min(0.01)])
  });

  ngOnInit(): void {
    this.cargarVigencias();
  }

  cargarVigencias(): void {
    this.cargandoVigencias = true;
    this.anuncioService.verPreciosDeVigencias().subscribe({
      next: (vigencias) => {
        this.vigencias = vigencias;
        this.cargandoVigencias = false;
      },
      error: (error) => {
        this.errorMsg = error.error;
        console.error('Error al cargar vigencias:', error);
        this.cargandoVigencias = false;
      }
    });
  }

  onVigenciaChange(): void {
    const diasSeleccionados = this.vigenciaForm.value.dias;
    
    if (diasSeleccionados) {
      this.vigenciaSeleccionada = this.vigencias.find(v => v.dias === diasSeleccionados) || null;
      
      if (this.vigenciaSeleccionada) {
        this.montoActual = parseFloat(this.vigenciaSeleccionada.monto);
        this.vigenciaForm.patchValue({
          monto: this.montoActual
        });
      }
    } else {
      this.vigenciaSeleccionada = null;
      this.montoActual = 0;
      this.vigenciaForm.patchValue({ monto: '' });
    }

    this.errorMsg = null;
    this.infoMsg = null;
  }

  editarPrecio(): void {
    if (this.vigenciaForm.invalid) {
      this.vigenciaForm.markAllAsTouched();
      return;
    }

    this.procesando = true;
    this.errorMsg = null;
    this.infoMsg = null;

    const vigenciaActualizada: VigenciaAnuncio = {
      dias: this.vigenciaForm.value.dias,
      monto: this.vigenciaForm.value.monto.toString()
    };

    this.anuncioService.cambiarPrecioVigenciaAnuncio(vigenciaActualizada).subscribe({
      next: () => {
        this.infoMsg = 'Precio actualizado exitosamente';
        this.procesando = false;
        
        this.montoActual = parseFloat(vigenciaActualizada.monto);
        
        this.cargarVigencias();
      },
      error: (error) => {
        this.errorMsg = error.error;
        this.procesando = false;
        console.error('Error:', error);
      }
    });
  }
}