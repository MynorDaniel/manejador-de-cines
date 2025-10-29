import { Component, EventEmitter, inject, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Pago } from '../../../models/pago';
import { JwtService } from '../../../services/jwt-service';

@Component({
  selector: 'app-pago-form',
  imports: [ReactiveFormsModule],
  templateUrl: './pago-form.html',
  styleUrl: './pago-form.css'
})
export class PagoForm implements OnInit {
  @Input() monto: number = 0;
  @Input() anuncioForm: FormGroup | undefined;
  @Output() pagoData = new EventEmitter<Pago>();

  jwtService = inject(JwtService);

  pagoForm = new FormGroup({
    fecha: new FormControl('', Validators.required)
  });

  ngOnChanges(): void {
    if (this.anuncioForm?.valid && this.pagoForm.valid) {
      this.emitirPago();
    }
  }

  ngOnInit(): void {
    const hoy = new Date().toISOString().split('T')[0];
    this.pagoForm.patchValue({ fecha: hoy });

    this.anuncioForm?.valueChanges.subscribe(() => {
      if (this.anuncioForm?.valid) {
        this.emitirPago();
      }
    });

    this.pagoForm?.valueChanges.subscribe(() => {
      if (this.anuncioForm?.valid) {
        this.emitirPago();
      }
    });

    if (this.anuncioForm?.valid) {
      this.emitirPago();
    }
  }

  emitirPago(): void {
    if (this.pagoForm.valid) {
      const usuarioId = this.jwtService.getUsuarioActual().id;
      
      const pago: Pago = {
        idUsuario: usuarioId,
        fecha: this.pagoForm.value.fecha!,
        monto: this.monto
      } as Pago; 

      console.log("Monto: ", this.monto);
      
      this.pagoData.emit(pago);
    }
  }
}
