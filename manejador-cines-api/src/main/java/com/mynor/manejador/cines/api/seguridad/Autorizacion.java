/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.seguridad;

import com.mynor.manejador.cines.api.bd.BaseDeDatos;
import com.mynor.manejador.cines.api.bd.Transaccion;
import com.mynor.manejador.cines.api.bd.UsuarioBD;
import com.mynor.manejador.cines.api.dtos.AnuncioEntradaDTO;
import com.mynor.manejador.cines.api.dtos.CarteraEntradaDTO;
import com.mynor.manejador.cines.api.dtos.UsuarioEditadoDTO;
import com.mynor.manejador.cines.api.dtos.UsuarioEntradaDTO;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.excepciones.AutorizacionException;
import com.mynor.manejador.cines.api.filtros.FiltrosUsuario;
import com.mynor.manejador.cines.api.modelo.Rol;
import com.mynor.manejador.cines.api.modelo.Usuario;
import java.util.Optional;

/**
 *
 * @author mynordma
 */
public class Autorizacion {
    
    private final BaseDeDatos<Usuario, FiltrosUsuario> USUARIO_BD;
    private CredencialesTokenDTO credencialesUsuarioActual;
    private final ManejadorJWT MANEJADOR_JWT;
    private final String AUTH_HEADER;
    
    public Autorizacion(String authHeader) throws AutorizacionException{
        USUARIO_BD = new UsuarioBD();
        MANEJADOR_JWT = new ManejadorJWT();
        AUTH_HEADER = authHeader;
    }
    
    public Usuario obtenerUsuarioActual(CredencialesTokenDTO credenciales) throws AutorizacionException, AccesoDeDatosException {
        
        FiltrosUsuario filtros = new FiltrosUsuario();
        filtros.setId(Optional.ofNullable(credenciales.getId()));
        
        Usuario[] coincidencias;
        
        try (Transaccion transaccion = new Transaccion()) {
            coincidencias = USUARIO_BD.leer(filtros, transaccion.obtenerConexion());
            transaccion.commit();
        }

        if(coincidencias.length < 1) throw new AutorizacionException("Sesión no existe");
        
        return coincidencias[0];
    }

    public void validarCrearUsuario(UsuarioEntradaDTO usuarioDTO) throws AccesoDeDatosException, AutorizacionException {

        
        Rol rolNuevoUsuario = Rol.valueOf(usuarioDTO.getRol());
        
        if(rolNuevoUsuario == Rol.ANUNCIANTE || rolNuevoUsuario == Rol.CLIENTE) return;
        
        credencialesUsuarioActual = MANEJADOR_JWT.validarToken(AUTH_HEADER);
        System.out.println(credencialesUsuarioActual.toString());
        
        Usuario usuarioActual = obtenerUsuarioActual(credencialesUsuarioActual);
        
        Rol rolUsuarioActual = usuarioActual.getRol();
        
        if(rolNuevoUsuario == Rol.ADMINISTRADOR_CINES || rolNuevoUsuario == Rol.ADMINISTRADOR_SISTEMA){
            if(rolUsuarioActual != Rol.ADMINISTRADOR_SISTEMA) throw new AutorizacionException("Sin autorización");
        }
        
    }

    public CredencialesTokenDTO validarSesion() throws AutorizacionException, AccesoDeDatosException {
        return MANEJADOR_JWT.validarToken(AUTH_HEADER);
    }

    public void validarEditarUsuario(UsuarioEditadoDTO usuarioDTO) throws AutorizacionException, AccesoDeDatosException {
        credencialesUsuarioActual = MANEJADOR_JWT.validarToken(AUTH_HEADER);
        Usuario usuarioActual = obtenerUsuarioActual(credencialesUsuarioActual);
        
        if(!usuarioActual.getId().equals(Integer.valueOf(usuarioDTO.getId()))) throw new AutorizacionException("Sin autorización");
    }

    public void validarDesactivarUsuario(Integer id) throws AutorizacionException, AccesoDeDatosException {
        credencialesUsuarioActual = MANEJADOR_JWT.validarToken(AUTH_HEADER);
        Usuario usuarioActual = obtenerUsuarioActual(credencialesUsuarioActual);
        
        if(!usuarioActual.getId().equals(id)) throw new AutorizacionException("Sin autorización");
    }

    public void validarEditarCartera(CarteraEntradaDTO carteraDTO) throws AutorizacionException, AccesoDeDatosException {
        credencialesUsuarioActual = MANEJADOR_JWT.validarToken(AUTH_HEADER);
        Usuario usuarioActual = obtenerUsuarioActual(credencialesUsuarioActual);
        
        if(!usuarioActual.getId().equals(Integer.valueOf(carteraDTO.getUsuarioId()))) throw new AutorizacionException("Sin autorización");
    }

    /**
     * El usuario debe ser un anunciante
     * El usuario del pago debe corresponder al usuario actual
     * @param anuncioDTO
     * @throws AutorizacionException
     * @throws AccesoDeDatosException 
     */
    public void validarComprarAnuncio(AnuncioEntradaDTO anuncioDTO) throws AutorizacionException, AccesoDeDatosException {
        credencialesUsuarioActual = MANEJADOR_JWT.validarToken(AUTH_HEADER);
        Usuario usuarioActual = obtenerUsuarioActual(credencialesUsuarioActual);
        
        boolean coincideId = usuarioActual.getId().equals(Integer.valueOf(anuncioDTO.getPago().getIdUsuario()));
        boolean esAnunciante = usuarioActual.getRol() == Rol.ANUNCIANTE;
        
        if(!coincideId || !esAnunciante) throw new AutorizacionException("Sin autorización para crear este anuncio");
    }

    
    
}
