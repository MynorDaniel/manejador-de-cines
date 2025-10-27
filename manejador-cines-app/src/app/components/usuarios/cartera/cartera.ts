import { Component, inject, OnInit } from '@angular/core';
import { CarteraService } from '../../../services/cartera-service';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CarteraI } from '../../../models/cartera';

@Component({
  selector: 'app-cartera',
  imports: [ReactiveFormsModule],
  templateUrl: './cartera.html',
  styleUrl: './cartera.css'
})
export class Cartera implements OnInit{
  carteraService = inject(CarteraService);
  
  cartera: CarteraI | null = null;
  errorMsg: string | null = null;
  infoMsg: string | null = null;
  
  carteraForm: FormGroup = new FormGroup({
    monto: new FormControl('', [Validators.required, Validators.min(0.01), Validators.pattern(/^\d+(\.\d{1,2})?$/)])
  });

  ngOnInit(): void {
    this.cargarCartera();
  }

  cargarCartera(): void {
    this.carteraService.obtenerCartera().subscribe({
      next: (cartera) => {
        this.cartera = cartera;
      },
      error: (error) => {
        console.error('Error al cargar la cartera:', error);
      }
    });
  }

  agregarSaldo(): void {
    if (this.carteraForm.invalid) {
      this.carteraForm.markAllAsTouched();
      return;
    }

    const monto = this.carteraForm.value.monto;
    
    if (this.cartera) {
      this.cartera.saldo = Math.round((this.cartera.saldo + monto) * 100) / 100;

      this.carteraService.editarCartera(this.cartera).subscribe({
      next: () => {
        this.infoMsg = "Cartera actualizada";
        
      },
      error: (error) => {
        this.errorMsg = error.error;
      }
    });
    }
    
    this.carteraForm.reset();
  }
}
