/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.servicios;

import com.mynor.manejador.cines.api.bd.BaseDeDatos;
import com.mynor.manejador.cines.api.bd.CalificacionBD;
import com.mynor.manejador.cines.api.bd.CalificacionSalaBD;
import com.mynor.manejador.cines.api.bd.SalaBD;
import com.mynor.manejador.cines.api.bd.Transaccion;
import com.mynor.manejador.cines.api.dtos.CalificacionSalaDTO;
import com.mynor.manejador.cines.api.dtos.SalaDTO;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.excepciones.UsuarioInvalidoException;
import com.mynor.manejador.cines.api.filtros.FiltrosCalificacion;
import com.mynor.manejador.cines.api.filtros.FiltrosCalificacionSala;
import com.mynor.manejador.cines.api.filtros.FiltrosSala;
import com.mynor.manejador.cines.api.modelo.Calificacion;
import com.mynor.manejador.cines.api.modelo.CalificacionSala;
import com.mynor.manejador.cines.api.modelo.Cine;
import com.mynor.manejador.cines.api.modelo.Rol;
import com.mynor.manejador.cines.api.modelo.Sala;
import com.mynor.manejador.cines.api.modelo.Usuario;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

/**
 *
 * @author mynordma
 */
public class SalaServicio {
    
    private final BaseDeDatos<Sala, FiltrosSala> SALA_BD;
    private final BaseDeDatos<CalificacionSala, FiltrosCalificacionSala> CALIFICACION_SALA_BD;
    private final BaseDeDatos<Calificacion, FiltrosCalificacion> CALIFICACION_BD;
    
    public SalaServicio(){
        SALA_BD = new SalaBD();
        CALIFICACION_SALA_BD = new CalificacionSalaBD();
        CALIFICACION_BD = new CalificacionBD();
    }


    public void crearSala(SalaDTO salaDTO) throws AccesoDeDatosException {
        
        Cine cine = new Cine();
        cine.setId(Integer.valueOf(salaDTO.getIdCine()));
        
        Sala sala = new Sala();
        sala.setCalificacionesBloqueadas(Boolean.FALSE);
        sala.setComentariosBloqueados(Boolean.FALSE);
        sala.setCine(cine);
        sala.setColumnasAsientos(Integer.valueOf(salaDTO.getColumnasAsientos()));
        sala.setFilasAsientos(Integer.valueOf(salaDTO.getFilasAsientos()));
        sala.setVisible(Boolean.TRUE);
        
        try(Transaccion t = new Transaccion()){
            SALA_BD.crear(sala, t.obtenerConexion());
            t.commit();
        }
    }

    public SalaDTO[] verSalas(Integer idUsuario) throws AccesoDeDatosException, UsuarioInvalidoException {
        Sala[] salas = filtrarSalasPorRolDeUsuario(leerTodas(), idUsuario);
        return Arrays.stream(salas).map(sala -> {
            
           SalaDTO salaDTO = new SalaDTO();
           salaDTO.setCalificacionesBloqueadas(sala.getCalificacionesBloqueadas().toString());
           salaDTO.setColumnasAsientos(sala.getColumnasAsientos().toString());
           salaDTO.setFilasAsientos(sala.getFilasAsientos().toString());
           salaDTO.setComentariosBloqueados(sala.getComentariosBloqueados().toString());
           salaDTO.setId(sala.getId().toString());
           salaDTO.setIdCine(sala.getCine().getId().toString());
           salaDTO.setVisible(sala.getVisible().toString());
           salaDTO.setCalificacion(obtenerCalificacionTotal(sala.getId()));
           
           Optional<CalificacionSala> c = leerCalificacionASalaPorUsuario(idUsuario, sala.getId());
           
           if(c.isPresent()){
               salaDTO.setCalificacionDelUsuarioActual(c.get().getValor());
               salaDTO.setIdCalificacionUsuario(c.get().getId());
           }
           
           
           return salaDTO;
        }).toArray(SalaDTO[]::new);
    }

    public SalaDTO verSala(Integer id, Integer idUsuario) throws AccesoDeDatosException, UsuarioInvalidoException {
        Sala sala = leerPorId(id);
        
        UsuarioServicio usuarioServicio = new UsuarioServicio();
        Usuario usuario = usuarioServicio.leerPorId(idUsuario);
        
        Rol rol = usuario.getRol();
        
        if(!sala.getVisible() && (rol == Rol.ANUNCIANTE || rol == Rol.CLIENTE)) throw new AccesoDeDatosException("Sala no disponible");
        
        SalaDTO salaDTO = new SalaDTO();
        salaDTO.setCalificacionesBloqueadas(sala.getCalificacionesBloqueadas().toString());
        salaDTO.setColumnasAsientos(sala.getColumnasAsientos().toString());
        salaDTO.setFilasAsientos(sala.getFilasAsientos().toString());
        salaDTO.setComentariosBloqueados(sala.getComentariosBloqueados().toString());
        salaDTO.setId(sala.getId().toString());
        salaDTO.setIdCine(sala.getCine().getId().toString());
        salaDTO.setVisible(sala.getVisible().toString());
        salaDTO.setCalificacion(obtenerCalificacionTotal(sala.getId()));
        Optional<CalificacionSala> c = leerCalificacionASalaPorUsuario(idUsuario, sala.getId());
           
        if(c.isPresent()){
            salaDTO.setCalificacionDelUsuarioActual(c.get().getValor());
            salaDTO.setIdCalificacionUsuario(c.get().getId());
        }

        return salaDTO;
    }

