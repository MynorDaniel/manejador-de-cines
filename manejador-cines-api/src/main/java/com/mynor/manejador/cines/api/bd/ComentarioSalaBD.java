/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.bd;

import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.filtros.FiltrosComentarioSala;
import com.mynor.manejador.cines.api.modelo.ComentarioSala;
import com.mynor.manejador.cines.api.modelo.Sala;
import com.mynor.manejador.cines.api.modelo.Usuario;
import java.sql.*;

/**
 *
 * @author mynordma
 */
public class ComentarioSalaBD implements BaseDeDatos<ComentarioSala, FiltrosComentarioSala> {

    @Override
    public ComentarioSala crear(ComentarioSala entidad, Connection conn) throws AccesoDeDatosException {
        String sqlComentario = "INSERT INTO Comentario (usuario, contenido, fecha) VALUES (?, ?, ?)";
        String sqlComentarioSala = "INSERT INTO ComentarioSala (comentario, sala) VALUES (?, ?)";
        
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
            
            try (PreparedStatement stmtComentarioSala = conn.prepareStatement(sqlComentarioSala)) {
                stmtComentarioSala.setInt(1, entidad.getId());
                stmtComentarioSala.setInt(2, entidad.getSala().getId());
                
                stmtComentarioSala.executeUpdate();
            }
            
            return entidad;
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al crear comentario de sala: " + e.getMessage());
        }
    }

    @Override
    public ComentarioSala[] leer(FiltrosComentarioSala filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder(
            "SELECT c.id, c.usuario, c.contenido, c.fecha, cs.sala " +
            "FROM Comentario c " +
            "INNER JOIN ComentarioSala cs ON c.id = cs.comentario " +
            "WHERE 1=1"
        );
        
        if (filtros.getIdComentario().isPresent()) {
            sql.append(" AND cs.comentario = ?");
        }
        if (filtros.getIdSala().isPresent()) {
            sql.append(" AND cs.sala = ?");
        }
        
        sql.append(" ORDER BY c.fecha DESC, c.id DESC");
        
        try (PreparedStatement stmt = conn.prepareStatement(sql.toString(),
                                                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                            ResultSet.CONCUR_READ_ONLY)) {
            int paramIndex = 1;
            
            if (filtros.getIdComentario().isPresent()) {
                stmt.setInt(paramIndex++, filtros.getIdComentario().get());
            }
            if (filtros.getIdSala().isPresent()) {
                stmt.setInt(paramIndex++, filtros.getIdSala().get());
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                int longitud = obtenerLongitudRS(rs);
                ComentarioSala[] comentarios = new ComentarioSala[longitud];
                int index = 0;
                
                while (rs.next()) {
                    comentarios[index++] = mapearComentarioSalaBasico(rs);
                }
                
                return comentarios;
            }
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al leer comentarios de sala: " + e.getMessage());
        }
    }

    @Override
    public ComentarioSala[] leerCompleto(FiltrosComentarioSala filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder(
            "SELECT " +
            "c.id as comentario_id, " +
            "c.usuario as comentario_usuario, " +
            "c.contenido as comentario_contenido, " +
            "c.fecha as comentario_fecha, " +
            "cs.sala as comentariosala_sala, " +
            "u.id as usuario_id, " +
            "u.nombre as usuario_nombre, " +
            "u.correo as usuario_correo, " +
            "u.activado as usuario_activado, " +
            "s.id as sala_id, " +
            "s.cine as sala_cine " +
            "FROM Comentario c " +
            "INNER JOIN ComentarioSala cs ON c.id = cs.comentario " +
            "LEFT JOIN Usuario u ON c.usuario = u.id " +
            "LEFT JOIN Sala s ON cs.sala = s.id " +
            "WHERE 1=1"
        );
        
        if (filtros.getIdComentario().isPresent()) {
            sql.append(" AND cs.comentario = ?");
        }
        if (filtros.getIdSala().isPresent()) {
            sql.append(" AND cs.sala = ?");
        }
        
        sql.append(" ORDER BY c.fecha DESC, c.id DESC");
        
        try (PreparedStatement stmt = conn.prepareStatement(sql.toString(),
                                                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                            ResultSet.CONCUR_READ_ONLY)) {
            int paramIndex = 1;
            
            if (filtros.getIdComentario().isPresent()) {
                stmt.setInt(paramIndex++, filtros.getIdComentario().get());
            }
            if (filtros.getIdSala().isPresent()) {
                stmt.setInt(paramIndex++, filtros.getIdSala().get());
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                int longitud = obtenerLongitudRS(rs);
                ComentarioSala[] comentarios = new ComentarioSala[longitud];
                int index = 0;
                
                while (rs.next()) {
                    comentarios[index++] = mapearComentarioSalaCompleto(rs);
                }
                
                return comentarios;
            }
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al leer comentarios de sala completos: " + e.getMessage());
        }
    }

    @Override
    public ComentarioSala actualizar(ComentarioSala entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "UPDATE Comentario SET contenido = ? WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entidad.getContenido());
            stmt.setInt(2, entidad.getId());
            
            int filasActualizadas = stmt.executeUpdate();
            
            if (filasActualizadas == 0) {
                throw new AccesoDeDatosException("No se encontró el comentario con id: " + entidad.getId());
            }
            
            return entidad;
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al actualizar comentario de sala: " + e.getMessage());
        }
    }

    @Override
    public ComentarioSala eliminar(ComentarioSala entidad, Connection conn) throws AccesoDeDatosException {
        String sqlComentarioSala = "DELETE FROM ComentarioSala WHERE comentario = ?";
        String sqlComentario = "DELETE FROM Comentario WHERE id = ?";
        
        try {
            try (PreparedStatement stmt = conn.prepareStatement(sqlComentarioSala)) {
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
            throw new AccesoDeDatosException("Error al eliminar comentario de sala: " + e.getMessage());
        }
    }
    
    private ComentarioSala mapearComentarioSalaBasico(ResultSet rs) throws SQLException {
        ComentarioSala comentario = new ComentarioSala();
        comentario.setId(rs.getInt("id"));
        comentario.setContenido(rs.getString("contenido"));
        comentario.setFecha(rs.getDate("fecha").toLocalDate());
        
        if (rs.getObject("usuario") != null) {
            Usuario usuario = new Usuario();
            usuario.setId(rs.getInt("usuario"));
            comentario.setUsuario(usuario);
        }
        
        if (rs.getObject("sala") != null) {
            Sala sala = new Sala();
            sala.setId(rs.getInt("sala"));
            comentario.setSala(sala);
        }
        
        return comentario;
    }
    
    private ComentarioSala mapearComentarioSalaCompleto(ResultSet rs) throws SQLException {
        ComentarioSala comentario = new ComentarioSala();
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
        
        if (rs.getObject("sala_id") != null) {
            Sala sala = new Sala();
            sala.setId(rs.getInt("sala_id"));
            comentario.setSala(sala);
        }
        
        return comentario;
    }
}
