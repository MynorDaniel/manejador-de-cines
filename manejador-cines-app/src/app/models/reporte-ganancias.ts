import { AnuncioReporte } from "./anuncio-reporte";
import { BloqueoReporte } from "./bloqueo-reporte";
import { CineReporte } from "./cine-reporte";

export interface ReporteGanancias {
    costo: string;
    ingreso: string;
    ganancia: string;
    cines: CineReporte[];
    anuncios: AnuncioReporte[];
    bloqueos: BloqueoReporte[];
}