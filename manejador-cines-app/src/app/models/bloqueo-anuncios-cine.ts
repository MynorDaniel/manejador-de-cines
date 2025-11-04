import { Pago } from "./pago";

export interface BloqueoAnunciosCine{
  idCine: string;
  pago: Pago;
  dias: string;
}