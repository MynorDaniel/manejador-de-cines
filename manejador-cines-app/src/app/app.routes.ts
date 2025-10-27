import { Routes } from '@angular/router';
import { Inicio } from './components/inicio/inicio/inicio';
import { IniciarSesion } from './components/inicio/iniciar-sesion/iniciar-sesion';
import { Home } from './components/inicio/home/home';
import { Registro } from './components/usuarios/registro/registro';
import { Perfil } from './components/usuarios/perfil/perfil';
import { Usuarios } from './components/usuarios/usuarios/usuarios';
import { EditarUsuario } from './components/usuarios/editar-usuario/editar-usuario';
import { Cartera } from './components/usuarios/cartera/cartera';

export const routes: Routes = [
    {path: '', component: Inicio},
    {path: 'login', component: IniciarSesion},
    {path: 'home', component: Home},
    {path: 'registro', component: Registro},
    {path: 'perfil/:id', component: Perfil},
    {path: 'usuarios', component: Usuarios},
    {path: 'editar-usuario', component: EditarUsuario},
    {path: 'cartera', component: Cartera}
];