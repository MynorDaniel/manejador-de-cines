import { Component, DoCheck, inject, OnInit } from '@angular/core';
import { Router, RouterLink, RouterModule } from '@angular/router';
import { Usuario } from '../../../models/usuario';

@Component({
  selector: 'app-header',
  imports: [RouterModule, RouterLink],
  templateUrl: './header.html',
  styleUrl: './header.css'
})
export class Header implements OnInit, DoCheck {

  router:Router = inject(Router);
  usuario:Usuario | null = null;

  ngOnInit(): void {
    const token = sessionStorage.getItem('token');
    if (token) {
      this.usuario = JSON.parse(token).usuario as Usuario;
      console.log(this.usuario.nombre);
      
    }
  }

  ngDoCheck(): void {
    const token = sessionStorage.getItem('token');
    this.usuario = token ? JSON.parse(token).usuario : null;
  }

  logout(): void {
    sessionStorage.removeItem('token');
    this.usuario = null;
    this.router.navigate(['/']);
  }

}