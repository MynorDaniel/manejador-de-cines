import { Routes } from '@angular/router';
import { Inicio } from './components/inicio/inicio/inicio';
import { IniciarSesion } from './components/inicio/iniciar-sesion/iniciar-sesion';
import { Home } from './components/inicio/home/home';
import { Registro } from './components/usuarios/registro/registro';
import { Perfil } from './components/usuarios/perfil/perfil';
import { Usuarios } from './components/usuarios/usuarios/usuarios';
import { EditarUsuario } from './components/usuarios/editar-usuario/editar-usuario';
import { Cartera } from './components/usuarios/cartera/cartera';
import { ComprarAnuncio } from './components/anuncios/comprar-anuncio/comprar-anuncio';
import { AnunciosComponent } from './components/anuncios/anuncios-component/anuncios-component';
import { EditarAnuncio } from './components/anuncios/editar-anuncio/editar-anuncio';
import { PreciosTipoComponent } from './components/anuncios/precios-tipo-component/precios-tipo-component';
import { PreciosVigenciaComponent } from './components/anuncios/precios-vigencia-component/precios-vigencia-component';
import { CrearCineComponent } from './components/cines/crear-cine-component/crear-cine-component';

export const routes: Routes = [
    {path: '', component: Inicio},
    {path: 'login', component: IniciarSesion},
    {path: 'home', component: Home},
    {path: 'registro', component: Registro},
    {path: 'perfil/:id', component: Perfil},
    {path: 'usuarios', component: Usuarios},
    {path: 'editar-usuario', component: EditarUsuario},
    {path: 'cartera', component: Cartera},
    {path: 'comprar-anuncio', component: ComprarAnuncio},
    {path: 'ver-anuncios/:propios', component: AnunciosComponent},
    {path: 'editar-anuncio', component: EditarAnuncio},
    {path: 'precios-tipo-anuncio', component: PreciosTipoComponent},
    {path: 'precios-vigencia-anuncio', component: PreciosVigenciaComponent},
    {path: 'crear-cine', component: CrearCineComponent}
];