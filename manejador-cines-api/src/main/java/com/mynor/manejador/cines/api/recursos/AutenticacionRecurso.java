/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.recursos;

import com.mynor.manejador.cines.api.dtos.CredencialesEntradaDTO;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.excepciones.UsuarioInvalidoException;
import com.mynor.manejador.cines.api.seguridad.TokenDTO;
import com.mynor.manejador.cines.api.servicios.AutenticacionServicio;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author mynordma
 */

@Path("auth")
public class AutenticacionRecurso {
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response iniciarSesion(CredencialesEntradaDTO credencialesDTO) {
        
        AutenticacionServicio autenticacionServicio = new AutenticacionServicio();
        
        try {
            credencialesDTO.validarEntrada();
            TokenDTO token = autenticacionServicio.iniciarSesion(credencialesDTO);
            return Response.ok(token).build();
        } catch (AccesoDeDatosException | NoSuchAlgorithmException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (UsuarioInvalidoException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
    }
    
}