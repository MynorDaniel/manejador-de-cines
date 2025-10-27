/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.bd;

import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.filtros.FiltrosCartera;
import com.mynor.manejador.cines.api.modelo.Cartera;
import com.mynor.manejador.cines.api.modelo.Imagen;
import com.mynor.manejador.cines.api.modelo.Rol;
import com.mynor.manejador.cines.api.modelo.Usuario;
import java.sql.Connection;
import java.sql.*;

/**
 *
 * @author mynordma
 */
public class CarteraBD implements BaseDeDatos<Cartera, FiltrosCartera> {
    
    @Override
    public Cartera crear(Cartera entidad, Connection conn) throws AccesoDeDatosException {
        String sql = """
            INSERT INTO Cartera (usuario, saldo)
            VALUES (?, ?)
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entidad.getUsuario().getId());
            ps.setDouble(2, entidad.getSaldo());
            ps.executeUpdate();
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public Cartera[] leer(FiltrosCartera filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder("""
            SELECT usuario, saldo 
            FROM Cartera 
            WHERE 1=1
        """);
        
        if (filtros != null && filtros.getIdUsuario().isPresent()) {
            sql.append(" AND usuario = ?");
        }
        
        try (PreparedStatement ps = conn.prepareStatement(sql.toString(),
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            
            int index = 1;
            if (filtros != null && filtros.getIdUsuario().isPresent()) {
                ps.setInt(index++, filtros.getIdUsuario().get());
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                int total = obtenerLongitudRS(rs);
                Cartera[] carteras = new Cartera[total];
                int i = 0;
                
                while (rs.next()) {
                    Cartera cartera = new Cartera();
                    
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("usuario"));
                    cartera.setUsuario(usuario);
                    
                    cartera.setSaldo(rs.getDouble("saldo"));
                    carteras[i++] = cartera;
                }
                return carteras;
            }
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public Cartera[] leerCompleto(FiltrosCartera filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder("""
            SELECT 
                c.usuario,
                c.saldo,
                u.id,
                u.imagen,
                u.nombre,
                u.rol,
                u.correo,
                u.clave,
                u.activado
            FROM Cartera c
            INNER JOIN Usuario u ON c.usuario = u.id
            WHERE 1=1
        """);
        
        if (filtros != null && filtros.getIdUsuario().isPresent()) {
            sql.append(" AND c.usuario = ?");
        }
        
        try (PreparedStatement ps = conn.prepareStatement(sql.toString(),
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            
            int index = 1;
            if (filtros != null && filtros.getIdUsuario().isPresent()) {
                ps.setInt(index++, filtros.getIdUsuario().get());
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                int total = obtenerLongitudRS(rs);
                Cartera[] carteras = new Cartera[total];
                int i = 0;
                
                while (rs.next()) {
                    Cartera cartera = new Cartera();
                    
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    
                    Imagen img = new Imagen();
                    img.setId(rs.getInt("imagen"));
                    usuario.setImagen(img);
                    
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setRol(Rol.valueOf(rs.getString("rol")));
                    usuario.setCorreo(rs.getString("correo"));
                    usuario.setClave(rs.getString("clave"));
                    usuario.setActivado(rs.getBoolean("activado"));
                    
                    cartera.setUsuario(usuario);
                    cartera.setSaldo(rs.getDouble("saldo"));
                    carteras[i++] = cartera;
                }
                return carteras;
            }
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public Cartera actualizar(Cartera entidad, Connection conn) throws AccesoDeDatosException {
        String sql = """
            UPDATE Cartera 
            SET saldo = ?
            WHERE usuario = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, entidad.getSaldo());
            ps.setInt(2, entidad.getUsuario().getId());
            ps.executeUpdate();
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public Cartera eliminar(Cartera entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "DELETE FROM Cartera WHERE usuario = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entidad.getUsuario().getId());
            ps.executeUpdate();
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }
}
