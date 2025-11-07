/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.recursos;

import com.mynor.manejador.cines.api.dtos.CalificacionSalaDTO;
import com.mynor.manejador.cines.api.dtos.SalaDTO;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.excepciones.AutorizacionException;
import com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException;
import com.mynor.manejador.cines.api.excepciones.UsuarioInvalidoException;
import com.mynor.manejador.cines.api.seguridad.Autorizacion;
import com.mynor.manejador.cines.api.servicios.SalaServicio;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

/**
 *
 * @author mynordma
 */
@Path("salas")
public class SalaRecurso {
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crearSala(@HeaderParam("Authorization") String authHeader, SalaDTO salaDTO) {
        
        SalaServicio servicio = new SalaServicio();
        
        try {
            
            salaDTO.validarEntrada();
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarAdminCines();
            
            servicio.crearSala(salaDTO);
            
            return Response.ok().build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (EntidadInvalidaException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } 
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response verSalas(@HeaderParam("Authorization") String authHeader) {
        
        SalaServicio servicio = new SalaServicio();
        
        try {
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            Integer idUsuario = autorizacion.validarSesion().getId();
            
            SalaDTO[] salas = servicio.verSalas(idUsuario);
            
            return Response.ok(salas).build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (EntidadInvalidaException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verSala(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id) {
        
        SalaServicio servicio = new SalaServicio();
        
        try {
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            Integer idUsuario = autorizacion.validarSesion().getId();
            
            SalaDTO sala = servicio.verSala(Integer.valueOf(id), idUsuario);
            
            return Response.ok(sala).build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (NumberFormatException ex){
            return Response.status(Response.Status.BAD_REQUEST).entity("Id inválido").build();
        } catch (EntidadInvalidaException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @GET
    @Path("cine/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verSalasPorCine(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id) {
        
        SalaServicio servicio = new SalaServicio();
        
        try {
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            Integer idUsuario = autorizacion.validarSesion().getId();
            
            SalaDTO[] salas = servicio.verSalasPorCine(Integer.valueOf(id), idUsuario);
            
            return Response.ok(salas).build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (NumberFormatException ex){
            return Response.status(Response.Status.BAD_REQUEST).entity("Id inválido").build();
        } catch (EntidadInvalidaException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editarSala(@HeaderParam("Authorization") String authHeader, SalaDTO salaDTO) {
        
        SalaServicio servicio = new SalaServicio();
        
        try {
            
            salaDTO.validarEdicion();
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarEdicionDeSala(salaDTO);
            
            servicio.editarSala(salaDTO);
            
            return Response.ok().build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (EntidadInvalidaException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } 
    }
    
    @PUT
    @Path("calificacion")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response calificarSala(@HeaderParam("Authorization") String authHeader, CalificacionSalaDTO calificacionDTO) {
        
        SalaServicio servicio = new SalaServicio();
        
        try {
            
            calificacionDTO.validarEntrada();
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            Integer idUsuario = autorizacion.validarSesion().getId();
            
            calificacionDTO.setIdUsuario(idUsuario.toString());
            
            servicio.cambiarCalificacion(calificacionDTO);
            
            return Response.ok().build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (EntidadInvalidaException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } 
    }
    
    @DELETE
    @Path("calificacion")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response eliminarCalificacion(@HeaderParam("Authorization") String authHeader, CalificacionSalaDTO calificacionDTO) {
        
        SalaServicio servicio = new SalaServicio();
        
        try {
            
            calificacionDTO.validarEliminacion();
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarEliminarCalificacion(calificacionDTO);
            
            servicio.eliminarCalificacion(calificacionDTO);
            
            return Response.ok().build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (EntidadInvalidaException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } 
    }
}