    public SalaDTO[] verSalasPorCine(Integer idCine, Integer idUsuario) throws AccesoDeDatosException, UsuarioInvalidoException {
        Sala[] salas = filtrarSalasPorRolDeUsuario(leerPorCine(idCine), idUsuario);
        return Arrays.stream(salas).map(sala -> {
            
           SalaDTO salaDTO = new SalaDTO();
           salaDTO.setCalificacionesBloqueadas(sala.getCalificacionesBloqueadas().toString());
           salaDTO.setColumnasAsientos(sala.getColumnasAsientos().toString());
           salaDTO.setFilasAsientos(sala.getFilasAsientos().toString());
           salaDTO.setComentariosBloqueados(sala.getComentariosBloqueados().toString());
           salaDTO.setId(sala.getId().toString());
           salaDTO.setIdCine(sala.getCine().getId().toString());
           salaDTO.setVisible(sala.getVisible().toString());
           salaDTO.setCalificacion(obtenerCalificacionTotal(sala.getId()));
           Optional<CalificacionSala> c = leerCalificacionASalaPorUsuario(idUsuario, sala.getId());
           
           if(c.isPresent()){
               salaDTO.setCalificacionDelUsuarioActual(c.get().getValor());
               salaDTO.setIdCalificacionUsuario(c.get().getId());
           }
           
           return salaDTO;
        }).toArray(SalaDTO[]::new);
    }

    public void editarSala(SalaDTO salaDTO) throws AccesoDeDatosException {
        Sala sala = leerPorId(Integer.valueOf(salaDTO.getId()));
        
        sala.setFilasAsientos(Integer.valueOf(salaDTO.getFilasAsientos()));
        sala.setColumnasAsientos(Integer.valueOf(salaDTO.getColumnasAsientos()));
        sala.setCalificacionesBloqueadas(Boolean.valueOf(salaDTO.getCalificacionesBloqueadas()));
        sala.setComentariosBloqueados(Boolean.valueOf(salaDTO.getComentariosBloqueados()));
        sala.setVisible(Boolean.valueOf(salaDTO.getVisible()));
        
        try(Transaccion t = new Transaccion()){
            SALA_BD.actualizar(sala, t.obtenerConexion());
            t.commit();
        }
        
    }

    /**
     * Si no existe una calificacion la crea
     * @param calificacionDTO 
     * @throws com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException 
     */
    public void cambiarCalificacion(CalificacionSalaDTO calificacionDTO) throws AccesoDeDatosException {
        
        Sala sala = leerPorId(Integer.valueOf(calificacionDTO.getIdSala()));
        if(sala.getCalificacionesBloqueadas()) throw new AccesoDeDatosException("Calificaciones bloqueadas");
        
        Optional<CalificacionSala> calificacionExistente = leerCalificacionSala(Integer.valueOf(calificacionDTO.getIdUsuario()), Integer.valueOf(calificacionDTO.getIdSala()));
        
        if(calificacionExistente.isPresent()){
            CalificacionSala calificacion = calificacionExistente.get();
            calificacion.setValor(Integer.valueOf(calificacionDTO.getValor()));
            
            try(Transaccion t = new Transaccion()){
                CALIFICACION_BD.actualizar(calificacion, t.obtenerConexion());
                t.commit();
            }
            
        }else{
            Calificacion calificacion = new CalificacionSala();
            calificacion.setFecha(LocalDate.now());
            calificacion.setValor(Integer.valueOf(calificacionDTO.getValor()));
            
            Usuario usuario = new Usuario();
            usuario.setId(Integer.valueOf(calificacionDTO.getIdUsuario()));
            
            calificacion.setUsuario(usuario);
            
            try(Transaccion t = new Transaccion()){
                CalificacionSala calificacionCreada = (CalificacionSala) CALIFICACION_BD.crear(calificacion, t.obtenerConexion());
                calificacionCreada.setSala(sala);
                CALIFICACION_SALA_BD.crear(calificacionCreada, t.obtenerConexion());
                t.commit();
            }
        }
        
    }

    public void eliminarCalificacion(CalificacionSalaDTO calificacionDTO) throws AccesoDeDatosException {
        try(Transaccion t = new Transaccion()){
            CalificacionSala calificacion = leerCalificacionSala(Integer.valueOf(calificacionDTO.getIdUsuario()), Integer.valueOf(calificacionDTO.getIdSala())).get();
            
            CALIFICACION_SALA_BD.eliminar(calificacion, t.obtenerConexion());
            t.commit();
        }
    }
    
