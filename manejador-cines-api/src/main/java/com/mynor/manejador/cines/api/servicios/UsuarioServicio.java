/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.servicios;

import com.mynor.manejador.cines.api.bd.BaseDeDatos;
import com.mynor.manejador.cines.api.bd.Transaccion;
import com.mynor.manejador.cines.api.bd.UsuarioBD;
import com.mynor.manejador.cines.api.dtos.ImagenEntradaDTO;
import com.mynor.manejador.cines.api.dtos.UsuarioEditadoDTO;
import com.mynor.manejador.cines.api.dtos.UsuarioEntradaDTO;
import com.mynor.manejador.cines.api.dtos.UsuarioSalidaDTO;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.excepciones.UsuarioInvalidoException;
import com.mynor.manejador.cines.api.filtros.FiltrosUsuario;
import com.mynor.manejador.cines.api.modelo.Imagen;
import com.mynor.manejador.cines.api.modelo.Rol;
import com.mynor.manejador.cines.api.modelo.Usuario;
import com.mynor.manejador.cines.api.seguridad.Cifrador;
import com.mynor.manejador.cines.api.seguridad.CredencialesTokenDTO;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

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
        
        validarCorreoExistente(usuarioDTO.getCorreo());
        
        // Crear
        Cifrador cifrador = new Cifrador();
        usuarioDTO.setClave(cifrador.hashear(usuarioDTO.getClave()));
        
        Usuario usuario = new Usuario();
        usuario.setActivado(Boolean.TRUE);
        usuario.setClave(usuarioDTO.getClave());
        usuario.setCorreo(usuarioDTO.getCorreo());
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setRol(Rol.valueOf(usuarioDTO.getRol()));
                
        try(Transaccion transaccion = new Transaccion()){
            USUARIO_BD.crear(usuario, transaccion.obtenerConexion());
            transaccion.commit();
        }
    }

    public UsuarioSalidaDTO obtenerPorId(Integer id) throws AccesoDeDatosException, UsuarioInvalidoException {
                
        Usuario usuario = leerPorId(id);
        
        if(!usuario.getActivado()) throw new UsuarioInvalidoException("Usuario desactivado");
        
        // Mapear a DTO
        
        UsuarioSalidaDTO usuarioDTO = new UsuarioSalidaDTO();
        usuarioDTO.setActivado(usuario.getActivado());
        usuarioDTO.setCorreo(usuario.getCorreo());
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setImagen(usuario.getImagen());
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setRol(usuario.getRol());
        
        return usuarioDTO;
    }

    public UsuarioSalidaDTO[] obtenerTodos() throws AccesoDeDatosException, UsuarioInvalidoException {
        FiltrosUsuario filtros = new FiltrosUsuario();

        Usuario[] coincidencias;
        
        try (Transaccion transaccion = new Transaccion()) {
            coincidencias = USUARIO_BD.leer(filtros, transaccion.obtenerConexion());
            transaccion.commit();
        }
        
        if(coincidencias.length < 1) throw new UsuarioInvalidoException("Sin coincidencias");
        
        // Mapear a DTO
        
        UsuarioSalidaDTO[] usuariosDTO = new UsuarioSalidaDTO[coincidencias.length];
        
        for (int i = 0; i < coincidencias.length; i++) {
            
            Usuario coincidencia = coincidencias[i];
            UsuarioSalidaDTO usuarioDTO = new UsuarioSalidaDTO();
            usuarioDTO.setActivado(coincidencia.getActivado());
            usuarioDTO.setCorreo(coincidencia.getCorreo());
            usuarioDTO.setId(coincidencia.getId());
            usuarioDTO.setImagen(coincidencia.getImagen());
            usuarioDTO.setNombre(coincidencia.getNombre());
            usuarioDTO.setRol(coincidencia.getRol());
            
            usuariosDTO[i] = usuarioDTO;
            
        }
        
        return usuariosDTO;
    }

    public void editarUsuario(UsuarioEditadoDTO usuarioDTO) throws AccesoDeDatosException, UsuarioInvalidoException, NoSuchAlgorithmException {
        Usuario usuario = leerPorId(Integer.valueOf(usuarioDTO.getId()));
        
        // Si el correo cambia, validar que no exista
        if(!usuario.getCorreo().equals(usuarioDTO.getCorreo())){
            validarCorreoExistente(usuarioDTO.getCorreo());
        }
        
        // Cifrar la clave si no es null
        if(!StringUtils.isBlank(usuarioDTO.getClave())){
            Cifrador c = new Cifrador();
            usuarioDTO.setClave(c.hashear(usuarioDTO.getClave()));
            usuario.setClave(usuarioDTO.getClave());
        }
        
        // Actualizar
        usuario.setCorreo(usuarioDTO.getCorreo());
        usuario.setNombre(usuarioDTO.getNombre());
        
        try(Transaccion t = new Transaccion()){
            USUARIO_BD.actualizar(usuario, t.obtenerConexion());
            t.commit();
        }
    }
    
    protected Usuario leerPorId (Integer id) throws AccesoDeDatosException, UsuarioInvalidoException {
        FiltrosUsuario filtros = new FiltrosUsuario();
        filtros.setId(Optional.ofNullable(id));

        Usuario[] coincidencias;
        
        try (Transaccion transaccion = new Transaccion()) {
            coincidencias = USUARIO_BD.leerCompleto(filtros, transaccion.obtenerConexion());
            transaccion.commit();
        }
        
        if(coincidencias.length < 1) throw new UsuarioInvalidoException("Usuario no encontrado");
        
        Usuario usuario = coincidencias[0];
        
        return usuario;
                
    } 
    
    private void validarCorreoExistente(String correo) throws AccesoDeDatosException, UsuarioInvalidoException{
        FiltrosUsuario filtros = new FiltrosUsuario();
        filtros.setCorreo(Optional.ofNullable(correo));

        Usuario[] coincidencias;
        
        try(Transaccion transaccion = new Transaccion()){
            coincidencias = USUARIO_BD.leer(filtros, transaccion.obtenerConexion());
            transaccion.commit();
        }
        
        if(coincidencias.length > 0) throw new UsuarioInvalidoException("Correo en uso");
    }
    
    public void editarImagenUsuario(CredencialesTokenDTO credenciales, ImagenEntradaDTO imagenDTO) throws AccesoDeDatosException, UsuarioInvalidoException{
        //Obtener usuario
        Usuario usuario = leerPorId(credenciales.getId());
        
        //Editar la imagen
        Imagen imagen = new Imagen();
        imagen.setId(Integer.valueOf(imagenDTO.getId()));
        
        usuario.setImagen(imagen);
        
        try(Transaccion t = new Transaccion()){
            USUARIO_BD.actualizar(usuario, t.obtenerConexion());
            t.commit();
        }
    }

    public void desactivarCuenta(Integer id) throws AccesoDeDatosException, UsuarioInvalidoException {
        if(id.equals(1)) throw new AccesoDeDatosException("No se puede desactivar este usuario");
        Usuario usuario = leerPorId(id);
        try(Transaccion t = new Transaccion()){
            USUARIO_BD.eliminar(usuario, t.obtenerConexion());
            t.commit();
        }
    }
    
}