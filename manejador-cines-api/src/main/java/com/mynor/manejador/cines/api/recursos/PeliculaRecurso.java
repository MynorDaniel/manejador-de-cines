/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.recursos;

import com.mynor.manejador.cines.api.dtos.CalificacionDTO;
import com.mynor.manejador.cines.api.dtos.CategoriaDTO;
import com.mynor.manejador.cines.api.dtos.ClasificacionDTO;
import com.mynor.manejador.cines.api.dtos.FiltrosPeliculasDTO;
import com.mynor.manejador.cines.api.dtos.PeliculaDTO;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.excepciones.AutorizacionException;
import com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException;
import com.mynor.manejador.cines.api.seguridad.Autorizacion;
import com.mynor.manejador.cines.api.servicios.PeliculaServicio;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

/**
 *
 * @author mynordma
 */
@Path("peliculas")
public class PeliculaRecurso {
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crearPelicula(@HeaderParam("Authorization") String authHeader, PeliculaDTO peliculaDTO) {
        
        PeliculaServicio servicio = new PeliculaServicio();
        
        try {
            
            peliculaDTO.validarEntrada();
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarAdminSistema();
            
            servicio.crearPelicula(peliculaDTO);
            
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
    public Response verPeliculas(
        @HeaderParam("Authorization") String authHeader,
        @QueryParam("id") String id,
        @QueryParam("titulo") String titulo,
        @QueryParam("idCategoria") String idCategoria
    ) {

        PeliculaServicio servicio = new PeliculaServicio();

        try {
            Autorizacion autorizacion = new Autorizacion(authHeader);
            Integer idUsuario = autorizacion.validarSesion().getId();

            FiltrosPeliculasDTO filtros = new FiltrosPeliculasDTO();
            filtros.setId(id);
            filtros.setTitulo(titulo);
            filtros.setIdCategoria(idCategoria);

            PeliculaDTO[] peliculas = servicio.verPeliculas(idUsuario, filtros);

            return Response.ok(peliculas).build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verPelicula(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id) {
        
        PeliculaServicio servicio = new PeliculaServicio();
        
        try {
            Autorizacion autorizacion = new Autorizacion(authHeader);
            Integer idUsuario = autorizacion.validarSesion().getId();
            
            PeliculaDTO pelicula = servicio.verPelicula(idUsuario, id);
            
            return Response.ok(pelicula).build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editarPelicula(@HeaderParam("Authorization") String authHeader, PeliculaDTO peliculaDTO) {
        
        PeliculaServicio servicio = new PeliculaServicio();
        
        try {
            
            peliculaDTO.validarEdicion();
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarAdminSistema();
            
            servicio.editarPelicula(peliculaDTO);
            
            return Response.ok().build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (EntidadInvalidaException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } 
    }
    
    /*@DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response eliminarPelicula(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id) {
    
    PeliculaServicio servicio = new PeliculaServicio();
    
    try {
    Autorizacion autorizacion = new Autorizacion(authHeader);
    autorizacion.validarAdminSistema();
    
    servicio.eliminarPelicula(Integer.valueOf(id));
    
    return Response.ok().build();
    } catch (AccesoDeDatosException e) {
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    } catch (AutorizacionException ex) {
    return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
    } catch (NumberFormatException ex) {
    return Response.status(Response.Status.BAD_REQUEST).entity("Id inválido").build();
    }
    }*/
    
    @POST
    @Path("calificacion/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response calificarPelicula(@HeaderParam("Authorization") String authHeader, CalificacionDTO calificacionDTO, @PathParam("id") String id) {
        
        PeliculaServicio servicio = new PeliculaServicio();
        
        try {
            
            calificacionDTO.validarEntrada();
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            Integer idUsuario = autorizacion.validarSesion().getId();
            
            servicio.cambiarCalificacion(calificacionDTO, id, idUsuario);
            
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
    @Path("clasificaciones")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verClasificaciones(@HeaderParam("Authorization") String authHeader) {
        
        PeliculaServicio servicio = new PeliculaServicio();
        
        try {
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarSesion();
            
            ClasificacionDTO[] clasificaciones = servicio.verClasificaciones();
            
            return Response.ok(clasificaciones).build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
    }
    
    @GET
    @Path("categorias")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verCategorias(@HeaderParam("Authorization") String authHeader) {
        
        PeliculaServicio servicio = new PeliculaServicio();
        
        try {
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarSesion();
            
            CategoriaDTO[] categorias = servicio.verCategorias();
            
            return Response.ok(categorias).build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
    }
}
