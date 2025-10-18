/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.recursos;

import com.mynor.manejador.cines.api.dtos.CredencialesEntradaDTO;
import com.mynor.manejador.cines.api.dtos.UsuarioSalidaDTO;
import com.mynor.manejador.cines.api.seguridad.ManejadorJWT;
import com.mynor.manejador.cines.api.seguridad.TokenDTO;
import com.mynor.manejador.cines.api.servicios.AutenticacionServicio;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
            UsuarioSalidaDTO usuario = autenticacionServicio.iniciarSesion(credencialesDTO);
            ManejadorJWT jwt = new ManejadorJWT();
            TokenDTO token = jwt.generarToken(usuario);
            return Response.ok(token).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        }
    }
    
}
