/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.bd;

import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.filtros.FiltrosCostoDiarioCine;
import com.mynor.manejador.cines.api.modelo.Cine;
import com.mynor.manejador.cines.api.modelo.CostoDiarioCine;
import com.mynor.manejador.cines.api.modelo.Imagen;
import com.mynor.manejador.cines.api.modelo.Rol;
import com.mynor.manejador.cines.api.modelo.Usuario;
import java.sql.*;

/**
 *
 * @author mynordma
 */
public class CostoDiarioCineBD implements BaseDeDatos<CostoDiarioCine, FiltrosCostoDiarioCine> {
    
    @Override
    public CostoDiarioCine crear(CostoDiarioCine entidad, Connection conn) throws AccesoDeDatosException {
        String sql = """
            INSERT INTO CostoDiarioCine (cine, fecha_cambio, monto)
            VALUES (?, ?, ?)
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entidad.getCine().getId());
            ps.setDate(2, Date.valueOf(entidad.getFechaCambio()));
            ps.setDouble(3, entidad.getMonto());
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
    public CostoDiarioCine[] leer(FiltrosCostoDiarioCine filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder("""
            SELECT id, cine, fecha_cambio, monto
            FROM CostoDiarioCine
            WHERE 1=1
        """);
        
        if (filtros != null) {
            if (filtros.getId().isPresent()) {
                sql.append(" AND id = ?");
            }
            if (filtros.getIdCine().isPresent()) {
                sql.append(" AND cine = ?");
            }
        }
        
        try (PreparedStatement ps = conn.prepareStatement(sql.toString(),
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            
            int index = 1;
            if (filtros != null) {
                if (filtros.getId().isPresent()) {
                    ps.setInt(index++, filtros.getId().get());
                }
                if (filtros.getIdCine().isPresent()) {
                    ps.setInt(index++, filtros.getIdCine().get());
                }
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                int total = obtenerLongitudRS(rs);
                CostoDiarioCine[] costos = new CostoDiarioCine[total];
                int i = 0;
                
                while (rs.next()) {
                    CostoDiarioCine costo = new CostoDiarioCine();
                    costo.setId(rs.getInt("id"));
                    
                    Cine cine = new Cine();
                    cine.setId(rs.getInt("cine"));
                    costo.setCine(cine);
                    
                    costo.setFechaCambio(rs.getDate("fecha_cambio").toLocalDate());
                    costo.setMonto(rs.getDouble("monto"));
                    
                    costos[i++] = costo;
                }
                return costos;
            }
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public CostoDiarioCine[] leerCompleto(FiltrosCostoDiarioCine filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder("""
            SELECT 
                cdc.id,
                cdc.cine,
                cdc.fecha_cambio,
                cdc.monto,
                c.id as cine_id,
                c.usuario_creador as cine_usuario_creador,
                c.nombre as cine_nombre,
                c.ubicacion as cine_ubicacion,
                c.activado as cine_activado,
                u.id as usuario_id,
                u.imagen as usuario_imagen,
                u.nombre as usuario_nombre,
                u.rol as usuario_rol,
                u.correo as usuario_correo,
                u.clave as usuario_clave,
                u.activado as usuario_activado
            FROM CostoDiarioCine cdc
            LEFT JOIN Cine c ON cdc.cine = c.id
            LEFT JOIN Usuario u ON c.usuario_creador = u.id
            WHERE 1=1
        """);
        
        if (filtros != null) {
            if (filtros.getId().isPresent()) {
                sql.append(" AND cdc.id = ?");
            }
            if (filtros.getIdCine().isPresent()) {
                sql.append(" AND cdc.cine = ?");
            }
        }
        
        try (PreparedStatement ps = conn.prepareStatement(sql.toString(),
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            
            int index = 1;
            if (filtros != null) {
                if (filtros.getId().isPresent()) {
                    ps.setInt(index++, filtros.getId().get());
                }
                if (filtros.getIdCine().isPresent()) {
                    ps.setInt(index++, filtros.getIdCine().get());
                }
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                int total = obtenerLongitudRS(rs);
                CostoDiarioCine[] costos = new CostoDiarioCine[total];
                int i = 0;
                
                while (rs.next()) {
                    CostoDiarioCine costo = new CostoDiarioCine();
                    costo.setId(rs.getInt("id"));
                    costo.setFechaCambio(rs.getDate("fecha_cambio").toLocalDate());
                    costo.setMonto(rs.getDouble("monto"));
                    
                    if (rs.getObject("cine_id") != null) {
                        Cine cine = new Cine();
                        cine.setId(rs.getInt("cine_id"));
                        cine.setNombre(rs.getString("cine_nombre"));
                        cine.setUbicacion(rs.getString("cine_ubicacion"));
                        cine.setActivado(rs.getBoolean("cine_activado"));
                        
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
                        
                        costo.setCine(cine);
                    }
                    
                    costos[i++] = costo;
                }
                return costos;
            }
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public CostoDiarioCine actualizar(CostoDiarioCine entidad, Connection conn) throws AccesoDeDatosException {
        String sql = """
            UPDATE CostoDiarioCine 
            SET cine = ?, fecha_cambio = ?, monto = ?
            WHERE id = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entidad.getCine().getId());
            ps.setDate(2, Date.valueOf(entidad.getFechaCambio()));
            ps.setDouble(3, entidad.getMonto());
            ps.setInt(4, entidad.getId());
            ps.executeUpdate();
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public CostoDiarioCine eliminar(CostoDiarioCine entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "DELETE FROM CostoDiarioCine WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entidad.getId());
            ps.executeUpdate();
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }
}
