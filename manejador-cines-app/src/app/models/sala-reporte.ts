import { BoletoReporte } from "./boleto-reporte";
import { CalificacionReporte } from "./calificacion-reporte";
import { ComentarioReporte } from "./comentario-reporte";
import { ProyeccionReporte } from "./proyeccion-reporte";
import { UsuarioReporteDTO } from "./usuario-reporte";

export interface SalaReporte {
    // Info de la sala
    id: string;
    nombreCine: string;
    filas: string;
    columnas: string;
    calificacionesBloqueadas: string;
    comentariosBloqueados: string;
    visible: string;
    
    // Reporte comentarios
    comentarios: ComentarioReporte[];

    // Reporte peliculas proyectadas
    proyecciones: ProyeccionReporte[];

    // Reporte salas más gustadas
    promedioCalificacion: string;
    calificaciones: CalificacionReporte[];
    
    // Reporte boletos vendidos
    totalDineroBoletosVendidos: string;
    boletos: BoletoReporte[];

}