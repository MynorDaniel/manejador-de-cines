/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.recursos;

import com.mynor.manejador.cines.api.dtos.ComentarioDTO;
import com.mynor.manejador.cines.api.dtos.ComentarioPeliculaDTO;
import com.mynor.manejador.cines.api.dtos.ComentarioSalaDTO;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.excepciones.AutorizacionException;
import com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException;
import com.mynor.manejador.cines.api.seguridad.Autorizacion;
import com.mynor.manejador.cines.api.servicios.ComentarioServicio;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

/**
 *
 * @author mynordma
 */
@Path("comentarios")
public class ComentarioRecurso {
    
    @POST
    @Path("sala")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crearComentarioSala(@HeaderParam("Authorization") String authHeader, ComentarioSalaDTO comentarioDTO) {
        
        ComentarioServicio servicio = new ComentarioServicio();
        
        try {
            
            comentarioDTO.validarEntrada();
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            Integer idUsuario = autorizacion.validarSesion().getId();
            
            comentarioDTO.getComentario().setIdUsuario(idUsuario.toString());
            
            servicio.crearComentarioSala(comentarioDTO);
            
            return Response.ok().build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (EntidadInvalidaException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } 
    }
    
    @POST
    @Path("pelicula")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crearComentarioPelicula(@HeaderParam("Authorization") String authHeader, ComentarioPeliculaDTO comentarioDTO) {
        
        ComentarioServicio servicio = new ComentarioServicio();
        
        try {
            
            comentarioDTO.validarEntrada();
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            Integer idUsuario = autorizacion.validarSesion().getId();
            
            comentarioDTO.getComentario().setIdUsuario(idUsuario.toString());
            
            servicio.crearComentarioPelicula(comentarioDTO);
            
            return Response.ok().build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (EntidadInvalidaException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } 
    }
    
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editarComentario(@HeaderParam("Authorization") String authHeader, ComentarioDTO comentarioDTO) {
        
        ComentarioServicio servicio = new ComentarioServicio();
        
        try {
            
            comentarioDTO.validarEntrada();
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarEdicionDeComentario(Integer.valueOf(comentarioDTO.getId()));
            
            servicio.editarComentario(comentarioDTO);
            
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
    @Path("{id}")
    public Response eliminarComentario(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id) {
        
        ComentarioServicio servicio = new ComentarioServicio();
        
        try {
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarEdicionDeComentario(Integer.valueOf(id));
            
            servicio.eliminarComentario(Integer.valueOf(id));
            
            return Response.ok().build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }catch (NumberFormatException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Id del comentario invalido").build();
        } 
    }
    
    @GET
    @Path("{id}")
    public Response verComentario(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id) {
        
        ComentarioServicio servicio = new ComentarioServicio();
        
        try {
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarSesion();
            
            ComentarioDTO comentario = servicio.verComentario(Integer.valueOf(id));
            
            return Response.ok(comentario).build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (NumberFormatException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Id del comentario invalido").build();
        } 
    }
    
    @GET
    @Path("sala/{id}")
    public Response verComentariosSala(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id) {
        
        ComentarioServicio servicio = new ComentarioServicio();
        
        try {
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarSesion();
            
            ComentarioSalaDTO[] comentarios = servicio.verComentariosPorSala(Integer.valueOf(id));
            
            return Response.ok(comentarios).build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (NumberFormatException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Id de la sala invalido").build();
        } 
    }
    
    @GET
    @Path("pelicula/{id}")
    public Response verComentariosPelicula(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id) {
        
        ComentarioServicio servicio = new ComentarioServicio();
        
        try {
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarSesion();
            
            ComentarioPeliculaDTO[] comentarios = servicio.verComentariosPorPelicula(Integer.valueOf(id));
            
            return Response.ok(comentarios).build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (NumberFormatException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Id de la pelicula invalido").build();
        } 
    }
}
