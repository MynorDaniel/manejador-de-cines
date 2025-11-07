/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.servicios;

import com.mynor.manejador.cines.api.bd.BaseDeDatos;
import com.mynor.manejador.cines.api.bd.ProyeccionBD;
import com.mynor.manejador.cines.api.bd.Transaccion;
import com.mynor.manejador.cines.api.dtos.ProyeccionDTO;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.excepciones.ProyeccionInvalidaException;
import com.mynor.manejador.cines.api.filtros.FiltrosProyeccion;
import com.mynor.manejador.cines.api.modelo.Pelicula;
import com.mynor.manejador.cines.api.modelo.Proyeccion;
import com.mynor.manejador.cines.api.modelo.Sala;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;

/**
 *
 * @author mynordma
 */
public class ProyeccionServicio {
    
    private BaseDeDatos<Proyeccion, FiltrosProyeccion> PROYECCION_BD;
    
    public ProyeccionServicio(){
        PROYECCION_BD = new ProyeccionBD();
    }

    public void crearProyeccion(ProyeccionDTO proyeccionDTO) throws AccesoDeDatosException, ProyeccionInvalidaException {
        if(fechaHoraAntesDeAhora(proyeccionDTO.getFecha(), proyeccionDTO.getHora())) throw new ProyeccionInvalidaException("Fecha y hora ya pasaron");
        
        Proyeccion proyeccion = new Proyeccion();

        Sala sala = new Sala();
        sala.setId(Integer.valueOf(proyeccionDTO.getIdSala()));
        proyeccion.setSala(sala);

        Pelicula pelicula = new Pelicula();
        pelicula.setId(Integer.valueOf(proyeccionDTO.getIdPelicula()));
        proyeccion.setPelicula(pelicula);

        proyeccion.setFecha(LocalDate.parse(proyeccionDTO.getFecha()));
        proyeccion.setHora(LocalTime.parse(proyeccionDTO.getHora()));
        
        try (Transaccion t = new Transaccion()) {
            PROYECCION_BD.crear(proyeccion, t.obtenerConexion());
            t.commit();
        }
    }
    
    private boolean fechaHoraAntesDeAhora(String fechaStr, String horaStr) {
        try {
            LocalDate fecha = LocalDate.parse(fechaStr);
            LocalTime hora = LocalTime.parse(horaStr);
            LocalDateTime fechaHora = LocalDateTime.of(fecha, hora);
            LocalDateTime ahora = LocalDateTime.now();
            return fechaHora.isBefore(ahora);
        } catch (DateTimeParseException | NullPointerException e) {
            return false;
        }
    }
    
    private boolean fechaAntesDeHoy(String fechaStr) {
        try {
            LocalDate fecha = LocalDate.parse(fechaStr);
            LocalDate hoy = LocalDate.now();
            return fecha.isBefore(hoy);
        } catch (DateTimeParseException | NullPointerException e) {
            return false;
        }
    }

    public void editarProyeccion(ProyeccionDTO proyeccionDTO) throws AccesoDeDatosException, ProyeccionInvalidaException {
        
        Proyeccion proyeccion = leerPorId(Integer.valueOf(proyeccionDTO.getId()));
        
        if(!proyeccion.getFecha().equals(LocalDate.parse(proyeccionDTO.getFecha()))){
            if(fechaAntesDeHoy(proyeccionDTO.getFecha())) throw new ProyeccionInvalidaException("Fecha ya pasó");
            
        }
        
        Sala sala = new Sala();
        sala.setId(Integer.valueOf(proyeccionDTO.getIdSala()));
        proyeccion.setSala(sala);

        Pelicula pelicula = new Pelicula();
        pelicula.setId(Integer.valueOf(proyeccionDTO.getIdPelicula()));
        proyeccion.setPelicula(pelicula);

        proyeccion.setFecha(LocalDate.parse(proyeccionDTO.getFecha()));
        proyeccion.setHora(LocalTime.parse(proyeccionDTO.getHora()));

        try (Transaccion t = new Transaccion()) {
            PROYECCION_BD.actualizar(proyeccion, t.obtenerConexion());
            t.commit();
        }
    }
    
    public Proyeccion leerPorId(Integer id) throws AccesoDeDatosException {
        FiltrosProyeccion filtros = new FiltrosProyeccion();
        filtros.setId(Optional.ofNullable(id));
        
        try (Transaccion t = new Transaccion()) {
            Proyeccion[] coincidencias = PROYECCION_BD.leer(filtros, t.obtenerConexion());
            t.commit();
            
            if(coincidencias.length < 1) throw new AccesoDeDatosException("Sin coincidencias");
            
            return coincidencias[0];
        }
    }
    
    public Proyeccion[] leerPorSala(Integer id) throws AccesoDeDatosException {
        FiltrosProyeccion filtros = new FiltrosProyeccion();
        filtros.setIdSala(Optional.ofNullable(id));
        
        try (Transaccion t = new Transaccion()) {
            Proyeccion[] coincidencias = PROYECCION_BD.leer(filtros, t.obtenerConexion());
            t.commit();
            
            if(coincidencias.length < 1) return null;
            
            return coincidencias;
        }
    }

    public ProyeccionDTO verPorId(Integer id) throws AccesoDeDatosException {
        Proyeccion p = leerPorId(id);
        
        ProyeccionDTO pDTO = new ProyeccionDTO();
        pDTO.setFecha(p.getFecha().toString());
        pDTO.setHora(p.getHora().toString());
        pDTO.setId(p.getId().toString());
        pDTO.setIdPelicula(p.getPelicula().getId().toString());
        pDTO.setIdSala(p.getSala().getId().toString());
        pDTO.setPrecio(p.getPrecio().toString());
        return pDTO;
    }
    
    
}
