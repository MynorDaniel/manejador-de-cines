import { Component, inject, OnInit } from '@angular/core';
import { AnuncioService } from '../../../services/anuncio-service';
import { TipoAnuncio } from '../../../models/tipo-anuncio';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-precios-tipo-component',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './precios-tipo-component.html',
  styleUrl: './precios-tipo-component.css'
})
export class PreciosTipoComponent implements OnInit {
  
  anuncioService = inject(AnuncioService);
  
  errorMsg: string | null = null;
  infoMsg: string | null = null;
  tipos: TipoAnuncio[] = [];
  cargandoTipos: boolean = false;
  procesando: boolean = false;
  tipoSeleccionado: TipoAnuncio | null = null;
  montoActual: number = 0;

  tipoForm: FormGroup = new FormGroup({
    tipo: new FormControl('', [Validators.required]),
    monto: new FormControl('', [Validators.required, Validators.min(0.01)])
  });

  ngOnInit(): void {
    this.cargarPrecios();
  }

  cargarPrecios(): void {
    this.cargandoTipos = true;
    this.anuncioService.verPreciosDeTipos().subscribe({
      next: (tipos) => {
        this.tipos = tipos;
        this.cargandoTipos = false;
      },
      error: (error) => {
        this.errorMsg = error.error;
        console.error('Error al cargar tipos:', error);
        this.cargandoTipos = false;
      }
    });
  }

  onTipoChange(): void {
    const tipoSeleccionadoValue = this.tipoForm.value.tipo;
    
    if (tipoSeleccionadoValue) {
      this.tipoSeleccionado = this.tipos.find(t => t.tipo === tipoSeleccionadoValue) || null;
      
      if (this.tipoSeleccionado) {
        this.montoActual = parseFloat(this.tipoSeleccionado.monto);
        this.tipoForm.patchValue({
          monto: this.montoActual
        });
      }
    } else {
      this.tipoSeleccionado = null;
      this.montoActual = 0;
      this.tipoForm.patchValue({ monto: '' });
    }

    this.errorMsg = null;
    this.infoMsg = null;
  }

  editarPrecio(): void {
    if (this.tipoForm.invalid) {
      this.tipoForm.markAllAsTouched();
      return;
    }

    this.procesando = true;
    this.errorMsg = null;
    this.infoMsg = null;

    const tipoActualizado: TipoAnuncio = {
      tipo: this.tipoForm.value.tipo,
      monto: this.tipoForm.value.monto.toString()
    };

    this.anuncioService.cambiarPrecioTipoAnuncio(tipoActualizado).subscribe({
      next: () => {
        this.infoMsg = 'Precio actualizado exitosamente';
        this.procesando = false;
        
        this.montoActual = parseFloat(tipoActualizado.monto);
        
        this.cargarPrecios();
      },
      error: (error) => {
        this.errorMsg = error.error;
        this.procesando = false;
        console.error('Error:', error);
      }
    });
  }

  formatearTipo(tipo: string): string {
    const nombres: { [key: string]: string } = {
      'TEXTO': 'Texto Simple',
      'TEXTO_IMAGEN': 'Texto con Imagen',
      'TEXTO_VIDEO': 'Texto con Video'
    };
    return nombres[tipo] || tipo;
  }
}