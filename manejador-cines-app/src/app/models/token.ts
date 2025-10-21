import { Usuario } from "./usuario";

export interface Token {
    token: string;
    tipo: string;
    expiraEn: number;
    usuario: Usuario;
}
