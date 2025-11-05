export interface Cine {
  id?: string;
  idUsuarioCreador?: string;
  nombre: string;
  ubicacion: string;
  activado?: string;
  fechaCreacion: string;
  bloqueoActivo?: boolean;
  fechaUltimoCambioDeCosto: string;
}
