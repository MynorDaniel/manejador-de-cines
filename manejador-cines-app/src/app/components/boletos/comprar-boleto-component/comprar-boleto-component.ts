import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProyeccionService } from '../../../services/proyeccion-service';
import { BoletoService } from '../../../services/boleto-service';
import { Proyeccion } from '../../../models/proyeccion';
import { Boleto } from '../../../models/boleto';

@Component({
  selector: 'app-comprar-boleto-component',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './comprar-boleto-component.html',
  styleUrl: './comprar-boleto-component.css'
})
export class ComprarBoletoComponent implements OnInit {

  route = inject(ActivatedRoute);
  router = inject(Router);
  proyeccionService = inject(ProyeccionService);
  boletoService = inject(BoletoService);

  proyeccion?: Proyeccion;
  cargando = true;
  errorMsg: string | null = null;
  infoMsg: string | null = null;
  procesandoCompra = false;

  compraForm = new FormGroup({
    fechaPago: new FormControl('', [Validators.required]),
    confirmar: new FormControl(false, [Validators.requiredTrue])
  });

  ngOnInit(): void {
    const idProyeccion = this.route.snapshot.paramMap.get('idProyeccion');
    
    if (!idProyeccion) {
      this.errorMsg = 'ID de proyección no válido';
      this.cargando = false;
      return;
    }

    const fechaActual = new Date().toISOString().split('T')[0];
    this.compraForm.patchValue({
      fechaPago: fechaActual
    });

    this.proyeccionService.verProyeccion(idProyeccion).subscribe({
      next: (proyeccion) => {
        this.proyeccion = proyeccion;
        this.cargando = false;
      },
      error: (err) => {
        this.errorMsg = err.error || 'Error al cargar la proyección';
        this.cargando = false;
      }
    });
  }

  comprarBoleto(): void {
    if (this.compraForm.invalid) {
      this.errorMsg = 'Complete todos los campos correctamente y confirme la compra';
      return;
    }

    if (!this.proyeccion) {
      this.errorMsg = 'No se pudo obtener información de la proyección';
      return;
    }

    this.procesandoCompra = true;
    this.errorMsg = null;
    this.infoMsg = null;

    const boleto: Boleto = {
      idProyeccion: this.proyeccion.id!,
      pagoDTO: {
        idUsuario: 0,
        fecha: this.compraForm.value.fechaPago!,
        monto: +(this.proyeccion.precio ?? 0)
      }
    };

    this.boletoService.comprarBoleto(boleto).subscribe({
      next: () => {
        this.infoMsg = 'Boleto comprado exitosamente';
        this.procesandoCompra = false;
        
      },
      error: (err) => {
        this.errorMsg = err.error || 'Error al procesar la compra';
        this.procesandoCompra = false;
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/cines']);
  }
}