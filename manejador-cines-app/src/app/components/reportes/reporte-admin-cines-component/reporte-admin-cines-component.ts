import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ReporteService } from '../../../services/reporte-service';
import { SalaService } from '../../../services/sala-service';
import { Sala } from '../../../models/sala';
import { CineService } from '../../../services/cine-service';
import { Cine } from '../../../models/cine';
import { JwtService } from '../../../services/jwt-service';
import { SalaReporte } from '../../../models/sala-reporte';

@Component({
  selector: 'app-reporte-admin-cines-component',
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
  salasReporte: SalaReporte[] = [];
  cargando = false;
  errorMsg: string | null = null;
  infoMsg: string | null = null;

  tiposReporte = [
    { valor: 'comentarios', texto: 'Comentarios de Salas' },
    { valor: 'peliculas', texto: 'Películas Proyectadas' },
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

    if (fechaInicio && fechaFin && fechaInicio > fechaFin) {
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
    this.salasReporte = [];

    if (!this.validarFormulario()) {
      return;
    }

    this.cargando = true;

    const fechaInicio = this.filtrosForm.get('fechaInicio')?.value || undefined;
    const fechaFin = this.filtrosForm.get('fechaFin')?.value || undefined;
    const idSala = this.filtrosForm.get('idSala')?.value || undefined;
    const tipoReporte = this.filtrosForm.get('tipoReporte')?.value;

    let observable;

    switch (tipoReporte) {
      case 'comentarios':
        observable = this.reporteService.verReporteComentariosSalas("false", fechaInicio, fechaFin, idSala);
        break;
      case 'peliculas':
        observable = this.reporteService.verReportePeliculasProyectadas("false", fechaInicio, fechaFin, idSala);
        break;
      case 'salas':
        observable = this.reporteService.verReporteSalasMasGustadas("false", fechaInicio, fechaFin, idSala);
        break;
      case 'boletos':
        observable = this.reporteService.verReporteBoletoVendidos("false", fechaInicio, fechaFin, idSala);
        break;
      default:
        this.cargando = false;
        this.errorMsg = 'Tipo de reporte no válido';
        return;
    }

    observable.subscribe({
      next: (salas) => {
        this.cargando = false;
        this.salasReporte = salas;
        
        if (this.salasReporte.length === 0) {
          this.infoMsg = 'No se encontraron resultados para los filtros seleccionados';
        } else {
          this.infoMsg = 'Reporte generado exitosamente';
        }
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
    const idSala = this.filtrosForm.get('idSala')?.value || undefined;
    const tipoReporte = this.filtrosForm.get('tipoReporte')?.value;

    let observable;

    switch (tipoReporte) {
      case 'comentarios':
        observable = this.reporteService.verReporteComentariosSalas("true", fechaInicio, fechaFin, idSala);
        break;
      case 'peliculas':
        observable = this.reporteService.verReportePeliculasProyectadas("true", fechaInicio, fechaFin, idSala);
        break;
      case 'salas':
        observable = this.reporteService.verReporteSalasMasGustadas("true", fechaInicio, fechaFin, idSala);
        break;
      case 'boletos':
        observable = this.reporteService.verReporteBoletoVendidos("true", fechaInicio, fechaFin, idSala);
        break;
      default:
        this.errorMsg = 'Tipo de reporte no válido';
        return;
    }

    observable.subscribe({
      next: (response: any) => {
        // Crear un blob con el tipo MIME correcto
        const blob = new Blob([response], { type: 'application/pdf' });
        
        // Verificar que sea un Blob válido
        if (blob.size === 0) {
          this.errorMsg = 'El PDF generado está vacío';
          return;
        }

        // Crear URL del blob
        const url = window.URL.createObjectURL(blob);
        
        // Abrir en nueva pestaña
        window.open(url, '_blank');
        
        // Liberar la URL después de un tiempo
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
    this.filtrosForm.reset({
      tipoReporte: 'comentarios'
    });
    this.salasReporte = [];
    this.errorMsg = null;
    this.infoMsg = null;
  }

  obtenerNombreCine(idCine: number): string {
    const cine = this.cines.find(c => c.id === idCine.toString());
    return cine ? cine.nombre : `Cine ${idCine}`;
  }

  get tipoReporteSeleccionado(): string {
    return this.filtrosForm.get('tipoReporte')?.value;
  }
}