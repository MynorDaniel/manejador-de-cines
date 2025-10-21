/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.recursos;

import com.mynor.manejador.cines.api.dtos.UsuarioEntradaDTO;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.excepciones.UsuarioInvalidoException;
import com.mynor.manejador.cines.api.servicios.UsuarioServicio;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author mynordma
 */

@Path("usuarios")
public class UsuarioRecurso {
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crearUsuario(UsuarioEntradaDTO usuarioDTO) {
        
        UsuarioServicio usuarioServicio = new UsuarioServicio();
        
        try {
            usuarioDTO.validarEntrada();
            usuarioServicio.crearUsuario(usuarioDTO);
            return Response.ok().build();
        } catch (AccesoDeDatosException | UsuarioInvalidoException | NoSuchAlgorithmException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } 
    }
}
