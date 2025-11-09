import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ReporteService } from '../../../services/reporte-service';
import { FiltrosReporteAdminCines } from '../../../models/filtros-reporte-admin-cines';
import { SalaService } from '../../../services/sala-service';
import { Sala } from '../../../models/sala';
import { CineService } from '../../../services/cine-service';
import { Cine } from '../../../models/cine';
import { JwtService } from '../../../services/jwt-service';

@Component({
  selector: 'app-reporte-admin-cines-component',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './reporte-admin-cines-component.html',
  styleUrl: './reporte-admin-cines-component.css'
})
export class ReporteAdminCinesComponent implements OnInit {

  reporteService = inject(ReporteService);
  salaService = inject(SalaService);
  cineService = inject(CineService);
  jwtService = inject(JwtService);
  fb = inject(FormBuilder);

  filtrosForm: FormGroup;
  salas: Sala[] = [];
  cines: Cine[] = [];
  cargando = false;
  errorMsg: string | null = null;
  infoMsg: string | null = null;

  tiposReporte = [
    { valor: 'comentarios', texto: 'Comentarios de Salas' },
    { valor: 'peliculas', texto: 'Películas Más Gustadas' },
    { valor: 'salas', texto: 'Salas Más Gustadas' },
    { valor: 'boletos', texto: 'Boletos Vendidos' }
  ];

  constructor() {
    this.filtrosForm = this.fb.group({
      tipoReporte: ['comentarios', Validators.required],
      idSala: [''],
      fechaInicio: [''],
      fechaFin: ['']
    });
  }

  ngOnInit(): void {
    this.cargarCinesDelUsuario();
  }

  cargarCinesDelUsuario(): void {
    const usuarioActual = this.jwtService.getUsuarioActual();
    
    this.cineService.verCinesPorUsuario(usuarioActual.id).subscribe({
      next: (cines) => {
        this.cines = cines;
        
        if (this.cines.length > 0) {
          this.cargarSalasDeCines();
        } else {
          this.errorMsg = 'No tienes cines registrados';
        }
      },
      error: (err) => {
        this.errorMsg = err.error || 'Error al cargar los cines';
      }
    });
  }

  cargarSalasDeCines(): void {
    this.salaService.verSalas().subscribe({
      next: (salas) => {
        const idsCinesUsuario = this.cines.map(cine => cine.id);
        this.salas = salas.filter(sala => 
          idsCinesUsuario.includes(sala.idCine)
        );
      },
      error: (err) => {
        this.errorMsg = err.error || 'Error al cargar las salas';
      }
    });
  }

  validarFormulario(): boolean {
    const fechaInicio = this.filtrosForm.get('fechaInicio')?.value;
    const fechaFin = this.filtrosForm.get('fechaFin')?.value;

    if (fechaInicio > fechaFin) {
      this.errorMsg = 'La fecha de inicio debe ser menor o igual a la fecha de fin';
      return false;
    }

    if (!this.filtrosForm.get('tipoReporte')?.value) {
      this.errorMsg = 'Debe seleccionar un tipo de reporte';
      return false;
    }

    return true;
  }

  generarReporte(): void {
    this.errorMsg = null;
    this.infoMsg = null;

    if (!this.validarFormulario()) {
      return;
    }

    this.cargando = true;

    const filtros: FiltrosReporteAdminCines = {
      fechaInicio: this.filtrosForm.get('fechaInicio')?.value,
      fechaFin: this.filtrosForm.get('fechaFin')?.value
    };

    const idSala = this.filtrosForm.get('idSala')?.value;
    if (idSala) {
      filtros.idSala = idSala;
    }

    const tipoReporte = this.filtrosForm.get('tipoReporte')?.value;

    this.reporteService.verReporteAdminCines(filtros, tipoReporte).subscribe({
      next: (blob) => {
        this.cargando = false;
        this.infoMsg = 'Reporte generado exitosamente';
        
        const url = window.URL.createObjectURL(blob);
        window.open(url, '_blank');
      },
      error: (err) => {
        this.cargando = false;
        this.errorMsg = "Sin coincidencias"
      }
    });
  }

  limpiarFormulario(): void {
    this.filtrosForm.reset({
      tipoReporte: 'comentarios'
    });
    this.errorMsg = null;
    this.infoMsg = null;
  }

  obtenerNombreCine(idCine: number): string {
    const cine = this.cines.find(c => c.id === idCine.toString());
    return cine ? cine.nombre : `Cine ${idCine}`;
  }
}