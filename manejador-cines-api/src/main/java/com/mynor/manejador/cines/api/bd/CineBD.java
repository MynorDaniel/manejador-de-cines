/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.bd;

import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.filtros.FiltrosCine;
import com.mynor.manejador.cines.api.modelo.Cine;
import com.mynor.manejador.cines.api.modelo.Imagen;
import com.mynor.manejador.cines.api.modelo.Rol;
import com.mynor.manejador.cines.api.modelo.Usuario;
import java.sql.*;

/**
 *
 * @author mynordma
 */
public class CineBD implements BaseDeDatos<Cine, FiltrosCine> {
    
    @Override
    public Cine crear(Cine entidad, Connection conn) throws AccesoDeDatosException {
        String sql = """
            INSERT INTO Cine (usuario_creador, nombre, ubicacion, activado)
            VALUES (?, ?, ?, ?)
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entidad.getUsuarioCreador().getId());
            ps.setString(2, entidad.getNombre());
            ps.setString(3, entidad.getUbicacion());
            ps.setBoolean(4, entidad.getActivado());
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entidad.setId(rs.getInt(1));
                }
            }
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public Cine[] leer(FiltrosCine filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder("""
            SELECT id, usuario_creador, nombre, ubicacion, activado
            FROM Cine
            WHERE 1=1
        """);
        
        if (filtros != null) {
            if (filtros.getId().isPresent()) {
                sql.append(" AND id = ?");
            }
            if (filtros.getActivado().isPresent()) {
                sql.append(" AND activado = ?");
            }
        }
        
        try (PreparedStatement ps = conn.prepareStatement(sql.toString(),
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            
            int index = 1;
            if (filtros != null) {
                if (filtros.getId().isPresent()) {
                    ps.setInt(index++, filtros.getId().get());
                }
                if (filtros.getActivado().isPresent()) {
                    ps.setBoolean(index++, filtros.getActivado().get());
                }
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                int total = obtenerLongitudRS(rs);
                Cine[] cines = new Cine[total];
                int i = 0;
                
                while (rs.next()) {
                    Cine cine = new Cine();
                    cine.setId(rs.getInt("id"));
                    
                    Usuario usuarioCreador = new Usuario();
                    usuarioCreador.setId(rs.getInt("usuario_creador"));
                    cine.setUsuarioCreador(usuarioCreador);
                    
                    cine.setNombre(rs.getString("nombre"));
                    cine.setUbicacion(rs.getString("ubicacion"));
                    cine.setActivado(rs.getBoolean("activado"));
                    
                    cines[i++] = cine;
                }
                return cines;
            }
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public Cine[] leerCompleto(FiltrosCine filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder("""
            SELECT 
                c.id,
                c.usuario_creador,
                c.nombre,
                c.ubicacion,
                c.activado,
                u.id as usuario_id,
                u.imagen as usuario_imagen,
                u.nombre as usuario_nombre,
                u.rol as usuario_rol,
                u.correo as usuario_correo,
                u.clave as usuario_clave,
                u.activado as usuario_activado
            FROM Cine c
            LEFT JOIN Usuario u ON c.usuario_creador = u.id
            WHERE 1=1
        """);
        
        if (filtros != null) {
            if (filtros.getId().isPresent()) {
                sql.append(" AND c.id = ?");
            }
            if (filtros.getActivado().isPresent()) {
                sql.append(" AND c.activado = ?");
            }
            if (filtros.getIdUsuario().isPresent()) {
                sql.append(" AND usuario_id = ?");
            }
        }
        
        try (PreparedStatement ps = conn.prepareStatement(sql.toString(),
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            
            int index = 1;
            if (filtros != null) {
                if (filtros.getId().isPresent()) {
                    ps.setInt(index++, filtros.getId().get());
                }
                if (filtros.getActivado().isPresent()) {
                    ps.setBoolean(index++, filtros.getActivado().get());
                }
                if (filtros.getIdUsuario().isPresent()) {
                    ps.setInt(index++, filtros.getIdUsuario().get());
                }
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                int total = obtenerLongitudRS(rs);
                Cine[] cines = new Cine[total];
                int i = 0;
                
                while (rs.next()) {
                    Cine cine = new Cine();
                    cine.setId(rs.getInt("id"));
                    cine.setNombre(rs.getString("nombre"));
                    cine.setUbicacion(rs.getString("ubicacion"));
                    cine.setActivado(rs.getBoolean("activado"));
                    
                    if (rs.getObject("usuario_id") != null) {
                        Usuario usuario = new Usuario();
                        usuario.setId(rs.getInt("usuario_id"));
                        
                        Imagen img = new Imagen();
                        img.setId(rs.getInt("usuario_imagen"));
                        usuario.setImagen(img);
                        
                        usuario.setNombre(rs.getString("usuario_nombre"));
                        usuario.setRol(Rol.valueOf(rs.getString("usuario_rol")));
                        usuario.setCorreo(rs.getString("usuario_correo"));
                        usuario.setClave(rs.getString("usuario_clave"));
                        usuario.setActivado(rs.getBoolean("usuario_activado"));
                        
                        cine.setUsuarioCreador(usuario);
                    }
                    
                    cines[i++] = cine;
                }
                return cines;
            }
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public Cine actualizar(Cine entidad, Connection conn) throws AccesoDeDatosException {
        String sql = """
            UPDATE Cine 
            SET nombre = ?, ubicacion = ?, activado = ?
            WHERE id = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getUbicacion());
            ps.setBoolean(3, entidad.getActivado());
            ps.setInt(4, entidad.getId());
            ps.executeUpdate();
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public Cine eliminar(Cine entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "DELETE FROM Cine WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entidad.getId());
            ps.executeUpdate();
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }
}
