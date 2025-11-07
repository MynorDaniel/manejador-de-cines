/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.seguridad;

import com.mynor.manejador.cines.api.bd.BaseDeDatos;
import com.mynor.manejador.cines.api.bd.Transaccion;
import com.mynor.manejador.cines.api.bd.UsuarioBD;
import com.mynor.manejador.cines.api.dtos.AnuncioEntradaDTO;
import com.mynor.manejador.cines.api.dtos.BloqueoAnunciosCineDTO;
import com.mynor.manejador.cines.api.dtos.CalificacionSalaDTO;
import com.mynor.manejador.cines.api.dtos.CarteraEntradaDTO;
import com.mynor.manejador.cines.api.dtos.CineDTO;
import com.mynor.manejador.cines.api.dtos.SalaDTO;
import com.mynor.manejador.cines.api.dtos.UsuarioEditadoDTO;
import com.mynor.manejador.cines.api.dtos.UsuarioEntradaDTO;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.excepciones.AutorizacionException;
import com.mynor.manejador.cines.api.filtros.FiltrosUsuario;
import com.mynor.manejador.cines.api.modelo.Anuncio;
import com.mynor.manejador.cines.api.modelo.Cine;
import com.mynor.manejador.cines.api.modelo.Rol;
import com.mynor.manejador.cines.api.modelo.Usuario;
import com.mynor.manejador.cines.api.servicios.AnuncioServicio;
import com.mynor.manejador.cines.api.servicios.CineServicio;
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

    public void validarEditarAnuncio(AnuncioEntradaDTO anuncioDTO) throws AutorizacionException, AccesoDeDatosException {
        credencialesUsuarioActual = MANEJADOR_JWT.validarToken(AUTH_HEADER);
        Usuario usuarioActual = obtenerUsuarioActual(credencialesUsuarioActual);
        
        AnuncioServicio anuncioServicio = new AnuncioServicio();
        Anuncio anuncio = anuncioServicio.leerPorId(Integer.valueOf(anuncioDTO.getId()));
        
        if(usuarioActual.getRol() != Rol.ADMINISTRADOR_SISTEMA){
            if(!usuarioActual.getId().equals(anuncio.getPago().getUsuario().getId())) throw new AutorizacionException("Sin autorización para editar este anuncio");
        }
    }

    public void validarAdminSistema() throws AutorizacionException, AccesoDeDatosException {
        credencialesUsuarioActual = MANEJADOR_JWT.validarToken(AUTH_HEADER);
        Usuario usuarioActual = obtenerUsuarioActual(credencialesUsuarioActual);
        
        boolean esAdminSistema = usuarioActual.getRol() == Rol.ADMINISTRADOR_SISTEMA;
        
        if(!esAdminSistema) throw new AutorizacionException("Sin autorización");
    }

    public void validarCrearCine(CineDTO cineDTO) throws AutorizacionException, AccesoDeDatosException {
        credencialesUsuarioActual = MANEJADOR_JWT.validarToken(AUTH_HEADER);
        Usuario usuarioActual = obtenerUsuarioActual(credencialesUsuarioActual);
        
        Rol rolUsuarioActual = usuarioActual.getRol();
        
        if(rolUsuarioActual != Rol.ADMINISTRADOR_CINES) throw new AutorizacionException("Sin autorización para crear cines");
        
        cineDTO.setIdUsuarioCreador(String.valueOf(usuarioActual.getId()));
        
    }

    public void validarEditarCine(CineDTO cineDTO) throws AutorizacionException {
        credencialesUsuarioActual = MANEJADOR_JWT.validarToken(AUTH_HEADER);
        
        if(!credencialesUsuarioActual.getId().equals(Integer.valueOf(cineDTO.getIdUsuarioCreador()))) throw new AutorizacionException("Sin autorización para editar este cine");
    }

    public void validarBloqueoAnuncios(BloqueoAnunciosCineDTO bloqueoDTO) throws AccesoDeDatosException, AutorizacionException {
        validarCreadorDeCine(Integer.valueOf(bloqueoDTO.getIdCine()));
    }

    public Integer validarPagoCine(Integer idCine) throws AutorizacionException, AccesoDeDatosException {
        return validarCreadorDeCine(idCine);
    }
    
    public Integer validarCreadorDeCine(Integer idCine) throws AutorizacionException, AccesoDeDatosException{
        credencialesUsuarioActual = MANEJADOR_JWT.validarToken(AUTH_HEADER);
        
        CineServicio servicio = new CineServicio();
        Cine cine = servicio.leerCinePorId(idCine);
        
        if(!cine.getUsuarioCreador().getId().equals(credencialesUsuarioActual.getId())) throw new AutorizacionException("No puedes modificar este cine");
        
        return credencialesUsuarioActual.getId();
    }

    public void validarAdminCines() throws AutorizacionException, AccesoDeDatosException {
        credencialesUsuarioActual = MANEJADOR_JWT.validarToken(AUTH_HEADER);
        Usuario usuarioActual = obtenerUsuarioActual(credencialesUsuarioActual);
        
        boolean esAdminCines = usuarioActual.getRol() == Rol.ADMINISTRADOR_CINES;
        
        if(!esAdminCines) throw new AutorizacionException("Sin autorización");
    }

    public void validarEdicionDeSala(SalaDTO salaDTO) throws AutorizacionException, AccesoDeDatosException {
        credencialesUsuarioActual = MANEJADOR_JWT.validarToken(AUTH_HEADER);
        
        CineServicio cineServicio = new CineServicio();
        Cine cine = cineServicio.leerCinePorId(Integer.valueOf(salaDTO.getIdCine()));
        
        if(!cine.getUsuarioCreador().getId().equals(credencialesUsuarioActual.getId())) throw new AutorizacionException("No puedes modificar este cine");
        
    }

    public void validarEliminarCalificacion(CalificacionSalaDTO calificacionDTO) throws AutorizacionException {
        credencialesUsuarioActual = MANEJADOR_JWT.validarToken(AUTH_HEADER);
        if(!credencialesUsuarioActual.getId().equals(Integer.valueOf(calificacionDTO.getIdUsuario()))) throw new AutorizacionException("No puedes modificar esta calificacion");
    }

    
    
}
