/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.servicios;

import com.mynor.manejador.cines.api.bd.BaseDeDatos;
import com.mynor.manejador.cines.api.bd.UsuarioBD;
import com.mynor.manejador.cines.api.dtos.CredencialesEntradaDTO;
import com.mynor.manejador.cines.api.dtos.UsuarioSalidaDTO;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.excepciones.UsuarioInvalidoException;
import com.mynor.manejador.cines.api.filtros.FiltrosUsuario;
import com.mynor.manejador.cines.api.modelo.Usuario;
import com.mynor.manejador.cines.api.seguridad.Cifrador;
import com.mynor.manejador.cines.api.seguridad.ManejadorJWT;
import com.mynor.manejador.cines.api.seguridad.TokenDTO;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 *
 * @author mynordma
 */
public class AutenticacionServicio {
    
    private final BaseDeDatos<Usuario, FiltrosUsuario> USUARIO_BD;
    
    public AutenticacionServicio(){
        USUARIO_BD = new UsuarioBD();
    }

    public TokenDTO iniciarSesion(CredencialesEntradaDTO credencialesDTO) throws AccesoDeDatosException, NoSuchAlgorithmException, UsuarioInvalidoException {
        // Obtener entidad usuario
        
        Cifrador cifrador = new Cifrador();
        credencialesDTO.setClave(cifrador.hashear(credencialesDTO.getClave()));
        
        FiltrosUsuario filtros = new FiltrosUsuario();
        filtros.setCorreo(Optional.ofNullable(credencialesDTO.getCorreo()));

        Usuario[] coincidencias = USUARIO_BD.leerCompleto(filtros);
        
        if(coincidencias.length < 1) throw new UsuarioInvalidoException("Usuario no encontrado");
        
        // Verificar credenciales
        
        Usuario usuario = coincidencias[0];
        
        if(!usuario.getClave().equals(credencialesDTO.getClave())) throw new UsuarioInvalidoException("Contraseña incorrecta");
        if(!usuario.getActivado()) throw new UsuarioInvalidoException("Usuario desactivado");
        
        // Crear token
        UsuarioSalidaDTO usuarioDTO = new UsuarioSalidaDTO();
        usuarioDTO.setActivado(usuario.getActivado());
        usuarioDTO.setCorreo(usuario.getCorreo());
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setImagen(usuario.getImagen());
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setRol(usuario.getRol());
        
        ManejadorJWT jwt = new ManejadorJWT();
        TokenDTO token = jwt.generarToken(usuarioDTO);
        
        return token;
    }
    
}