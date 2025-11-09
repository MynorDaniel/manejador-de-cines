import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ReporteService } from '../../../services/reporte-service';
import { FiltrosReporteAdminSistema } from '../../../models/filtros-reporte-admin-sistema';
import { CineService } from '../../../services/cine-service';
import { Cine } from '../../../models/cine';
import { UsuarioService } from '../../../services/usuario-service';
import { Usuario } from '../../../models/usuario';

@Component({
  selector: 'app-reporte-admin-sistema-component',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './reporte-admin-sistema-component.html',
  styleUrl: './reporte-admin-sistema-component.css'
})
export class ReporteAdminSistemaComponent implements OnInit {

  reporteService = inject(ReporteService);
  cineService = inject(CineService);
  usuarioService = inject(UsuarioService);
  fb = inject(FormBuilder);

  filtrosForm: FormGroup;
  cines: Cine[] = [];
  anunciantes: Usuario[] = [];
  cargando = false;
  errorMsg: string | null = null;
  infoMsg: string | null = null;

  tiposReporte = [
    { valor: 'ganancias', texto: 'Ganancias del Sistema' },
    { valor: 'anuncios', texto: 'Anuncios Publicados' },
    { valor: 'ganancias-anunciante', texto: 'Ganancias por Anunciante' },
    { valor: 'salas-populares', texto: 'Salas Más Populares' },
    { valor: 'salas-comentadas', texto: 'Salas Más Comentadas' }
  ];

  tiposAnuncio = [
    { valor: 'TEXTO_IMAGEN', texto: 'Texto e Imagen' },
    { valor: 'TEXTO_VIDEO', texto: 'Texto y Video' },
    { valor: 'TEXTO', texto: 'Texto' }
  ];

  diasVigencia = [
    { valor: '1', texto: '1 día' },
    { valor: '3', texto: '3 días' },
    { valor: '7', texto: '7 días' },
    { valor: '14', texto: '14 días' }
  ];

  constructor() {
    this.filtrosForm = this.fb.group({
      tipoReporte: ['ganancias', Validators.required],
      fechaInicio: [''],
      fechaFin: [''],
      tipoAnuncio: [''],
      vigencia: [''],
      anuncianteId: ['']
    });

    this.filtrosForm.get('tipoReporte')?.valueChanges.subscribe(() => {
      this.limpiarFiltrosOpcionales();
    });
  }

  ngOnInit(): void {
    this.cargarAnunciantes();
  }

  cargarAnunciantes(): void {
    this.usuarioService.obtenerUsuarios().subscribe({
      next: (usuarios) => {
        this.anunciantes = usuarios.filter(usuario => 
          usuario.rol === 'ANUNCIANTE'
        );
      },
      error: (err) => {
        console.error('Error al cargar anunciantes:', err);
      }
    });
  }

  limpiarFiltrosOpcionales(): void {
    this.filtrosForm.patchValue({
      fechaInicio: '',
      fechaFin: '',
      tipoAnuncio: '',
      vigencia: '',
      anuncianteId: ''
    });
  }

  mostrarFiltrosAnuncios(): boolean {
    return this.filtrosForm.get('tipoReporte')?.value === 'anuncios';
  }

  mostrarFiltroAnunciante(): boolean {
    return this.filtrosForm.get('tipoReporte')?.value === 'ganancias-anunciante';
  }

  validarFormulario(): boolean {
    const fechaInicio = this.filtrosForm.get('fechaInicio')?.value;
    const fechaFin = this.filtrosForm.get('fechaFin')?.value;

    if ((fechaInicio && !fechaFin) || (!fechaInicio && fechaFin)) {
      this.errorMsg = 'Debe ingresar ambas fechas o ninguna';
      return false;
    }

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

    if (!this.validarFormulario()) {
      return;
    }

    this.cargando = true;

    const filtros: FiltrosReporteAdminSistema = {};

    const fechaInicio = this.filtrosForm.get('fechaInicio')?.value;
    const fechaFin = this.filtrosForm.get('fechaFin')?.value;
    
    if (fechaInicio && fechaFin) {
      filtros.fechaInicio = fechaInicio;
      filtros.fechaFin = fechaFin;
    }

    const tipoReporte = this.filtrosForm.get('tipoReporte')?.value;

    if (this.mostrarFiltrosAnuncios()) {
      const tipoAnuncio = this.filtrosForm.get('tipoAnuncio')?.value;
      const vigencia = this.filtrosForm.get('vigencia')?.value;
      
      if (tipoAnuncio) {
        filtros.tipoAnuncio = tipoAnuncio;
      }
      if (vigencia) {
        filtros.vigencia = vigencia;
      }
    }

    if (this.mostrarFiltroAnunciante()) {
      const anuncianteId = this.filtrosForm.get('anuncianteId')?.value;
      if (anuncianteId) {
        filtros.anuncianteId = anuncianteId;
      }
    }

    this.reporteService.verReporteAdminSistema(filtros, tipoReporte).subscribe({
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
      tipoReporte: 'ganancias'
    });
    this.errorMsg = null;
    this.infoMsg = null;
  }
}