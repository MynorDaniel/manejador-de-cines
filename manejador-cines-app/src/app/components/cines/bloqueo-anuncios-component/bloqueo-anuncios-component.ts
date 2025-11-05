import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CineService } from '../../../services/cine-service';
import { JwtService } from '../../../services/jwt-service';
import { BloqueoAnunciosCine } from '../../../models/bloqueo-anuncios-cine';
import { Pago } from '../../../models/pago';

@Component({
  selector: 'app-bloqueo-anuncios-component',
  imports: [],
  templateUrl: './bloqueo-anuncios-component.html',
  styleUrl: './bloqueo-anuncios-component.css'
})
export class BloqueoAnunciosComponent {
  idCine!: string;
  bloqueo!: BloqueoAnunciosCine;
  usuarioActual!: any;
  procesando = false;

  errorMsg: string | null = null;
  infoMsg: string | null = null;

  route = inject(ActivatedRoute);
  router = inject(Router);
  cineService = inject(CineService);
  jwtService = inject(JwtService);

  ngOnInit(): void {

    this.idCine = this.route.snapshot.paramMap.get('id')!;
    this.usuarioActual = this.jwtService.getUsuarioActual();

    const pago: Pago = {
      idUsuario: this.usuarioActual.id,
      fecha: new Date().toISOString().split('T')[0],
      monto: 35.0
    };

    this.bloqueo = {
      idCine: this.idCine,
      pago,
      dias: '30'
    };
  }

  confirmarBloqueo(): void {
    this.procesando = true;
    this.cineService.bloquearAnuncios(this.bloqueo).subscribe({
      next: () => {
        this.infoMsg = 'Anuncios bloqueados exitosamente por 30 días.';
        this.router.navigate(['/ver-cine', this.idCine]);
      },
      error: (err) => {
        console.error('Error al bloquear anuncios:', err);
        this.errorMsg = err.error;
        this.procesando = false;
      }
    });
  }
}
