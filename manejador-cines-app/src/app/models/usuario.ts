import { Imagen } from "./imagen";

export interface Usuario {
    id: number;
    imagen: Imagen;
    nombre: string;
    rol: string;
    correo: string;
    activado: boolean;
}
