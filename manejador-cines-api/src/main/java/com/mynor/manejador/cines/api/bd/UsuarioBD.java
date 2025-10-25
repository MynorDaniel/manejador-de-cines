/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.bd;

import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.filtros.FiltrosUsuario;
import com.mynor.manejador.cines.api.modelo.Imagen;
import com.mynor.manejador.cines.api.modelo.Rol;
import com.mynor.manejador.cines.api.modelo.Usuario;
import java.sql.*;

/**
 *
 * @author mynordma
 */
public class UsuarioBD implements BaseDeDatos<Usuario, FiltrosUsuario>{
    
    @Override
    public Usuario crear(Usuario usuario, Connection conn) throws AccesoDeDatosException {
        String sql = """
            INSERT INTO Usuario (nombre, rol, correo, clave, imagen)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getRol().name());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getClave());
            ps.setInt(5, 1);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getInt(1));
                }
            }

            return usuario;

        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public Usuario[] leer(FiltrosUsuario filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder("""
            SELECT id, imagen, nombre, rol, correo, clave, activado
            FROM Usuario
            WHERE 1=1
        """);

        if (filtros != null) {
            if (filtros.getId().isPresent()) {
                sql.append(" AND id = ?");
            }
            if (filtros.getCorreo().isPresent()) {
                sql.append(" AND correo = ?");
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
                if (filtros.getCorreo().isPresent()) {
                    ps.setString(index++, filtros.getCorreo().get());
                }
                if (filtros.getActivado().isPresent()) {
                    ps.setBoolean(index++, filtros.getActivado().get());
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                int total = obtenerLongitudRS(rs);
                Usuario[] usuarios = new Usuario[total];
                int i = 0;

                while (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id"));

                    Imagen img = new Imagen();
                    img.setId(rs.getInt("imagen"));
                    u.setImagen(img);

                    u.setNombre(rs.getString("nombre"));
                    u.setRol(Rol.valueOf(rs.getString("rol")));
                    u.setCorreo(rs.getString("correo"));
                    u.setClave(rs.getString("clave"));
                    u.setActivado(rs.getBoolean("activado"));

                    usuarios[i++] = u;
                }

                return usuarios;
            }

        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }


    @Override
    public Usuario[] leerCompleto(FiltrosUsuario filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder("""
            SELECT u.id, u.nombre, u.rol, u.correo, u.clave, u.activado,
                   i.id AS imagen_id, i.link AS imagen_link
            FROM Usuario u
            LEFT JOIN Imagen i ON u.imagen = i.id
            WHERE 1=1
        """);

        if (filtros != null) {
            if (filtros.getId().isPresent()) {
                sql.append(" AND u.id = ?");
            }
            if (filtros.getCorreo().isPresent()) {
                sql.append(" AND u.correo = ?");
            }
            if (filtros.getActivado().isPresent()) {
                sql.append(" AND u.activado = ?");
            }
        }

        try (PreparedStatement ps = conn.prepareStatement(sql.toString(),
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {

            int i = 1;
            if (filtros != null) {
                if (filtros.getId().isPresent()) {
                    ps.setInt(i++, filtros.getId().get());
                }
                if (filtros.getCorreo().isPresent()) {
                    ps.setString(i++, filtros.getCorreo().get());
                }
                if (filtros.getActivado().isPresent()) {
                    ps.setBoolean(i++, filtros.getActivado().get());
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                Usuario[] usuarios = new Usuario[obtenerLongitudRS(rs)];
                int j = 0;

                while (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id"));
                    u.setNombre(rs.getString("nombre"));
                    u.setRol(Rol.valueOf(rs.getString("rol")));
                    u.setCorreo(rs.getString("correo"));
                    u.setClave(rs.getString("clave"));
                    u.setActivado(rs.getBoolean("activado"));

                    Imagen img = new Imagen();
                    img.setId(rs.getInt("imagen_id"));
                    img.setLink(rs.getString("imagen_link"));
                    u.setImagen(img);

                    usuarios[j++] = u;
                }

                return usuarios;
            }

        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }


    @Override
    public Usuario actualizar(Usuario usuario, Connection conn) throws AccesoDeDatosException {
        String sql = """
            UPDATE Usuario
            SET imagen = ?, nombre = ?, correo = ?, clave = ?, activado = ?
            WHERE id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, usuario.getImagen().getId());
            ps.setString(2, usuario.getNombre());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getClave());
            ps.setBoolean(5, usuario.getActivado());
            ps.setInt(6, usuario.getId());

            ps.executeUpdate();
            return usuario;

        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public Usuario eliminar(Usuario usuario, Connection conn) throws AccesoDeDatosException {
        String sql = "UPDATE Usuario SET activado = 0 WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuario.getId());
            ps.executeUpdate();
            usuario.setActivado(false);
            return usuario;
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }
    
}
