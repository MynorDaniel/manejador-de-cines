import { Injectable, inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, GuardResult, MaybeAsync, Router, RouterStateSnapshot } from '@angular/router';
import { JwtService } from './jwt-service';

@Injectable({
  providedIn: 'root'
})
export class RolGuardService implements CanActivate {
  
  private router = inject(Router);
  private jwtService = inject(JwtService);

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): MaybeAsync<GuardResult> {
    const allowedRoles = route.data['allowedRoles'] as string[];
    
    if (!allowedRoles || allowedRoles.length === 0) {
      return true;
    }

    if (!this.rolPermitido(allowedRoles)) {
      this.router.navigate(['/']);
      return false;
    }

    return true;
  }

  rolPermitido(allowedRoles: string[]): boolean {
    const usuario = this.jwtService.getUsuarioActual();
    return usuario?.rol != null && allowedRoles.includes(usuario.rol);
  }
}
