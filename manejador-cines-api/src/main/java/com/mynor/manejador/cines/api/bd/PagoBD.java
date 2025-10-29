/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.bd;

import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.filtros.FiltrosPago;
import com.mynor.manejador.cines.api.modelo.Imagen;
import com.mynor.manejador.cines.api.modelo.Pago;
import com.mynor.manejador.cines.api.modelo.Rol;
import com.mynor.manejador.cines.api.modelo.Usuario;
import java.sql.Connection;
import java.sql.*;

/**
 *
 * @author mynordma
 */
public class PagoBD implements BaseDeDatos<Pago, FiltrosPago> {
    
    @Override
    public Pago crear(Pago entidad, Connection conn) throws AccesoDeDatosException {
        String sql = """
            INSERT INTO Pago (usuario, fecha, monto)
            VALUES (?, ?, ?)
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entidad.getUsuario().getId());
            ps.setDate(2, Date.valueOf(entidad.getFecha()));
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
    public Pago[] leer(FiltrosPago filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder("""
            SELECT id, usuario, fecha, monto
            FROM Pago
            WHERE 1=1
        """);
        
        if (filtros != null) {
            if (filtros.getId().isPresent()) {
                sql.append(" AND id = ?");
            }
            if (filtros.getIdUsuario().isPresent()) {
                sql.append(" AND usuario = ?");
            }
        }
        
        try (PreparedStatement ps = conn.prepareStatement(sql.toString(),
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            
            int index = 1;
            if (filtros != null) {
                if (filtros.getId().isPresent()) {
                    ps.setInt(index++, filtros.getId().get());
                }
                if (filtros.getIdUsuario().isPresent()) {
                    ps.setInt(index++, filtros.getIdUsuario().get());
                }
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                int total = obtenerLongitudRS(rs);
                Pago[] pagos = new Pago[total];
                int i = 0;
                
                while (rs.next()) {
                    Pago pago = new Pago();
                    pago.setId(rs.getInt("id"));
                    
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("usuario"));
                    pago.setUsuario(usuario);
                    
                    pago.setFecha(rs.getDate("fecha").toLocalDate());
                    pago.setMonto(rs.getDouble("monto"));
                    
                    pagos[i++] = pago;
                }
                return pagos;
            }
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public Pago[] leerCompleto(FiltrosPago filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder("""
            SELECT 
                p.id,
                p.usuario,
                p.fecha,
                p.monto,
                u.id as usuario_id,
                u.imagen as usuario_imagen,
                u.nombre as usuario_nombre,
                u.rol as usuario_rol,
                u.correo as usuario_correo,
                u.clave as usuario_clave,
                u.activado as usuario_activado
            FROM Pago p
            LEFT JOIN Usuario u ON p.usuario = u.id
            WHERE 1=1
        """);
        
        if (filtros != null) {
            if (filtros.getId().isPresent()) {
                sql.append(" AND p.id = ?");
            }
            if (filtros.getIdUsuario().isPresent()) {
                sql.append(" AND p.usuario = ?");
            }
        }
        
        try (PreparedStatement ps = conn.prepareStatement(sql.toString(),
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            
            int index = 1;
            if (filtros != null) {
                if (filtros.getId().isPresent()) {
                    ps.setInt(index++, filtros.getId().get());
                }
                if (filtros.getIdUsuario().isPresent()) {
                    ps.setInt(index++, filtros.getIdUsuario().get());
                }
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                int total = obtenerLongitudRS(rs);
                Pago[] pagos = new Pago[total];
                int i = 0;
                
                while (rs.next()) {
                    Pago pago = new Pago();
                    pago.setId(rs.getInt("id"));
                    pago.setFecha(rs.getDate("fecha").toLocalDate());
                    pago.setMonto(rs.getDouble("monto"));
                    
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
                        
                        pago.setUsuario(usuario);
                    }
                    
                    pagos[i++] = pago;
                }
                return pagos;
            }
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public Pago actualizar(Pago entidad, Connection conn) throws AccesoDeDatosException {
        String sql = """
            UPDATE Pago 
            SET usuario = ?, fecha = ?, monto = ?
            WHERE id = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entidad.getUsuario().getId());
            ps.setDate(2, Date.valueOf(entidad.getFecha()));
            ps.setDouble(3, entidad.getMonto());
            ps.setInt(4, entidad.getId());
            ps.executeUpdate();
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public Pago eliminar(Pago entidad, Connection conn) throws AccesoDeDatosException {
        String sqlPagoAnuncio = "DELETE FROM PagoAnuncio WHERE pago = ?";
        try (PreparedStatement psPagoAnuncio = conn.prepareStatement(sqlPagoAnuncio)) {
            psPagoAnuncio.setInt(1, entidad.getId());
            psPagoAnuncio.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
        
        String sql = "DELETE FROM Pago WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entidad.getId());
            ps.executeUpdate();
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }
}
