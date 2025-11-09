/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.servicios;

import com.mynor.manejador.cines.api.bd.BaseDeDatos;
import com.mynor.manejador.cines.api.bd.ComentarioBD;
import com.mynor.manejador.cines.api.bd.ComentarioPeliculaBD;
import com.mynor.manejador.cines.api.bd.ComentarioSalaBD;
import com.mynor.manejador.cines.api.bd.Transaccion;
import com.mynor.manejador.cines.api.dtos.ComentarioDTO;
import com.mynor.manejador.cines.api.dtos.ComentarioPeliculaDTO;
import com.mynor.manejador.cines.api.dtos.ComentarioSalaDTO;
import com.mynor.manejador.cines.api.dtos.UsuarioSalidaDTO;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.filtros.FiltrosComentario;
import com.mynor.manejador.cines.api.filtros.FiltrosComentarioPelicula;
import com.mynor.manejador.cines.api.filtros.FiltrosComentarioSala;
import com.mynor.manejador.cines.api.modelo.Comentario;
import com.mynor.manejador.cines.api.modelo.ComentarioPelicula;
import com.mynor.manejador.cines.api.modelo.ComentarioSala;
import com.mynor.manejador.cines.api.modelo.Pelicula;
import com.mynor.manejador.cines.api.modelo.Sala;
import com.mynor.manejador.cines.api.modelo.Usuario;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

/**
 *
 * @author mynordma
 */
public class ComentarioServicio {
    
    private final BaseDeDatos<Comentario, FiltrosComentario> COMENTARIO_BD;
    private final BaseDeDatos<ComentarioSala, FiltrosComentarioSala> COMENTARIO_SALA_BD;
    private final BaseDeDatos<ComentarioPelicula, FiltrosComentarioPelicula> COMENTARIO_PELICULA_BD;

    public ComentarioServicio() {
        COMENTARIO_BD = new ComentarioBD();
        COMENTARIO_SALA_BD = new ComentarioSalaBD();
        COMENTARIO_PELICULA_BD = new ComentarioPeliculaBD();
    }

    public void crearComentarioSala(ComentarioSalaDTO comentarioDTO) throws AccesoDeDatosException {
        
        ComentarioSala comentario = new ComentarioSala();
            
        Usuario usuario = new Usuario();
        usuario.setId(Integer.valueOf(comentarioDTO.getComentario().getIdUsuario()));
        comentario.setUsuario(usuario);

        SalaServicio salaServicio = new SalaServicio();
        
        Sala sala = salaServicio.leerPorId(Integer.valueOf(comentarioDTO.getIdSala()));
        comentario.setSala(sala);
        
        if(sala.getComentariosBloqueados()) throw new AccesoDeDatosException("Comentarios bloqueados");

        comentario.setContenido(comentarioDTO.getComentario().getContenido());
        comentario.setFecha(LocalDate.now());
        try (Transaccion t = new Transaccion()) {
            
            COMENTARIO_SALA_BD.crear(comentario, t.obtenerConexion());
            
            t.commit();
        }
    }

    public void crearComentarioPelicula(ComentarioPeliculaDTO comentarioDTO) throws AccesoDeDatosException {
        try (Transaccion t = new Transaccion()) {
            
            ComentarioPelicula comentario = new ComentarioPelicula();
            
            Usuario usuario = new Usuario();
            usuario.setId(Integer.valueOf(comentarioDTO.getComentario().getIdUsuario()));
            comentario.setUsuario(usuario);
            
            Pelicula pelicula = new Pelicula();
            pelicula.setId(Integer.valueOf(comentarioDTO.getIdPelicula()));
            comentario.setPelicula(pelicula);
            
            comentario.setContenido(comentarioDTO.getComentario().getContenido());
            comentario.setFecha(LocalDate.now());
            
            COMENTARIO_PELICULA_BD.crear(comentario, t.obtenerConexion());
            
            t.commit();
        }
    }

    public void editarComentario(ComentarioDTO comentarioDTO) throws AccesoDeDatosException {
        try (Transaccion t = new Transaccion()) {
            
            Comentario comentario = leerPorId(Integer.valueOf(comentarioDTO.getId()));
            
            comentario.setContenido(comentarioDTO.getContenido());
            
            COMENTARIO_BD.actualizar(comentario, t.obtenerConexion());
            
            t.commit();
        }
    }

    public void eliminarComentario(Integer id) throws AccesoDeDatosException {
        try (Transaccion t = new Transaccion()) {
            
            Comentario comentario = leerPorId(id);
            
            COMENTARIO_BD.eliminar(comentario, t.obtenerConexion());
            
            t.commit();
        }
    }

