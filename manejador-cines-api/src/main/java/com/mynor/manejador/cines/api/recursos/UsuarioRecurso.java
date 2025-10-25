/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.recursos;

import com.mynor.manejador.cines.api.dtos.ImagenEntradaDTO;
import com.mynor.manejador.cines.api.dtos.UsuarioEditadoDTO;
import com.mynor.manejador.cines.api.dtos.UsuarioEntradaDTO;
import com.mynor.manejador.cines.api.dtos.UsuarioSalidaDTO;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.excepciones.AutorizacionException;
import com.mynor.manejador.cines.api.excepciones.UsuarioInvalidoException;
import com.mynor.manejador.cines.api.seguridad.Autorizacion;
import com.mynor.manejador.cines.api.seguridad.CredencialesTokenDTO;
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
    public Response crearUsuario(@HeaderParam("Authorization") String authHeader, UsuarioEntradaDTO usuarioDTO) {
        
        UsuarioServicio usuarioServicio = new UsuarioServicio();
        
        try {
            
            usuarioDTO.validarEntrada();
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarCrearUsuario(usuarioDTO);
            
            usuarioServicio.crearUsuario(usuarioDTO);
            
            return Response.ok().build();
        } catch (AccesoDeDatosException | UsuarioInvalidoException | NoSuchAlgorithmException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } 
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verUsuario(@PathParam("id") String id, @HeaderParam("Authorization") String authHeader){
        UsuarioServicio usuarioServicio = new UsuarioServicio();
        
        try {
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarSesion();
            
            UsuarioSalidaDTO usuarioDTO = usuarioServicio.obtenerPorId(Integer.valueOf(id));
            return Response.ok(usuarioDTO).build();
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
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response verUsuarios(@HeaderParam("Authorization") String authHeader){
        UsuarioServicio usuarioServicio = new UsuarioServicio();
        
        try {
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarSesion();
            
            UsuarioSalidaDTO[] usuariosDTO = usuarioServicio.obtenerTodos();
            return Response.ok(usuariosDTO).build();
        } catch (UsuarioInvalidoException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (AccesoDeDatosException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editarUsuario(@HeaderParam("Authorization") String authHeader, UsuarioEditadoDTO usuarioDTO){
        UsuarioServicio usuarioServicio = new UsuarioServicio();
        
        try {
            usuarioDTO.validarEntrada();
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarEditarUsuario(usuarioDTO);
            
            usuarioServicio.editarUsuario(usuarioDTO);
            return Response.ok().build();
        } catch (UsuarioInvalidoException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (AccesoDeDatosException | NoSuchAlgorithmException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
    }
    
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editarImagenDeUsuario(@HeaderParam("Authorization") String authHeader, ImagenEntradaDTO imagenDTO){
        UsuarioServicio usuarioServicio = new UsuarioServicio();
        
        try {
            imagenDTO.validarEntrada();
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            
            CredencialesTokenDTO credenciales = autorizacion.validarSesion();
            
            
            usuarioServicio.editarImagenUsuario(credenciales, imagenDTO);
            
            return Response.ok().build();
        } catch (UsuarioInvalidoException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (AccesoDeDatosException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
    }
    
    @DELETE
    @Path("{id}")
    public Response desactivarUsuario(@PathParam("id") String id, @HeaderParam("Authorization") String authHeader){
        UsuarioServicio usuarioServicio = new UsuarioServicio();
        
        try {
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarDesactivarUsuario(Integer.valueOf(id));
            
            usuarioServicio.desactivarCuenta(Integer.valueOf(id));
            return Response.ok().build();
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
}
