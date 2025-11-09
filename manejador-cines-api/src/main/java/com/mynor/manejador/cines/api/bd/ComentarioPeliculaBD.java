/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.bd;

import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.filtros.FiltrosComentarioPelicula;
import com.mynor.manejador.cines.api.modelo.ComentarioPelicula;
import com.mynor.manejador.cines.api.modelo.Pelicula;
import com.mynor.manejador.cines.api.modelo.Usuario;
import java.sql.*;

/**
 *
 * @author mynordma
 */
public class ComentarioPeliculaBD implements BaseDeDatos<ComentarioPelicula, FiltrosComentarioPelicula> {

    @Override
    public ComentarioPelicula crear(ComentarioPelicula entidad, Connection conn) throws AccesoDeDatosException {
        String sqlComentario = "INSERT INTO Comentario (usuario, contenido, fecha) VALUES (?, ?, ?)";
        String sqlComentarioPelicula = "INSERT INTO ComentarioPelicula (comentario, pelicula) VALUES (?, ?)";
        
        try {
            try (PreparedStatement stmtComentario = conn.prepareStatement(sqlComentario, Statement.RETURN_GENERATED_KEYS)) {
                stmtComentario.setInt(1, entidad.getUsuario().getId());
                stmtComentario.setString(2, entidad.getContenido());
                stmtComentario.setDate(3, Date.valueOf(entidad.getFecha()));
                
                stmtComentario.executeUpdate();
                
                try (ResultSet rs = stmtComentario.getGeneratedKeys()) {
                    if (rs.next()) {
                        entidad.setId(rs.getInt(1));
                    }
                }
            }
            
            try (PreparedStatement stmtComentarioPelicula = conn.prepareStatement(sqlComentarioPelicula)) {
                stmtComentarioPelicula.setInt(1, entidad.getId());
                stmtComentarioPelicula.setInt(2, entidad.getPelicula().getId());
                
                stmtComentarioPelicula.executeUpdate();
            }
            
            return entidad;
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al crear comentario de película: " + e.getMessage());
        }
    }

    @Override
    public ComentarioPelicula[] leer(FiltrosComentarioPelicula filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder(
            "SELECT c.id, c.usuario, c.contenido, c.fecha, cp.pelicula " +
            "FROM Comentario c " +
            "INNER JOIN ComentarioPelicula cp ON c.id = cp.comentario " +
            "WHERE 1=1"
        );
        
        if (filtros.getIdComentario().isPresent()) {
            sql.append(" AND cp.comentario = ?");
        }
        if (filtros.getIdPelicula().isPresent()) {
            sql.append(" AND cp.pelicula = ?");
        }
        
        sql.append(" ORDER BY c.fecha DESC, c.id DESC");
        
        try (PreparedStatement stmt = conn.prepareStatement(sql.toString(),
                                                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                            ResultSet.CONCUR_READ_ONLY)) {
            int paramIndex = 1;
            
            if (filtros.getIdComentario().isPresent()) {
                stmt.setInt(paramIndex++, filtros.getIdComentario().get());
            }
            if (filtros.getIdPelicula().isPresent()) {
                stmt.setInt(paramIndex++, filtros.getIdPelicula().get());
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                int longitud = obtenerLongitudRS(rs);
                ComentarioPelicula[] comentarios = new ComentarioPelicula[longitud];
                int index = 0;
                
                while (rs.next()) {
                    comentarios[index++] = mapearComentarioPeliculaBasico(rs);
                }
                
                return comentarios;
            }
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al leer comentarios de película: " + e.getMessage());
        }
    }

    @Override
    public ComentarioPelicula[] leerCompleto(FiltrosComentarioPelicula filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder(
            "SELECT " +
            "c.id as comentario_id, " +
            "c.usuario as comentario_usuario, " +
            "c.contenido as comentario_contenido, " +
            "c.fecha as comentario_fecha, " +
            "cp.pelicula as comentariopelicula_pelicula, " +
            "u.id as usuario_id, " +
            "u.nombre as usuario_nombre, " +
            "u.correo as usuario_correo, " +
            "u.activado as usuario_activado, " +
            "p.id as pelicula_id, " +
            "p.titulo as pelicula_titulo, " +
            "p.clasificacion as pelicula_clasificacion, " +
            "p.sinopsis as pelicula_sinopsis, " +
            "p.fecha_estreno as pelicula_fecha_estreno, " +
            "p.duracion as pelicula_duracion, " +
            "p.director as pelicula_director, " +
            "p.reparto as pelicula_reparto " +
            "FROM Comentario c " +
            "INNER JOIN ComentarioPelicula cp ON c.id = cp.comentario " +
            "LEFT JOIN Usuario u ON c.usuario = u.id " +
            "LEFT JOIN Pelicula p ON cp.pelicula = p.id " +
            "WHERE 1=1"
        );
        
        if (filtros.getIdComentario().isPresent()) {
            sql.append(" AND cp.comentario = ?");
        }
        if (filtros.getIdPelicula().isPresent()) {
            sql.append(" AND cp.pelicula = ?");
        }
        
        sql.append(" ORDER BY c.fecha DESC, c.id DESC");
        
        try (PreparedStatement stmt = conn.prepareStatement(sql.toString(),
                                                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                            ResultSet.CONCUR_READ_ONLY)) {
            int paramIndex = 1;

            if (filtros.getIdComentario().isPresent()) {
                stmt.setInt(paramIndex++, filtros.getIdComentario().get());
            }
            if (filtros.getIdPelicula().isPresent()) {
                stmt.setInt(paramIndex++, filtros.getIdPelicula().get());
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                int longitud = obtenerLongitudRS(rs);
                ComentarioPelicula[] comentarios = new ComentarioPelicula[longitud];
                int index = 0;
                
                while (rs.next()) {
                    comentarios[index++] = mapearComentarioPeliculaCompleto(rs);
                }
                
                return comentarios;
            }
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al leer comentarios de película completos: " + e.getMessage());
        }
    }

