import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { CineService } from '../../../services/cine-service';
import { Cine } from '../../../models/cine';
import { JwtService } from '../../../services/jwt-service';
import { Usuario } from '../../../models/usuario';
import { AnuncioService } from '../../../services/anuncio-service';
import { PagarCostoComponent } from '../pagar-costo-component/pagar-costo-component';

@Component({
  selector: 'app-ver-cine-component',
  imports: [CommonModule, PagarCostoComponent],
  templateUrl: './ver-cine-component.html',
  styleUrl: './ver-cine-component.css'
})
export class VerCineComponent {

  route = inject(ActivatedRoute);
  router = inject(Router);
  cineService = inject(CineService);
  jwtService = inject(JwtService);
  anuncioService = inject(AnuncioService);

  usuarioActual: Usuario = this.jwtService.getUsuarioActual();
  idCreador: number = 0;

  cine?: Cine;
  cargando = true;
  mostrarModalPago = false;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.cineService.verCine(id).subscribe({
      next: (cine) => {
        this.cine = cine;
        this.cargando = false;
        this.idCreador = Number(cine.idUsuarioCreador);
        if (cine.bloqueoActivo != undefined) {
          this.anuncioService.setMostrarAnuncios(!cine.bloqueoActivo);
        }
      },
      error: (err) => {
        console.error('Error al cargar cine:', err);
        this.cargando = false;
      }
    });
  }

  ngOnDestroy() {
    this.anuncioService.setMostrarAnuncios(true);
  }

  editarCine(cine: Cine) {
    this.router.navigate(['/editar-cine', cine.id]);
  }

  bloquearAnuncios(cine: Cine) {
    this.router.navigate(['/bloqueo-anuncios', cine.id]);
  }

  cambiarCostoDiario() {
    this.router.navigate(['/costo-diario', this.cine!.id]);
  }

  abrirModalPago() {
    this.mostrarModalPago = true;
  }

  cerrarModalPago(pagoExitoso: boolean) {
    this.mostrarModalPago = false;
    if (pagoExitoso) {
      this.ngOnInit();
    }
  }

  crearSala(cine: Cine) {
    this.router.navigate(['/crear-sala', cine.id]);
  }

  verSalas(cine: Cine) {
    this.router.navigate(['/ver-salas', cine.id]);
  }
}