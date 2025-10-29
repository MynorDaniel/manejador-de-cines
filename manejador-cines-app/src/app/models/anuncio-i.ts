import { Pago } from "./pago";

export interface AnuncioI {
    vigencia: number;
    tipo: "TEXTO" | "TEXTO_IMAGEN" | "TEXTO_VIDEO";
    texto: string;
    idMedia: number | null;
    pago: Pago;
}