    @Override
    public ComentarioPelicula actualizar(ComentarioPelicula entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "UPDATE Comentario SET usuario = ?, contenido = ?, fecha = ? WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, entidad.getUsuario() != null ? entidad.getUsuario().getId() : null, Types.INTEGER);
            stmt.setString(2, entidad.getContenido());
            stmt.setDate(3, Date.valueOf(entidad.getFecha()));
            stmt.setInt(4, entidad.getId());
            
            int filasActualizadas = stmt.executeUpdate();
            
            if (filasActualizadas == 0) {
                throw new AccesoDeDatosException("No se encontró el comentario con id: " + entidad.getId());
            }
            
            return entidad;
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al actualizar comentario de película: " + e.getMessage());
        }
    }

    @Override
    public ComentarioPelicula eliminar(ComentarioPelicula entidad, Connection conn) throws AccesoDeDatosException {
        String sqlComentarioPelicula = "DELETE FROM ComentarioPelicula WHERE comentario = ?";
        String sqlComentario = "DELETE FROM Comentario WHERE id = ?";
        
        try {
            try (PreparedStatement stmt = conn.prepareStatement(sqlComentarioPelicula)) {
                stmt.setInt(1, entidad.getId());
                stmt.executeUpdate();
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sqlComentario)) {
                stmt.setInt(1, entidad.getId());
                
                int filasEliminadas = stmt.executeUpdate();
                
                if (filasEliminadas == 0) {
                    throw new AccesoDeDatosException("No se encontró el comentario con id: " + entidad.getId());
                }
            }
            
            return entidad;
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al eliminar comentario de película: " + e.getMessage());
        }
    }
    
    private ComentarioPelicula mapearComentarioPeliculaBasico(ResultSet rs) throws SQLException {
        ComentarioPelicula comentario = new ComentarioPelicula();
        comentario.setId(rs.getInt("id"));
        comentario.setContenido(rs.getString("contenido"));
        comentario.setFecha(rs.getDate("fecha").toLocalDate());
        
        if (rs.getObject("usuario") != null) {
            Usuario usuario = new Usuario();
            usuario.setId(rs.getInt("usuario"));
            comentario.setUsuario(usuario);
        }
        
        if (rs.getObject("pelicula") != null) {
            Pelicula pelicula = new Pelicula();
            pelicula.setId(rs.getInt("pelicula"));
            comentario.setPelicula(pelicula);
        }
        
        return comentario;
    }
    
    private ComentarioPelicula mapearComentarioPeliculaCompleto(ResultSet rs) throws SQLException {
        ComentarioPelicula comentario = new ComentarioPelicula();
        comentario.setId(rs.getInt("comentario_id"));
        comentario.setContenido(rs.getString("comentario_contenido"));
        comentario.setFecha(rs.getDate("comentario_fecha").toLocalDate());
        
        if (rs.getObject("usuario_id") != null) {
            Usuario usuario = new Usuario();
            usuario.setId(rs.getInt("usuario_id"));
            usuario.setNombre(rs.getString("usuario_nombre"));
            usuario.setCorreo(rs.getString("usuario_correo"));
            usuario.setActivado(rs.getBoolean("usuario_activado"));
            comentario.setUsuario(usuario);
        }
        
        if (rs.getObject("pelicula_id") != null) {
            Pelicula pelicula = new Pelicula();
            pelicula.setId(rs.getInt("pelicula_id"));
            pelicula.setTitulo(rs.getString("pelicula_titulo"));
            pelicula.setSinopsis(rs.getString("pelicula_sinopsis"));
            pelicula.setFechaEstreno(rs.getDate("pelicula_fecha_estreno").toLocalDate());
            pelicula.setDuracion(rs.getInt("pelicula_duracion"));
            pelicula.setDirector(rs.getString("pelicula_director"));
            pelicula.setReparto(rs.getString("pelicula_reparto"));
            comentario.setPelicula(pelicula);
        }
        
        return comentario;
    }
}
