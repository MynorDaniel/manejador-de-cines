/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.recursos;

import com.mynor.manejador.cines.api.dtos.BloqueoAnunciosCineDTO;
import com.mynor.manejador.cines.api.dtos.CineDTO;
import com.mynor.manejador.cines.api.dtos.CostoDiarioCineDTO;
import com.mynor.manejador.cines.api.dtos.CostoGlobalCinesDTO;
import com.mynor.manejador.cines.api.dtos.PagoSalidaDTO;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.excepciones.AutorizacionException;
import com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException;
import com.mynor.manejador.cines.api.seguridad.Autorizacion;
import com.mynor.manejador.cines.api.servicios.CineServicio;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

/**
 *
 * @author mynordma
 */
@Path("cines")
public class CineRecurso {
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crearCine(@HeaderParam("Authorization") String authHeader, CineDTO cineDTO) {
        
        CineServicio servicio = new CineServicio();
        
        try {
            
            cineDTO.validarEntrada();
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarCrearCine(cineDTO);
            
            servicio.crearCine(cineDTO);
            
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response verCines(@HeaderParam("Authorization") String authHeader) {
        
        CineServicio servicio = new CineServicio();
        
        try {
                        
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarSesion();
            
            CineDTO[] cines = servicio.verCines();
            
            return Response.ok(cines).build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verCine(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id) {
        
        CineServicio servicio = new CineServicio();
        
        try {
                        
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarSesion();
            
            CineDTO cine = servicio.verCine(Integer.valueOf(id));
            
            return Response.ok(cine).build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (NumberFormatException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } 
    }
    
    @GET
    @Path("usuario/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verCinesPorUsuario(@HeaderParam("Authorization") String authHeader, @PathParam("id") String id) {
        
        CineServicio servicio = new CineServicio();
        
        try {
                        
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarSesion();
            
            CineDTO[] cines = servicio.verCinesPorUsuario(Integer.valueOf(id));
            
            return Response.ok(cines).build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (NumberFormatException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } 
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editarCine(@HeaderParam("Authorization") String authHeader, CineDTO cineDTO) {
        
        CineServicio servicio = new CineServicio();
        
        try {
            
            cineDTO.validarEdicion();
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarEditarCine(cineDTO);
            
            servicio.editarCine(cineDTO);
            
            return Response.ok().build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (EntidadInvalidaException e){
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
    
    @POST
    @Path("bloqueo-anuncios")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response bloquearAnunciosEnCine(@HeaderParam("Authorization") String authHeader, BloqueoAnunciosCineDTO bloqueoDTO) {
        
        CineServicio servicio = new CineServicio();
        
        try {
            
            bloqueoDTO.validarEntrada();
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarBloqueoAnuncios(bloqueoDTO);
            
            servicio.crearBloqueoDeAnuncios(bloqueoDTO);
            
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
    @Path("bloqueo-anuncios/{idCine}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verBloqueoDeAnunciosEnCine(@HeaderParam("Authorization") String authHeader, @PathParam("idCine")  String idCine) {
        
        CineServicio servicio = new CineServicio();
        
        try {
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarSesion();
            
            BloqueoAnunciosCineDTO ultimoBloqueo = servicio.verUltimoBloqueoDeAnuncios(Integer.valueOf(idCine));
            
            return Response.ok(ultimoBloqueo).build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
    }
    
    @POST
    @Path("costo-diario")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crearCostoDiario(@HeaderParam("Authorization") String authHeader, CostoDiarioCineDTO costoDTO) {
        
        CineServicio servicio = new CineServicio();
        
        try {
            
            costoDTO.validarEntrada();
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarAdminSistema();
            
            servicio.crearCostoDiarioACine(costoDTO);
            
            return Response.ok().build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (EntidadInvalidaException e){
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
    
    @PUT
    @Path("costo-global")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response cambiarCostoGlobal(@HeaderParam("Authorization") String authHeader, CostoGlobalCinesDTO costoGlobalDTO) {
        
        CineServicio servicio = new CineServicio();
        
        try {
            
            costoGlobalDTO.validarEntrada();
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarAdminSistema();
            
            servicio.crearCostoGlobalDiario(costoGlobalDTO);
            
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
    @Path("costo-global")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verCostoGlobal(@HeaderParam("Authorization") String authHeader) {
        
        CineServicio servicio = new CineServicio();
        
        try {
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarAdminSistema();
            
            CostoGlobalCinesDTO costo = servicio.verCostoGlobalDiario();
            
            return Response.ok(costo).build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
    }
    
    @POST
    @Path("pago/{idCine}")
    public Response pagarCostoDiario(@HeaderParam("Authorization") String authHeader, @PathParam("idCine") String idCine) {
        
        CineServicio servicio = new CineServicio();
        
        try {
                        
            Autorizacion autorizacion = new Autorizacion(authHeader);
            Integer idUsuario = autorizacion.validarPagoCine(Integer.valueOf(idCine));
            
            servicio.pagarCostoDiarioCine(idUsuario, Integer.valueOf(idCine));
            
            return Response.ok().build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (NumberFormatException e){
            return Response.status(Response.Status.NOT_FOUND).entity("ID inválido").build();
        } catch (EntidadInvalidaException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
        }
    }
    
    @GET
    @Path("deuda/{idCine}")
    public Response verDeudaReal(@HeaderParam("Authorization") String authHeader, @PathParam("idCine") String idCine) {
        
        CineServicio servicio = new CineServicio();
        
        try {
                        
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarCreadorDeCine(Integer.valueOf(idCine));
            
            PagoSalidaDTO deuda = servicio.verDeuda(Integer.valueOf(idCine));
            
            return Response.ok(deuda).build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (NumberFormatException e){
            return Response.status(Response.Status.NOT_FOUND).entity("ID inválido").build();
        }
    }
}