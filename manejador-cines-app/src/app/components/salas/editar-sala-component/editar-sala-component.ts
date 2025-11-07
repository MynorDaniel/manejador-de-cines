import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Sala } from '../../../models/sala';
import { SalaService } from '../../../services/sala-service';

@Component({
  selector: 'app-editar-sala-component',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './editar-sala-component.html',
  styleUrl: './editar-sala-component.css'
})
export class EditarSalaComponent implements OnInit {

  errorMsg: string | null = null;
  infoMsg: string | null = null;
  
  salaService = inject(SalaService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  
  idSala: string = '';
  idCine: string = '';
  cargando = true;
  
  salaForm = new FormGroup({
    filasAsientos: new FormControl('', [Validators.required, Validators.min(1)]),
    columnasAsientos: new FormControl('', [Validators.required, Validators.min(1)]),
    calificacionesBloqueadas: new FormControl(false),
    comentariosBloqueados: new FormControl(false),
    visible: new FormControl(true)
  });
  
  ngOnInit(): void {
    this.idSala = this.route.snapshot.paramMap.get('id') || '';
    
    if (!this.idSala) {
      this.errorMsg = 'ID de sala no proporcionado';
      this.cargando = false;
      return;
    }
    
    this.cargarSala();
  }
  
  cargarSala(): void {
    this.salaService.verSala(this.idSala).subscribe({
      next: (sala) => {
        this.idCine = sala.idCine;
        
        this.salaForm.patchValue({
          filasAsientos: sala.filasAsientos,
          columnasAsientos: sala.columnasAsientos,
          calificacionesBloqueadas: sala.calificacionesBloqueadas === 'true',
          comentariosBloqueados: sala.comentariosBloqueados === 'true',
          visible: sala.visible === 'true'
        });
        
        this.cargando = false;
      },
      error: (err) => {
        this.errorMsg = err.error || 'Error al cargar la sala';
        this.cargando = false;
      }
    });
  }
  
  editarSala(): void {
    if (this.salaForm.invalid) {
      this.errorMsg = 'Complete todos los campos';
      return;
    }
    
    const sala: Sala = {
      id: this.idSala,
      idCine: this.idCine,
      filasAsientos: this.salaForm.value.filasAsientos!,
      columnasAsientos: this.salaForm.value.columnasAsientos!,
      calificacionesBloqueadas: this.salaForm.value.calificacionesBloqueadas ? 'true' : 'false',
      comentariosBloqueados: this.salaForm.value.comentariosBloqueados ? 'true' : 'false',
      visible: this.salaForm.value.visible ? 'true' : 'false'
    };
    
    this.salaService.editarSala(sala).subscribe({
      next: () => {
        this.infoMsg = 'Sala editada exitosamente';
        this.router.navigate(['/ver-sala', this.idSala]);
      },
      error: (err) => {
        this.errorMsg = err.error || 'Error al editar la sala';
      }
    });
  }
}