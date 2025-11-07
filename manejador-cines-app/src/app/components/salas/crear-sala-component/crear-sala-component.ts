import { Component, inject, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Sala } from '../../../models/sala';
import { SalaService } from '../../../services/sala-service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-crear-sala-component',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './crear-sala-component.html',
  styleUrl: './crear-sala-component.css'
})
export class CrearSalaComponent implements OnInit {

  errorMsg: string | null = null;
  infoMsg: string | null = null;
  
  salaService = inject(SalaService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  
  idCine: string = '';
  
  salaForm = new FormGroup({
    filasAsientos: new FormControl('', [Validators.required, Validators.min(1)]),
    columnasAsientos: new FormControl('', [Validators.required, Validators.min(1)])
  });
  
  ngOnInit(): void {
    this.idCine = this.route.snapshot.paramMap.get('idCine') || '';
    
    if (!this.idCine) {
      this.errorMsg = 'ID de cine no proporcionado';}
  }
  
  crearSala(): void {
    if (this.salaForm.invalid) {
      this.infoMsg = 'Complete todos los campos';
      return;
    }
    
    const sala: Sala = {
      idCine: this.idCine,
      filasAsientos: this.salaForm.value.filasAsientos!,
      columnasAsientos: this.salaForm.value.columnasAsientos!
    };
    
    this.salaService.crearSala(sala).subscribe({
      next: () => {
        this.infoMsg = 'Sala creada exitosamente';
        this.salaForm.reset();
      },
      error: (err) => {
        this.errorMsg = err.error || 'Error al crear la sala';
      }
    });
  }
}