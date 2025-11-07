/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.recursos;

import com.mynor.manejador.cines.api.dtos.BoletoDTO;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.excepciones.AutorizacionException;
import com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException;
import com.mynor.manejador.cines.api.seguridad.Autorizacion;
import com.mynor.manejador.cines.api.servicios.BoletoServicio;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

/**
 *
 * @author mynordma
 */
@Path("boletos")
public class BoletoRecurso {
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crearBoleto(@HeaderParam("Authorization") String authHeader, BoletoDTO boletoDTO) {
        
        BoletoServicio servicio = new BoletoServicio();
        
        try {
            
            boletoDTO.validarEntrada();
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            Integer idUsuario = autorizacion.validarSesion().getId();
            
            boletoDTO.setIdUsuario(idUsuario.toString());
            boletoDTO.getPagoDTO().setIdUsuario(idUsuario.toString());
            
            servicio.comprarBoleto(boletoDTO);
            
            return Response.ok().build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (NumberFormatException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Proyeccion invalida").build();
        } catch (EntidadInvalidaException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } 
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response verBoletosPorUsuario(@HeaderParam("Authorization") String authHeader) {
        
        BoletoServicio servicio = new BoletoServicio();
        
        try {
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            Integer idUsuario = autorizacion.validarSesion().getId();
            
            BoletoDTO[] boletos = servicio.verBoletosPorUsuario(idUsuario);
            
            return Response.ok(boletos).build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
    }
}
