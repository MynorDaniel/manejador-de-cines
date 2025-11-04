/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.bd;

import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.filtros.FiltrosBloqueoAnunciosCine;
import com.mynor.manejador.cines.api.modelo.BloqueoAnunciosCine;
import com.mynor.manejador.cines.api.modelo.Cine;
import com.mynor.manejador.cines.api.modelo.Imagen;
import com.mynor.manejador.cines.api.modelo.Pago;
import com.mynor.manejador.cines.api.modelo.Rol;
import com.mynor.manejador.cines.api.modelo.Usuario;
import java.sql.*;

/**
 *
 * @author mynordma
 */
public class BloqueoAnunciosCineBD implements BaseDeDatos<BloqueoAnunciosCine, FiltrosBloqueoAnunciosCine> {
    
    @Override
    public BloqueoAnunciosCine crear(BloqueoAnunciosCine entidad, Connection conn) throws AccesoDeDatosException {
        String sql = """
            INSERT INTO BloqueoAnunciosCine (cine, pago, dias)
            VALUES (?, ?, ?)
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entidad.getCine().getId());
            ps.setInt(2, entidad.getPago().getId());
            ps.setInt(3, entidad.getDias());
            ps.executeUpdate();
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public BloqueoAnunciosCine[] leer(FiltrosBloqueoAnunciosCine filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder("""
            SELECT cine, pago, dias
            FROM BloqueoAnunciosCine
            WHERE 1=1
        """);
        
        if (filtros != null && filtros.getIdCine().isPresent()) {
            sql.append(" AND cine = ?");
        }
        
        try (PreparedStatement ps = conn.prepareStatement(sql.toString(),
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            
            int index = 1;
            if (filtros != null && filtros.getIdCine().isPresent()) {
                ps.setInt(index++, filtros.getIdCine().get());
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                int total = obtenerLongitudRS(rs);
                BloqueoAnunciosCine[] bloqueos = new BloqueoAnunciosCine[total];
                int i = 0;
                
                while (rs.next()) {
                    BloqueoAnunciosCine bloqueo = new BloqueoAnunciosCine();
                    
                    Cine cine = new Cine();
                    cine.setId(rs.getInt("cine"));
                    bloqueo.setCine(cine);
                    
                    Pago pago = new Pago();
                    pago.setId(rs.getInt("pago"));
                    bloqueo.setPago(pago);
                    
                    bloqueo.setDias(rs.getInt("dias"));
                    
                    bloqueos[i++] = bloqueo;
                }
                return bloqueos;
            }
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public BloqueoAnunciosCine[] leerCompleto(FiltrosBloqueoAnunciosCine filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder("""
            SELECT 
                bac.cine,
                bac.pago,
                bac.dias,
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
            FROM BloqueoAnunciosCine bac
            LEFT JOIN Cine c ON bac.cine = c.id
            LEFT JOIN Pago p ON bac.pago = p.id
            LEFT JOIN Usuario u ON p.usuario = u.id
            WHERE 1=1
        """);
        
        if (filtros != null && filtros.getIdCine().isPresent()) {
            sql.append(" AND bac.cine = ?");
        }
        
        try (PreparedStatement ps = conn.prepareStatement(sql.toString(),
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            
            int index = 1;
            if (filtros != null && filtros.getIdCine().isPresent()) {
                ps.setInt(index++, filtros.getIdCine().get());
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                int total = obtenerLongitudRS(rs);
                BloqueoAnunciosCine[] bloqueos = new BloqueoAnunciosCine[total];
                int i = 0;
                
                while (rs.next()) {
                    BloqueoAnunciosCine bloqueo = new BloqueoAnunciosCine();
                    bloqueo.setDias(rs.getInt("dias"));
                    
                    if (rs.getObject("cine_id") != null) {
                        Cine cine = new Cine();
                        cine.setId(rs.getInt("cine_id"));
                        cine.setNombre(rs.getString("cine_nombre"));
                        cine.setUbicacion(rs.getString("cine_ubicacion"));
                        cine.setActivado(rs.getBoolean("cine_activado"));
                        
                        Usuario usuarioCreador = new Usuario();
                        usuarioCreador.setId(rs.getInt("cine_usuario_creador"));
                        cine.setUsuarioCreador(usuarioCreador);
                        
                        bloqueo.setCine(cine);
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
                        
                        bloqueo.setPago(pago);
                    }
                    
                    bloqueos[i++] = bloqueo;
                }
                return bloqueos;
            }
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public BloqueoAnunciosCine actualizar(BloqueoAnunciosCine entidad, Connection conn) throws AccesoDeDatosException {
        String sql = """
            UPDATE BloqueoAnunciosCine 
            SET dias = ?
            WHERE cine = ? AND pago = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entidad.getDias());
            ps.setInt(2, entidad.getCine().getId());
            ps.setInt(3, entidad.getPago().getId());
            ps.executeUpdate();
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public BloqueoAnunciosCine eliminar(BloqueoAnunciosCine entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "DELETE FROM BloqueoAnunciosCine WHERE cine = ? AND pago = ?";
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
