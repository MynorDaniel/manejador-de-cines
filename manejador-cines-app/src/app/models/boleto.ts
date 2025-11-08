import { Cine } from "./cine";
import { Pago } from "./pago";
import { Pelicula } from "./pelicula";
import { Proyeccion } from "./proyeccion";
import { Sala } from "./sala";

export interface Boleto {   
  id?: string;
  idUsuario?: string;
  idProyeccion: string;
  pagoDTO?: Pago;
  cine?: Cine;
  sala?: Sala;
  pelicula?: Pelicula;
  proyeccion?: Proyeccion;
}