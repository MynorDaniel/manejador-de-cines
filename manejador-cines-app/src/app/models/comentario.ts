import { Usuario } from "./usuario";

export interface Comentario {
    id?: string;
    idUsuario?: string;
    contenido: string;
    fecha?: string;
    usuario?: Usuario
}