    public ComentarioDTO verComentario(Integer id) throws AccesoDeDatosException {
        Comentario comentario = leerPorId(id);
        
        ComentarioDTO comentarioDTO = new ComentarioDTO();
        comentarioDTO.setId(comentario.getId().toString());
        comentarioDTO.setIdUsuario(comentario.getUsuario().getId().toString());
        comentarioDTO.setContenido(comentario.getContenido());
        comentarioDTO.setFecha(comentario.getFecha().toString());
        
        return comentarioDTO;
    }

    public ComentarioDTO[] verComentarios() throws AccesoDeDatosException {
        try (Transaccion t = new Transaccion()) {
            
            FiltrosComentario filtros = new FiltrosComentario();
            Comentario[] comentarios = COMENTARIO_BD.leerCompleto(filtros, t.obtenerConexion());
            
            t.commit();
            
            return Arrays.stream(comentarios).map(comentario -> {
                ComentarioDTO comentarioDTO = new ComentarioDTO();
                comentarioDTO.setId(comentario.getId().toString());
                comentarioDTO.setIdUsuario(comentario.getUsuario().getId().toString());
                comentarioDTO.setContenido(comentario.getContenido());
                comentarioDTO.setFecha(comentario.getFecha().toString());
                return comentarioDTO;
            }).toArray(ComentarioDTO[]::new);
        }
    }

    public ComentarioSalaDTO[] verComentariosPorSala(Integer idSala) throws AccesoDeDatosException {
        try (Transaccion t = new Transaccion()) {

            FiltrosComentarioSala filtros = new FiltrosComentarioSala();
            filtros.setIdSala(Optional.of(idSala));

            ComentarioSala[] comentarios = COMENTARIO_SALA_BD.leerCompleto(filtros, t.obtenerConexion());

            t.commit();

            return Arrays.stream(comentarios).map(comentario -> {
                ComentarioDTO comentarioDTO = new ComentarioDTO();
                comentarioDTO.setId(comentario.getId().toString());
                comentarioDTO.setIdUsuario(comentario.getUsuario().getId().toString());
                comentarioDTO.setContenido(comentario.getContenido());
                comentarioDTO.setFecha(comentario.getFecha().toString());
                
                UsuarioSalidaDTO usuarioDTO = new UsuarioSalidaDTO();
                usuarioDTO.setId(comentario.getUsuario().getId());
                usuarioDTO.setNombre(comentario.getUsuario().getNombre());
                comentarioDTO.setUsuario(usuarioDTO);

                ComentarioSalaDTO dto = new ComentarioSalaDTO();
                dto.setComentario(comentarioDTO);
                dto.setIdSala(comentario.getSala().getId().toString());

                return dto;
            }).toArray(ComentarioSalaDTO[]::new);
        }
    }

    public ComentarioPeliculaDTO[] verComentariosPorPelicula(Integer idPelicula) throws AccesoDeDatosException {
        try (Transaccion t = new Transaccion()) {

            FiltrosComentarioPelicula filtros = new FiltrosComentarioPelicula();
            filtros.setIdPelicula(Optional.of(idPelicula));

            ComentarioPelicula[] comentarios = COMENTARIO_PELICULA_BD.leerCompleto(filtros, t.obtenerConexion());

            t.commit();

            return Arrays.stream(comentarios).map(comentario -> {
                ComentarioDTO comentarioDTO = new ComentarioDTO();
                comentarioDTO.setId(comentario.getId().toString());
                comentarioDTO.setIdUsuario(comentario.getUsuario().getId().toString());
                comentarioDTO.setContenido(comentario.getContenido());
                comentarioDTO.setFecha(comentario.getFecha().toString());
                
                UsuarioSalidaDTO usuarioDTO = new UsuarioSalidaDTO();
                usuarioDTO.setId(comentario.getUsuario().getId());
                usuarioDTO.setNombre(comentario.getUsuario().getNombre());
                comentarioDTO.setUsuario(usuarioDTO);

                ComentarioPeliculaDTO dto = new ComentarioPeliculaDTO();
                dto.setComentario(comentarioDTO);
                dto.setIdPelicula(comentario.getPelicula().getId().toString());

                return dto;
            }).toArray(ComentarioPeliculaDTO[]::new);
        }
    }

    
    public Comentario leerPorId(Integer id) throws AccesoDeDatosException {
        try (Transaccion t = new Transaccion()) {
            FiltrosComentario filtros = new FiltrosComentario();
            filtros.setId(Optional.of(id));
            
            Comentario[] coincidencias = COMENTARIO_BD.leer(filtros, t.obtenerConexion());
            
            if (coincidencias.length < 1) {
                throw new AccesoDeDatosException("No se encontró el comentario con id: " + id);
            }
            
            t.commit();
            
            return coincidencias[0];
        }
    }
}