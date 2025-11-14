import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ReporteService } from '../../../services/reporte-service';
import { ReporteGanancias } from '../../../models/reporte-ganancias';

@Component({
  selector: 'app-reporte-admin-sistema-component',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './reporte-admin-sistema-component.html',
  styleUrl: './reporte-admin-sistema-component.css'
})
export class ReporteAdminSistemaComponent implements OnInit {

  reporteService = inject(ReporteService);
  fb = inject(FormBuilder);

  filtrosForm: FormGroup;
  reporteGanancias: ReporteGanancias | null = null;
  cargando = false;
  errorMsg: string | null = null;
  infoMsg: string | null = null;

  constructor() {
    this.filtrosForm = this.fb.group({
      fechaInicio: [''],
      fechaFin: ['']
    });
  }

  ngOnInit(): void {
  }

  validarFormulario(): boolean {
    const fechaInicio = this.filtrosForm.get('fechaInicio')?.value;
    const fechaFin = this.filtrosForm.get('fechaFin')?.value;

    if (fechaInicio && fechaFin && fechaInicio > fechaFin) {
      this.errorMsg = 'La fecha de inicio debe ser menor o igual a la fecha de fin';
      return false;
    }

    return true;
  }

  generarReporte(): void {
    this.errorMsg = null;
    this.infoMsg = null;
    this.reporteGanancias = null;

    if (!this.validarFormulario()) {
      return;
    }

    this.cargando = true;

    const fechaInicio = this.filtrosForm.get('fechaInicio')?.value || undefined;
    const fechaFin = this.filtrosForm.get('fechaFin')?.value || undefined;

    this.reporteService.verReporteGanancias("false", fechaInicio, fechaFin).subscribe({
      next: (reporte) => {
        this.cargando = false;
        this.reporteGanancias = reporte;
        this.infoMsg = 'Reporte generado exitosamente';
      },
      error: (err) => {
        this.cargando = false;
        this.errorMsg = err.error || 'Error al generar el reporte';
      }
    });
  }

  generarReportePDF(): void {
    this.errorMsg = null;
    this.infoMsg = null;

    if (!this.validarFormulario()) {
      return;
    }

    const fechaInicio = this.filtrosForm.get('fechaInicio')?.value || undefined;
    const fechaFin = this.filtrosForm.get('fechaFin')?.value || undefined;

    this.reporteService.verReporteGanancias("true", fechaInicio, fechaFin).subscribe({
      next: (response: any) => {
        const blob = new Blob([response], { type: 'application/pdf' });
        
        if (blob.size === 0) {
          this.errorMsg = 'El PDF generado está vacío';
          return;
        }

        const url = window.URL.createObjectURL(blob);
        
        window.open(url, '_blank');
        
        setTimeout(() => {
          window.URL.revokeObjectURL(url);
        }, 100);
        
        this.infoMsg = 'PDF abierto en nueva pestaña';
      },
      error: (err) => {
        console.error('Error al generar PDF:', err);
        this.errorMsg = 'Error al generar el PDF';
      }
    });
  }

  limpiarFormulario(): void {
    this.filtrosForm.reset();
    this.reporteGanancias = null;
    this.errorMsg = null;
    this.infoMsg = null;
  }
}
