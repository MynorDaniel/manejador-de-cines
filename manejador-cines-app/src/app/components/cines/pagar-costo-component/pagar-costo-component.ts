import { Component, inject, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CineService } from '../../../services/cine-service';
import { Pago } from '../../../models/pago';

@Component({
  selector: 'app-pagar-costo-component',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './pagar-costo-component.html',
  styleUrl: './pagar-costo-component.css'
})
export class PagarCostoComponent implements OnInit {
  @Input() idCine!: number;
  @Output() cerrar = new EventEmitter<boolean>();

  cineService = inject(CineService);

  deuda?: Pago;
  cargandoDeuda = true;
  errorDeuda = '';
  procesandoPago = false;
  errorPago = '';

  ngOnInit(): void {
    this.cargarDeuda();
  }

  cargarDeuda(): void {
    this.cargandoDeuda = true;
    this.errorDeuda = '';

    this.cineService.verDeuda(this.idCine).subscribe({
      next: (deuda) => {
        this.deuda = deuda;
        this.cargandoDeuda = false;
      },
      error: (err) => {
        console.error('Error al cargar deuda:', err);
        this.errorDeuda = err.error
        this.cargandoDeuda = false;
      }
    });
  }

  confirmarPago(): void {
    if (!this.deuda) return;

    this.procesandoPago = true;
    this.errorPago = '';

    this.cineService.pagarCostoDiario(this.idCine).subscribe({
      next: () => {
        this.cerrar.emit(true);
      },
      error: (err) => {
        console.error('Error al procesar pago:', err);
        this.errorPago = err.error
        this.procesandoPago = false;
      }
    });
  }

  cancelar(): void {
    this.cerrar.emit(false);
  }
}