import { Pago } from "./pago";

export interface AnuncioI {
    id?: number;
    vigencia: number;
    tipo: "TEXTO" | "TEXTO_IMAGEN" | "TEXTO_VIDEO";
    texto: string;
    idMedia: number | null;
    pago?: Pago;
    activado?: boolean;
}
