/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.servicios;

import com.mynor.manejador.cines.api.bd.BaseDeDatos;
import com.mynor.manejador.cines.api.bd.UsuarioBD;
import com.mynor.manejador.cines.api.dtos.UsuarioEntradaDTO;
import com.mynor.manejador.cines.api.dtos.UsuarioSalidaDTO;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.excepciones.UsuarioInvalidoException;
import com.mynor.manejador.cines.api.filtros.FiltrosUsuario;
import com.mynor.manejador.cines.api.modelo.Imagen;
import com.mynor.manejador.cines.api.modelo.Rol;
import com.mynor.manejador.cines.api.modelo.Usuario;
import com.mynor.manejador.cines.api.seguridad.Cifrador;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 *
 * @author mynordma
 */
public class UsuarioServicio {
    
    private final BaseDeDatos<Usuario, FiltrosUsuario> USUARIO_BD;
    
    public UsuarioServicio(){
        USUARIO_BD = new UsuarioBD();
    }

    public void crearUsuario(UsuarioEntradaDTO usuarioDTO) throws AccesoDeDatosException, NoSuchAlgorithmException, UsuarioInvalidoException {
        // Verificar que el correo no exista
        
        FiltrosUsuario filtros = new FiltrosUsuario();
        filtros.setCorreo(Optional.ofNullable(usuarioDTO.getCorreo()));

        Usuario[] coincidencias = USUARIO_BD.leer(filtros);
        
        if(coincidencias.length > 0) throw new UsuarioInvalidoException("Correo en uso");
        
        // Crear
        Cifrador cifrador = new Cifrador();
        usuarioDTO.setClave(cifrador.hashear(usuarioDTO.getClave()));
        
        Usuario usuario = new Usuario();
        usuario.setActivado(Boolean.TRUE);
        usuario.setClave(usuarioDTO.getClave());
        usuario.setCorreo(usuarioDTO.getCorreo());
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setRol(Rol.valueOf(usuarioDTO.getRol()));
        
        USUARIO_BD.crear(usuario);
    }
    
}
