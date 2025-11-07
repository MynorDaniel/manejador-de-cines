/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.bd;

import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.filtros.FiltrosCalificacion;
import com.mynor.manejador.cines.api.modelo.Calificacion;
import com.mynor.manejador.cines.api.modelo.Imagen;
import com.mynor.manejador.cines.api.modelo.Rol;
import com.mynor.manejador.cines.api.modelo.Usuario;
import java.sql.*;

/**
 *
 * @author mynordma
 */
public class CalificacionBD implements BaseDeDatos<Calificacion, FiltrosCalificacion> {

    @Override
    public Calificacion crear(Calificacion entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "INSERT INTO Calificacion (usuario, valor, fecha) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, entidad.getUsuario().getId());
            stmt.setInt(2, entidad.getValor());
            stmt.setDate(3, Date.valueOf(entidad.getFecha()));
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    entidad.setId(rs.getInt(1));
                }
            }
            
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al crear calificación");
        }
    }

    @Override
    public Calificacion[] leer(FiltrosCalificacion filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder("SELECT id, usuario, valor, fecha FROM Calificacion WHERE 1=1");
        
        if (filtros.getId().isPresent()) {
            sql.append(" AND id = ?");
        }
        if (filtros.getIdUsuario().isPresent()) {
            sql.append(" AND usuario = ?");
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(sql.toString(),
                                                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                            ResultSet.CONCUR_READ_ONLY)) {
            int paramIndex = 1;
            
            if (filtros.getId().isPresent()) {
                stmt.setInt(paramIndex++, filtros.getId().get());
            }
            if (filtros.getIdUsuario().isPresent()) {
                stmt.setInt(paramIndex++, filtros.getIdUsuario().get());
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                int longitud = obtenerLongitudRS(rs);
                Calificacion[] calificaciones = new Calificacion[longitud];
                int index = 0;
                
                while (rs.next()) {
                    Calificacion calificacion = new Calificacion();
                    calificacion.setId(rs.getInt("id"));
                    
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("usuario"));
                    calificacion.setUsuario(usuario);
                    
                    calificacion.setValor(rs.getInt("valor"));
                    calificacion.setFecha(rs.getDate("fecha").toLocalDate());
                    
                    calificaciones[index++] = calificacion;
                }
                
                return calificaciones;
            }
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al leer calificaciones");
        }
    }

    @Override
    public Calificacion[] leerCompleto(FiltrosCalificacion filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder(
            "SELECT c.id, c.usuario, c.valor, c.fecha, " +
            "u.id AS usuario_id, u.imagen, u.nombre AS usuario_nombre, u.rol, " +
            "u.correo, u.clave, u.activado " +
            "FROM Calificacion c " +
            "LEFT JOIN Usuario u ON c.usuario = u.id " +
            "WHERE 1=1"
        );
        
        if (filtros.getId().isPresent()) {
            sql.append(" AND c.id = ?");
        }
        if (filtros.getIdUsuario().isPresent()) {
            sql.append(" AND c.usuario = ?");
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(sql.toString(),
                                                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                            ResultSet.CONCUR_READ_ONLY)) {
            int paramIndex = 1;
            
            if (filtros.getId().isPresent()) {
                stmt.setInt(paramIndex++, filtros.getId().get());
            }
            if (filtros.getIdUsuario().isPresent()) {
                stmt.setInt(paramIndex++, filtros.getIdUsuario().get());
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                int longitud = obtenerLongitudRS(rs);
                Calificacion[] calificaciones = new Calificacion[longitud];
                int index = 0;
                
                while (rs.next()) {
                    Calificacion calificacion = new Calificacion();
                    calificacion.setId(rs.getInt("id"));
                    calificacion.setValor(rs.getInt("valor"));
                    calificacion.setFecha(rs.getDate("fecha").toLocalDate());
                    
                    if (rs.getObject("usuario_id") != null) {
                        Usuario usuario = new Usuario();
                        usuario.setId(rs.getInt("usuario_id"));
                        
                        Imagen imagen = new Imagen();
                        imagen.setId(rs.getInt("imagen"));
                        
                        usuario.setImagen(imagen);
                        usuario.setNombre(rs.getString("usuario_nombre"));
                        usuario.setRol(Rol.valueOf(rs.getString("rol")));
                        usuario.setCorreo(rs.getString("correo"));
                        usuario.setClave(rs.getString("clave"));
                        usuario.setActivado(rs.getBoolean("activado"));
                        
                        calificacion.setUsuario(usuario);
                    }
                    
                    calificaciones[index++] = calificacion;
                }
                
                return calificaciones;
            }
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al leer calificaciones completas");
        }
    }

    @Override
    public Calificacion actualizar(Calificacion entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "UPDATE Calificacion SET valor = ? WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, entidad.getValor());
            stmt.setInt(2, entidad.getId());
            
            int filasActualizadas = stmt.executeUpdate();
            if (filasActualizadas == 0) {
                throw new AccesoDeDatosException("No se encontró la calificación con id: " + entidad.getId());
            }
            
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al actualizar calificación");
        }
    }

    @Override
    public Calificacion eliminar(Calificacion entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "DELETE FROM Calificacion WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, entidad.getId());
            
            int filasEliminadas = stmt.executeUpdate();
            if (filasEliminadas == 0) {
                throw new AccesoDeDatosException("No se encontró la calificación con id: " + entidad.getId());
            }
            
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al eliminar calificación");
        }
    }
}