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
import com.mynor.manejador.cines.api.filtros.FiltrosUsuario;
import com.mynor.manejador.cines.api.modelo.Usuario;

/**
 *
 * @author mynordma
 */
public class AutenticacionServicio {
    
    private final BaseDeDatos<Usuario, FiltrosUsuario> USUARIO_BD;
    
    public AutenticacionServicio(){
        USUARIO_BD = new UsuarioBD();
    }

    public UsuarioSalidaDTO iniciarSesion(CredencialesEntradaDTO credencialesDTO) throws AccesoDeDatosException {
        // Obtener entidad usuario
        FiltrosUsuario filtros = new FiltrosUsuario();
        filtros.setCorreo(credencialesDTO.getCorreo());
        filtros.setClave(credencialesDTO.getClave());
        Usuario usuario = USUARIO_BD.leerCompleto(filtros)[0];
        
        // Crear token
        UsuarioSalidaDTO usuarioDTO = new UsuarioSalidaDTO();
        
        return usuarioDTO;
    }
    
}
