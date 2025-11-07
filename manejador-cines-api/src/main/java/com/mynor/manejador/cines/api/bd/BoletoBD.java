/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.bd;

import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.filtros.FiltrosBoleto;
import com.mynor.manejador.cines.api.modelo.Boleto;
import com.mynor.manejador.cines.api.modelo.Pago;
import com.mynor.manejador.cines.api.modelo.Proyeccion;
import com.mynor.manejador.cines.api.modelo.Usuario;
import java.sql.*;

/**
 *
 * @author mynordma
 */
public class BoletoBD implements BaseDeDatos<Boleto, FiltrosBoleto> {

    @Override
    public Boleto crear(Boleto entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "INSERT INTO Boleto (usuario, proyeccion, pago) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setObject(1, entidad.getUsuario() != null ? entidad.getUsuario().getId() : null, Types.INTEGER);
            stmt.setObject(2, entidad.getProyeccion() != null ? entidad.getProyeccion().getId() : null, Types.INTEGER);
            stmt.setObject(3, entidad.getPago() != null ? entidad.getPago().getId() : null, Types.INTEGER);
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    entidad.setId(rs.getInt(1));
                }
            }
            
            return entidad;
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al crear boleto: " + e.getMessage());
        }
    }

    @Override
    public Boleto[] leer(FiltrosBoleto filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder(
            "SELECT b.id, b.usuario, b.proyeccion, b.pago " +
            "FROM Boleto b " +
            "WHERE 1=1"
        );
        
        if (filtros.getId().isPresent()) {
            sql.append(" AND b.id = ?");
        }
        if (filtros.getIdUsuario().isPresent()) {
            sql.append(" AND b.usuario = ?");
        }
        if (filtros.getIdSala().isPresent()) {
            sql.append(" AND b.proyeccion IN (SELECT id FROM Proyeccion WHERE sala = ?)");
        }
        
        sql.append(" ORDER BY b.id DESC");
        
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
            if (filtros.getIdSala().isPresent()) {
                stmt.setInt(paramIndex++, filtros.getIdSala().get());
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                int longitud = obtenerLongitudRS(rs);
                Boleto[] boletos = new Boleto[longitud];
                int index = 0;
                
                while (rs.next()) {
                    boletos[index++] = mapearBoletoBasico(rs);
                }
                
                return boletos;
            }
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al leer boletos: " + e.getMessage());
        }
    }

    @Override
    public Boleto[] leerCompleto(FiltrosBoleto filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder(
            "SELECT " +
            "b.id as boleto_id, " +
            "b.usuario as boleto_usuario, " +
            "b.proyeccion as boleto_proyeccion, " +
            "b.pago as boleto_pago, " +
            "u.id as usuario_id, u.imagen as usuario_imagen, u.nombre as usuario_nombre, " +
            "u.rol as usuario_rol, u.correo as usuario_correo, u.activado as usuario_activado, " +
            "p.id as proyeccion_id, p.sala as proyeccion_sala, p.pelicula as proyeccion_pelicula, " +
            "p.fecha as proyeccion_fecha, p.hora as proyeccion_hora, " +
            "pg.id as pago_id, pg.usuario as pago_usuario, pg.fecha as pago_fecha, pg.monto as pago_monto " +
            "FROM Boleto b " +
            "LEFT JOIN Usuario u ON b.usuario = u.id " +
            "LEFT JOIN Proyeccion p ON b.proyeccion = p.id " +
            "LEFT JOIN Pago pg ON b.pago = pg.id " +
            "WHERE 1=1"
        );
        
        if (filtros.getId().isPresent()) {
            sql.append(" AND b.id = ?");
        }
        if (filtros.getIdUsuario().isPresent()) {
            sql.append(" AND b.usuario = ?");
        }
        if (filtros.getIdSala().isPresent()) {
            sql.append(" AND p.sala = ?");
        }
        
        sql.append(" ORDER BY b.id DESC");
        
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
            if (filtros.getIdSala().isPresent()) {
                stmt.setInt(paramIndex++, filtros.getIdSala().get());
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                int longitud = obtenerLongitudRS(rs);
                Boleto[] boletos = new Boleto[longitud];
                int index = 0;
                
                while (rs.next()) {
                    boletos[index++] = mapearBoletoCompleto(rs);
                }
                
                return boletos;
            }
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al leer boletos completos: " + e.getMessage());
        }
    }

    @Override
    public Boleto actualizar(Boleto entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "UPDATE Boleto SET usuario = ?, proyeccion = ?, pago = ? WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setObject(1, entidad.getUsuario() != null ? entidad.getUsuario().getId() : null, Types.INTEGER);
            stmt.setObject(2, entidad.getProyeccion() != null ? entidad.getProyeccion().getId() : null, Types.INTEGER);
            stmt.setObject(3, entidad.getPago() != null ? entidad.getPago().getId() : null, Types.INTEGER);
            stmt.setInt(4, entidad.getId());
            
            int filasActualizadas = stmt.executeUpdate();
            
            if (filasActualizadas == 0) {
                throw new AccesoDeDatosException("No se encontró el boleto con id: " + entidad.getId());
            }
            
            return entidad;
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al actualizar boleto: " + e.getMessage());
        }
    }

    @Override
    public Boleto eliminar(Boleto entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "DELETE FROM Boleto WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, entidad.getId());
            
            int filasEliminadas = stmt.executeUpdate();
            
            if (filasEliminadas == 0) {
                throw new AccesoDeDatosException("No se encontró el boleto con id: " + entidad.getId());
            }
            
            return entidad;
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al eliminar boleto: " + e.getMessage());
        }
    }
    
    private Boleto mapearBoletoBasico(ResultSet rs) throws SQLException {
        Boleto boleto = new Boleto();
        boleto.setId(rs.getInt("id"));
        
        if (rs.getObject("usuario") != null) {
            Usuario usuario = new Usuario();
            usuario.setId(rs.getInt("usuario"));
            boleto.setUsuario(usuario);
        }
        
        if (rs.getObject("proyeccion") != null) {
            Proyeccion proyeccion = new Proyeccion();
            proyeccion.setId(rs.getInt("proyeccion"));
            boleto.setProyeccion(proyeccion);
        }
        
        if (rs.getObject("pago") != null) {
            Pago pago = new Pago();
            pago.setId(rs.getInt("pago"));
            boleto.setPago(pago);
        }
        
        return boleto;
    }
    
    private Boleto mapearBoletoCompleto(ResultSet rs) throws SQLException {
        Boleto boleto = new Boleto();
        boleto.setId(rs.getInt("boleto_id"));
        
        if (rs.getObject("usuario_id") != null) {
            Usuario usuario = new Usuario();
            usuario.setId(rs.getInt("usuario_id"));
            usuario.setNombre(rs.getString("usuario_nombre"));
            usuario.setCorreo(rs.getString("usuario_correo"));
            usuario.setActivado(rs.getBoolean("usuario_activado"));
            
            if (rs.getObject("usuario_imagen") != null) {
                usuario.setImagen(new com.mynor.manejador.cines.api.modelo.Imagen());
                usuario.getImagen().setId(rs.getInt("usuario_imagen"));
            }
            
            boleto.setUsuario(usuario);
        }
        
        if (rs.getObject("proyeccion_id") != null) {
            Proyeccion proyeccion = new Proyeccion();
            proyeccion.setId(rs.getInt("proyeccion_id"));
            proyeccion.setFecha(rs.getDate("proyeccion_fecha").toLocalDate());
            proyeccion.setHora(rs.getTime("proyeccion_hora").toLocalTime());
            
            boleto.setProyeccion(proyeccion);
        }
        
        if (rs.getObject("pago_id") != null) {
            Pago pago = new Pago();
            pago.setId(rs.getInt("pago_id"));
            pago.setFecha(rs.getDate("pago_fecha").toLocalDate());
            pago.setMonto(rs.getDouble("pago_monto"));
            
            if (rs.getObject("pago_usuario") != null) {
                Usuario usuarioPago = new Usuario();
                usuarioPago.setId(rs.getInt("pago_usuario"));
                pago.setUsuario(usuarioPago);
            }
            
            boleto.setPago(pago);
        }
        
        return boleto;
    }
}
