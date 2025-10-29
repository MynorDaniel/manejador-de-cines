/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.recursos;

import com.mynor.manejador.cines.api.dtos.AnuncioEntradaDTO;
import com.mynor.manejador.cines.api.dtos.TipoAnuncioPrecioDTO;
import com.mynor.manejador.cines.api.dtos.VigenciaAnuncioDTO;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.excepciones.AutorizacionException;
import com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException;
import com.mynor.manejador.cines.api.seguridad.Autorizacion;
import com.mynor.manejador.cines.api.servicios.AnuncioServicio;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

/**
 *
 * @author mynordma
 */
@Path("anuncios")
public class AnuncioRecurso {
    
    /**
     * Recibe el anuncio a crear y lo valida
     * Autoriza la creacion
     * Llama al servicio para crear el anuncio
     * Devuelve un Response dependiendo de la respuesta del servicio
     * @param authHeader
     * @param anuncioDTO
     * @return 
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crearAnuncio(@HeaderParam("Authorization") String authHeader, AnuncioEntradaDTO anuncioDTO) {
        
        AnuncioServicio anuncioServicio = new AnuncioServicio();
        
        try {
            
            anuncioDTO.validarEntrada();
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarComprarAnuncio(anuncioDTO);
            
            anuncioServicio.comprarAnuncio(anuncioDTO);
            
            return Response.ok().build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (EntidadInvalidaException e){
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
    
    @GET
    @Path("precios/tipos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerPrecioTipos(@HeaderParam("Authorization") String authHeader){
        AnuncioServicio anuncioServicio = new AnuncioServicio();
        
        try {
                        
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarSesion();
            
            TipoAnuncioPrecioDTO[] preciosTipos = anuncioServicio.obtenerPreciosDeTipos();
            
            return Response.ok(preciosTipos).build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } 
    }
    
    @GET
    @Path("precios/vigencias")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerPrecioVigencias(@HeaderParam("Authorization") String authHeader){
        AnuncioServicio anuncioServicio = new AnuncioServicio();
        
        try {
                        
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarSesion();
            
            VigenciaAnuncioDTO[] preciosDias = anuncioServicio.obtenerPreciosDeVigencias();
            
            return Response.ok(preciosDias).build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
    }
}
