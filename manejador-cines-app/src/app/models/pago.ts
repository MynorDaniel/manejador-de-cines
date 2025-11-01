import { Usuario } from "./usuario";

export interface Pago {
    idUsuario: number;
    fecha: string;
    monto: number;
}
