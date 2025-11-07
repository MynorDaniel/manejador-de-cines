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
import { VerCinesComponent } from './components/cines/ver-cines-component/ver-cines-component';
import { VerCineComponent } from './components/cines/ver-cine-component/ver-cine-component';
import { MisCinesComponent } from './components/cines/mis-cines-component/mis-cines-component';
import { EditarCineComponent } from './components/cines/editar-cine-component/editar-cine-component';
import { BloqueoAnunciosComponent } from './components/cines/bloqueo-anuncios-component/bloqueo-anuncios-component';
import { CostoDiarioComponent } from './components/cines/costo-diario-component/costo-diario-component';
import { CostoGlobalComponent } from './components/cines/costo-global-component/costo-global-component';
import { CrearSalaComponent } from './components/salas/crear-sala-component/crear-sala-component';
import { VerSalasComponent } from './components/salas/ver-salas-component/ver-salas-component';
import { VerSalaComponent } from './components/salas/ver-sala-component/ver-sala-component';
import { EditarSalaComponent } from './components/salas/editar-sala-component/editar-sala-component';
import { CrearPeliculaComponent } from './components/peliculas/crear-pelicula-component/crear-pelicula-component';
import { VerPeliculaComponent } from './components/peliculas/ver-pelicula-component/ver-pelicula-component';
import { EditarPeliculaComponent } from './components/peliculas/editar-pelicula-component/editar-pelicula-component';
import { CrearProyeccionComponent } from './components/proyecciones/crear-proyeccion-component/crear-proyeccion-component';
import { EditarProyeccionComponent } from './components/proyecciones/editar-proyeccion-component/editar-proyeccion-component';
import { ComprarBoletoComponent } from './components/boletos/comprar-boleto-component/comprar-boleto-component';
import { MisBoletosComponent } from './components/boletos/mis-boletos-component/mis-boletos-component';

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
    {path: 'crear-cine', component: CrearCineComponent},
    {path: 'ver-cines', component: VerCinesComponent},
    {path: 'ver-cine/:id', component: VerCineComponent},
    {path: 'mis-cines', component: MisCinesComponent},
    {path: 'editar-cine/:id', component: EditarCineComponent},
    {path: 'bloqueo-anuncios/:id', component: BloqueoAnunciosComponent},
    {path: 'costo-diario/:id', component: CostoDiarioComponent},
    {path: 'costo-global', component: CostoGlobalComponent},
    {path: 'crear-sala/:idCine', component: CrearSalaComponent},
    {path: 'ver-salas/:idCine', component: VerSalasComponent},
    {path: 'ver-salas', component: VerSalasComponent},
    {path: 'ver-sala/:id', component: VerSalaComponent},
    {path: 'editar-sala/:id', component: EditarSalaComponent},
    {path: 'crear-pelicula', component: CrearPeliculaComponent},
    {path: 'ver-pelicula/:id', component: VerPeliculaComponent},
    {path: 'editar-pelicula/:id', component: EditarPeliculaComponent},
    {path: 'crear-proyeccion/:idCine', component: CrearProyeccionComponent},
    {path: 'editar-proyeccion/:id', component: EditarProyeccionComponent},
    {path: 'comprar-boleto/:idProyeccion', component: ComprarBoletoComponent},
    {path: 'mis-boletos', component: MisBoletosComponent}
];