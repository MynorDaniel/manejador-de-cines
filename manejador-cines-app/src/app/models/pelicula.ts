import { Calificacion } from "./calificacion";
import { Categoria } from "./categoria";
import { Clasificacion } from "./clasificacion";
import { Comentario } from "./comentario";
import { Imagen } from "./imagen";

export interface Pelicula { 
    id?: string;
    imagen: Imagen;
    clasificacion: Clasificacion;
    titulo: string;
    sinopsis: string;
    fechaEstreno: string;
    duracion: string;
    director: string;
    reparto: string;
    categorias: Categoria[];
    calificaciones?: Calificacion[];
    comentarios?: Comentario[];
}