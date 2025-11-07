/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.bd;

import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.filtros.FiltrosSala;
import com.mynor.manejador.cines.api.modelo.Cine;
import com.mynor.manejador.cines.api.modelo.Sala;
import com.mynor.manejador.cines.api.modelo.Usuario;
import java.sql.*;

/**
 *
 * @author mynordma
 */
public class SalaBD implements BaseDeDatos<Sala, FiltrosSala> {

    @Override
    public Sala crear(Sala entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "INSERT INTO Sala (cine, filas_asientos, columnas_asientos, " +
                     "calificaciones_bloqueadas, comentarios_bloqueados, visible) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, entidad.getCine().getId());
            stmt.setInt(2, entidad.getFilasAsientos());
            stmt.setInt(3, entidad.getColumnasAsientos());
            stmt.setBoolean(4, entidad.getCalificacionesBloqueadas());
            stmt.setBoolean(5, entidad.getComentariosBloqueados());
            stmt.setBoolean(6, entidad.getVisible());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    entidad.setId(rs.getInt(1));
                }
            }
            
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al crear sala");
        }
    }

    @Override
    public Sala[] leer(FiltrosSala filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder("SELECT id, cine, filas_asientos, columnas_asientos, " +
                                             "calificaciones_bloqueadas, comentarios_bloqueados, visible " +
                                             "FROM Sala WHERE 1=1");
        
        if (filtros.getId().isPresent()) {
            sql.append(" AND id = ?");
        }
        if (filtros.getIdCine().isPresent()) {
            sql.append(" AND cine = ?");
        }
        if (filtros.getVisible().isPresent()) {
            sql.append(" AND visible = ?");
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(sql.toString(), 
                                                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                            ResultSet.CONCUR_READ_ONLY)) {
            int paramIndex = 1;
            
            if (filtros.getId().isPresent()) {
                stmt.setInt(paramIndex++, filtros.getId().get());
            }
            if (filtros.getIdCine().isPresent()) {
                stmt.setInt(paramIndex++, filtros.getIdCine().get());
            }
            if (filtros.getVisible().isPresent()) {
                stmt.setBoolean(paramIndex++, filtros.getVisible().get());
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                int longitud = obtenerLongitudRS(rs);
                Sala[] salas = new Sala[longitud];
                int index = 0;
                
                while (rs.next()) {
                    Sala sala = new Sala();
                    sala.setId(rs.getInt("id"));
                    
                    Cine cine = new Cine();
                    cine.setId(rs.getInt("cine"));
                    sala.setCine(cine);
                    
                    sala.setFilasAsientos(rs.getInt("filas_asientos"));
                    sala.setColumnasAsientos(rs.getInt("columnas_asientos"));
                    sala.setCalificacionesBloqueadas(rs.getBoolean("calificaciones_bloqueadas"));
                    sala.setComentariosBloqueados(rs.getBoolean("comentarios_bloqueados"));
                    sala.setVisible(rs.getBoolean("visible"));
                    
                    salas[index++] = sala;
                }
                
                return salas;
            }
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al leer salas");
        }
    }

    @Override
    public Sala[] leerCompleto(FiltrosSala filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder(
            "SELECT s.id, s.cine, s.filas_asientos, s.columnas_asientos, " +
            "s.calificaciones_bloqueadas, s.comentarios_bloqueados, s.visible, " +
            "c.id AS cine_id, c.usuario_creador, c.nombre AS cine_nombre, " +
            "c.ubicacion, c.activado AS cine_activado, " +
            "u.id AS usuario_id, u.imagen, u.nombre AS usuario_nombre, u.rol, " +
            "u.correo, u.clave, u.activado AS usuario_activado " +
            "FROM Sala s " +
            "LEFT JOIN Cine c ON s.cine = c.id " +
            "LEFT JOIN Usuario u ON c.usuario_creador = u.id " +
            "WHERE 1=1"
        );
        
        if (filtros.getId().isPresent()) {
            sql.append(" AND s.id = ?");
        }
        if (filtros.getIdCine().isPresent()) {
            sql.append(" AND s.cine = ?");
        }
        if (filtros.getVisible().isPresent()) {
            sql.append(" AND s.visible = ?");
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(sql.toString(),
                                                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                            ResultSet.CONCUR_READ_ONLY)) {
            int paramIndex = 1;
            
            if (filtros.getId().isPresent()) {
                stmt.setInt(paramIndex++, filtros.getId().get());
            }
            if (filtros.getIdCine().isPresent()) {
                stmt.setInt(paramIndex++, filtros.getIdCine().get());
            }
            if (filtros.getVisible().isPresent()) {
                stmt.setBoolean(paramIndex++, filtros.getVisible().get());
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                int longitud = obtenerLongitudRS(rs);
                Sala[] salas = new Sala[longitud];
                int index = 0;
                
                while (rs.next()) {
                    Sala sala = new Sala();
                    sala.setId(rs.getInt("id"));
                    sala.setFilasAsientos(rs.getInt("filas_asientos"));
                    sala.setColumnasAsientos(rs.getInt("columnas_asientos"));
                    sala.setCalificacionesBloqueadas(rs.getBoolean("calificaciones_bloqueadas"));
                    sala.setComentariosBloqueados(rs.getBoolean("comentarios_bloqueados"));
                    sala.setVisible(rs.getBoolean("visible"));
                    
                    Cine cine = new Cine();
                    cine.setId(rs.getInt("cine_id"));
                    cine.setNombre(rs.getString("cine_nombre"));
                    cine.setUbicacion(rs.getString("ubicacion"));
                    cine.setActivado(rs.getBoolean("cine_activado"));
                    
                    if (rs.getObject("usuario_id") != null) {
                        Usuario usuario = new Usuario();
                        usuario.setId(rs.getInt("usuario_id"));
                        
                        cine.setUsuarioCreador(usuario);
                    }
                    
                    sala.setCine(cine);
                    salas[index++] = sala;
                }
                
                return salas;
            }
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al leer salas completas");
        }
    }

    @Override
    public Sala actualizar(Sala entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "UPDATE Sala SET filas_asientos = ?, columnas_asientos = ?, " +
                     "calificaciones_bloqueadas = ?, comentarios_bloqueados = ?, visible = ? " +
                     "WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, entidad.getFilasAsientos());
            stmt.setInt(2, entidad.getColumnasAsientos());
            stmt.setBoolean(3, entidad.getCalificacionesBloqueadas());
            stmt.setBoolean(4, entidad.getComentariosBloqueados());
            stmt.setBoolean(5, entidad.getVisible());
            stmt.setInt(6, entidad.getId());
            
            int filasActualizadas = stmt.executeUpdate();
            if (filasActualizadas == 0) {
                throw new AccesoDeDatosException("No se encontró la sala con id: " + entidad.getId());
            }
            
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al actualizar sala");
        }
    }

    @Override
    public Sala eliminar(Sala entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "DELETE FROM Sala WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, entidad.getId());
            
            int filasEliminadas = stmt.executeUpdate();
            if (filasEliminadas == 0) {
                throw new AccesoDeDatosException("No se encontró la sala con id: " + entidad.getId());
            }
            
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al eliminar sala");
        }
    }
}
