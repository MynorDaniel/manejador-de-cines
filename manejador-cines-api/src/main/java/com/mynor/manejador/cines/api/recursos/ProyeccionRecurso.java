/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.recursos;

import com.mynor.manejador.cines.api.dtos.ProyeccionDTO;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.excepciones.AutorizacionException;
import com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException;
import com.mynor.manejador.cines.api.seguridad.Autorizacion;
import com.mynor.manejador.cines.api.servicios.ProyeccionServicio;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

/**
 *
 * @author mynordma
 */
@Path("proyecciones")
public class ProyeccionRecurso {
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crearProyeccion(@HeaderParam("Authorization") String authHeader, ProyeccionDTO proyeccionDTO) {
        
        ProyeccionServicio servicio = new ProyeccionServicio();
        
        try {
            
            proyeccionDTO.validarEntrada();
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarCrearProyeccion(proyeccionDTO);
            
            servicio.crearProyeccion(proyeccionDTO);
            
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
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editarProyeccion(@HeaderParam("Authorization") String authHeader, ProyeccionDTO proyeccionDTO) {
        
        ProyeccionServicio servicio = new ProyeccionServicio();
        
        try {
            
            proyeccionDTO.validarEdicion();
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarCrearProyeccion(proyeccionDTO);
            
            servicio.editarProyeccion(proyeccionDTO);
            
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
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response verProyeccion(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id) {
        
        ProyeccionServicio servicio = new ProyeccionServicio();
        
        try {
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarSesion();
            
            ProyeccionDTO proyeccion = servicio.verPorId(Integer.valueOf(id));
            
            return Response.ok(proyeccion).build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (NumberFormatException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Id incorrecto").build();
        }
    }
}
