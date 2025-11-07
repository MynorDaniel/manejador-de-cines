import { Proyeccion } from "./proyeccion";

export interface Cine {
  id?: string;
  idUsuarioCreador?: string;
  nombre: string;
  ubicacion: string;
  activado?: string;
  fechaCreacion: string;
  bloqueoActivo?: boolean;
  fechaUltimoCambioDeCosto: string;
  proyecciones?: Proyeccion[];
}
