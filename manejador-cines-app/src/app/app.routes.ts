import { Routes } from '@angular/router';
import { Inicio } from './components/inicio/inicio/inicio';
import { IniciarSesion } from './components/inicio/iniciar-sesion/iniciar-sesion';
import { Home } from './components/inicio/home/home';
import { Registro } from './components/usuarios/registro/registro';

export const routes: Routes = [
    {path: '', component: Inicio},
    {path: 'login', component: IniciarSesion},
    {path: 'home', component: Home},
    {path: 'registro', component: Registro}
];