    public Sala leerPorId(Integer id) throws AccesoDeDatosException{
        FiltrosSala filtros = new FiltrosSala();
        filtros.setId(Optional.ofNullable(id));
        
        try(Transaccion t = new Transaccion()){
            Sala[] coincidencias = SALA_BD.leerCompleto(filtros, t.obtenerConexion());
            t.commit();
            
            if(coincidencias.length < 1) throw new AccesoDeDatosException("Sin coincidencias para la sala " + id);
            
            return coincidencias[0];
        }
    }
    
    protected Sala[] leerPorCine(Integer idCine) throws AccesoDeDatosException{
        FiltrosSala filtros = new FiltrosSala();
        filtros.setIdCine(Optional.ofNullable(idCine));
        
        try(Transaccion t = new Transaccion()){
            Sala[] coincidencias = SALA_BD.leer(filtros, t.obtenerConexion());
            t.commit();
            
            if(coincidencias.length < 1) throw new AccesoDeDatosException("Sin coincidencias para salas con cine" + idCine);
            
            return coincidencias;
        }
    }
    
    private Sala[] leerTodas() throws AccesoDeDatosException{
        FiltrosSala filtros = new FiltrosSala();
        
        try(Transaccion t = new Transaccion()){
            Sala[] coincidencias = SALA_BD.leer(filtros, t.obtenerConexion());
            t.commit();
            
            if(coincidencias.length < 1) throw new AccesoDeDatosException("Sin coincidencias para salas");
            
            return coincidencias;
        }
    }
    
    private Optional<CalificacionSala> leerCalificacionSala(Integer idUsuario, Integer idSala) throws AccesoDeDatosException{
        FiltrosCalificacionSala filtros = new FiltrosCalificacionSala();
        filtros.setIdUsuario(Optional.ofNullable(idUsuario));
        filtros.setIdSala(Optional.ofNullable(idSala));
        
        try(Transaccion t = new Transaccion()){
            CalificacionSala[] coincidencias = CALIFICACION_SALA_BD.leer(filtros, t.obtenerConexion());
            t.commit();
            
            if(coincidencias.length < 1) {
                return Optional.empty();
            }
            
            return Optional.of(coincidencias[0]);
        }
    }

    private Integer obtenerCalificacionTotal(Integer idSala) {
        System.out.println(idSala);
        FiltrosCalificacionSala filtros = new FiltrosCalificacionSala();
        filtros.setIdSala(Optional.ofNullable(idSala));
        
        Integer calificacionSuma = 0;
        Integer calificacionTotal = 0;
        
        try(Transaccion t = new Transaccion()){
            Calificacion[] coincidencias = CALIFICACION_SALA_BD.leerCompleto(filtros, t.obtenerConexion());
            t.commit();
            
            if(coincidencias.length < 1) {
                return 0;
            }
            
            for (Calificacion coincidencia : coincidencias) {
                calificacionSuma += coincidencia.getValor();
            }
            
            calificacionTotal = Math.round(calificacionSuma / coincidencias.length);
            
            System.out.println("Calificacion a sala " + idSala + ": " + calificacionTotal);
            
            return calificacionTotal;
            
        } catch (AccesoDeDatosException ex) {
            System.out.println(ex.getMessage());
            return 0;
        }
    }

    private Optional<CalificacionSala> leerCalificacionASalaPorUsuario(Integer idUsuario, Integer idSala) {
        FiltrosCalificacionSala filtros = new FiltrosCalificacionSala();
        filtros.setIdUsuario(Optional.ofNullable(idUsuario));
        filtros.setIdSala(Optional.ofNullable(idSala));
        
        try(Transaccion t = new Transaccion()){
            CalificacionSala[] coincidencias = CALIFICACION_SALA_BD.leer(filtros, t.obtenerConexion());
            t.commit();
            
            if(coincidencias.length < 1) {
                CalificacionSala c = new CalificacionSala();
                
                return Optional.empty();
            }
            
            return Optional.of(coincidencias[0]);
            
        } catch (AccesoDeDatosException ex) {
            return Optional.empty();
        } 
    }
    
    private Sala[] filtrarSalasPorRolDeUsuario(Sala[] salas, Integer idUsuario) throws AccesoDeDatosException, UsuarioInvalidoException{
        UsuarioServicio usuarioServicio = new UsuarioServicio();
        Usuario usuario = usuarioServicio.leerPorId(idUsuario);
        
        Rol rol = usuario.getRol();
        
        if(rol == Rol.CLIENTE || rol == Rol.ANUNCIANTE){
            return Arrays.stream(salas).filter(sala -> sala.getVisible()).toArray(Sala[]::new);
        }else{
            return salas;
        }
        
    }
    
}
