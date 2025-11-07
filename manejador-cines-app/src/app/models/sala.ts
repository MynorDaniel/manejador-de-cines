export interface Sala {
    id?: string;
    idCine: string;
    filasAsientos: string;
    columnasAsientos: string;
    calificacionesBloqueadas?: string;
    comentariosBloqueados?: string;
    visible?: string;
    calificacion?: number;
    calificacionDelUsuarioActual?: number;
    idCalificacionUsuario?: number;
}