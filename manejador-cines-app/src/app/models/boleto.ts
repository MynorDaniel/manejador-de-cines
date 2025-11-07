import { Pago } from "./pago";

export interface Boleto {   
  id?: string;
  idUsuario?: string;
  idProyeccion: string;
  pagoDTO?: Pago;
}