/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.bd;

import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.filtros.FiltrosPagoCine;
import com.mynor.manejador.cines.api.modelo.Cine;
import com.mynor.manejador.cines.api.modelo.Imagen;
import com.mynor.manejador.cines.api.modelo.Pago;
import com.mynor.manejador.cines.api.modelo.PagoCine;
import com.mynor.manejador.cines.api.modelo.Rol;
import com.mynor.manejador.cines.api.modelo.Usuario;
import java.sql.*;

/**
 *
 * @author mynordma
 */
public class PagoCineBD implements BaseDeDatos<PagoCine, FiltrosPagoCine> {
    
    @Override
    public PagoCine crear(PagoCine entidad, Connection conn) throws AccesoDeDatosException {
        String sql = """
            INSERT INTO PagoCine (cine, pago)
            VALUES (?, ?)
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entidad.getCine().getId());
            ps.setInt(2, entidad.getPago().getId());
            ps.executeUpdate();
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public PagoCine[] leer(FiltrosPagoCine filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder("""
            SELECT cine, pago
            FROM PagoCine
            WHERE 1=1
        """);
        
        if (filtros != null) {
            if (filtros.getIdCine().isPresent()) {
                sql.append(" AND cine = ?");
            }
            if (filtros.getIdPago().isPresent()) {
                sql.append(" AND pago = ?");
            }
        }
        
        try (PreparedStatement ps = conn.prepareStatement(sql.toString(),
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            
            int index = 1;
            if (filtros != null) {
                if (filtros.getIdCine().isPresent()) {
                    ps.setInt(index++, filtros.getIdCine().get());
                }
                if (filtros.getIdPago().isPresent()) {
                    ps.setInt(index++, filtros.getIdPago().get());
                }
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                int total = obtenerLongitudRS(rs);
                PagoCine[] pagosCine = new PagoCine[total];
                int i = 0;
                
                while (rs.next()) {
                    PagoCine pagoCine = new PagoCine();
                    
                    Cine cine = new Cine();
                    cine.setId(rs.getInt("cine"));
                    pagoCine.setCine(cine);
                    
                    Pago pago = new Pago();
                    pago.setId(rs.getInt("pago"));
                    pagoCine.setPago(pago);
                    
                    pagosCine[i++] = pagoCine;
                }
                return pagosCine;
            }
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public PagoCine[] leerCompleto(FiltrosPagoCine filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder("""
            SELECT 
                pc.cine,
                pc.pago,
                c.id as cine_id,
                c.usuario_creador as cine_usuario_creador,
                c.nombre as cine_nombre,
                c.ubicacion as cine_ubicacion,
                c.activado as cine_activado,
                p.id as pago_id,
                p.usuario as pago_usuario,
                p.fecha as pago_fecha,
                p.monto as pago_monto,
                u.id as usuario_id,
                u.imagen as usuario_imagen,
                u.nombre as usuario_nombre,
                u.rol as usuario_rol,
                u.correo as usuario_correo,
                u.clave as usuario_clave,
                u.activado as usuario_activado
            FROM PagoCine pc
            LEFT JOIN Cine c ON pc.cine = c.id
            LEFT JOIN Pago p ON pc.pago = p.id
            LEFT JOIN Usuario u ON p.usuario = u.id
            WHERE 1=1
        """);
        
        if (filtros != null) {
            if (filtros.getIdCine().isPresent()) {
                sql.append(" AND pc.cine = ?");
            }
            if (filtros.getIdPago().isPresent()) {
                sql.append(" AND pc.pago = ?");
            }
        }
        
        try (PreparedStatement ps = conn.prepareStatement(sql.toString(),
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            
            int index = 1;
            if (filtros != null) {
                if (filtros.getIdCine().isPresent()) {
                    ps.setInt(index++, filtros.getIdCine().get());
                }
                if (filtros.getIdPago().isPresent()) {
                    ps.setInt(index++, filtros.getIdPago().get());
                }
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                int total = obtenerLongitudRS(rs);
                PagoCine[] pagosCine = new PagoCine[total];
                int i = 0;
                
                while (rs.next()) {
                    PagoCine pagoCine = new PagoCine();
                    
                    if (rs.getObject("cine_id") != null) {
                        Cine cine = new Cine();
                        cine.setId(rs.getInt("cine_id"));
                        cine.setNombre(rs.getString("cine_nombre"));
                        cine.setUbicacion(rs.getString("cine_ubicacion"));
                        cine.setActivado(rs.getBoolean("cine_activado"));
                        
                        Usuario usuarioCreador = new Usuario();
                        usuarioCreador.setId(rs.getInt("cine_usuario_creador"));
                        cine.setUsuarioCreador(usuarioCreador);
                        
                        pagoCine.setCine(cine);
                    }
                    
                    if (rs.getObject("pago_id") != null) {
                        Pago pago = new Pago();
                        pago.setId(rs.getInt("pago_id"));
                        pago.setFecha(rs.getDate("pago_fecha").toLocalDate());
                        pago.setMonto(rs.getDouble("pago_monto"));
                        
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
                        
                        pagoCine.setPago(pago);
                    }
                    
                    pagosCine[i++] = pagoCine;
                }
                return pagosCine;
            }
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public PagoCine actualizar(PagoCine entidad, Connection conn) throws AccesoDeDatosException {
        throw new UnsupportedOperationException("No se puede actualizar una tabla asociativa. Usa eliminar y crear.");
    }

    @Override
    public PagoCine eliminar(PagoCine entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "DELETE FROM PagoCine WHERE cine = ? AND pago = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entidad.getCine().getId());
            ps.setInt(2, entidad.getPago().getId());
            ps.executeUpdate();
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }
   
}
