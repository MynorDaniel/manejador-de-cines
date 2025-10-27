/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.recursos;

import com.mynor.manejador.cines.api.dtos.CarteraDTO;
import com.mynor.manejador.cines.api.dtos.CarteraEntradaDTO;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.excepciones.AutorizacionException;
import com.mynor.manejador.cines.api.excepciones.UsuarioInvalidoException;
import com.mynor.manejador.cines.api.seguridad.Autorizacion;
import com.mynor.manejador.cines.api.seguridad.CredencialesTokenDTO;
import com.mynor.manejador.cines.api.servicios.CarteraServicio;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

/**
 *
 * @author mynordma
 */
@Path("cartera")
public class CarteraRecurso {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response verCartera(@HeaderParam("Authorization") String authHeader){
        CarteraServicio carteraServicio = new CarteraServicio();
        
        try {
            Autorizacion autorizacion = new Autorizacion(authHeader);
            CredencialesTokenDTO credenciales = autorizacion.validarSesion();
            
            CarteraDTO carteraDTO = carteraServicio.obtenerPorId(credenciales.getId());
            return Response.ok(carteraDTO).build();
        } catch (UsuarioInvalidoException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (AccesoDeDatosException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        } catch (NumberFormatException e){
            return Response.status(Response.Status.NOT_FOUND).entity("Id inválido").build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editarCartera(@HeaderParam("Authorization") String authHeader, CarteraEntradaDTO carteraDTO){
        CarteraServicio carteraServicio = new CarteraServicio();
        
        try {
            carteraDTO.validarEntrada();
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarEditarCartera(carteraDTO);
            
            carteraServicio.editarCartera(carteraDTO);
            return Response.ok().build();
        } catch (UsuarioInvalidoException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (AccesoDeDatosException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
    }
}
