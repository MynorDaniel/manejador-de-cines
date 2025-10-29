/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.bd;

import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.filtros.FiltrosTipoAnuncio;
import com.mynor.manejador.cines.api.modelo.TipoAnuncio;
import com.mynor.manejador.cines.api.modelo.TipoAnuncioPrecio;
import java.sql.*;

/**
 *
 * @author mynordma
 */
public class TipoAnuncioBD implements BaseDeDatos<TipoAnuncioPrecio, FiltrosTipoAnuncio> {

    @Override
    public TipoAnuncioPrecio crear(TipoAnuncioPrecio entidad, Connection conn) throws AccesoDeDatosException {
        String sql = """
            INSERT INTO TipoAnuncio (nombre, precio)
            VALUES (?, ?)
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entidad.getTipo().name());
            ps.setDouble(2, entidad.getMonto());
            ps.executeUpdate();
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al crear TipoAnuncio: " + e.getMessage());
        }
    }

    @Override
    public TipoAnuncioPrecio[] leer(FiltrosTipoAnuncio filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder("""
            SELECT nombre, precio
            FROM TipoAnuncio
            WHERE 1=1
        """);

        if (filtros != null && filtros.getTipo().isPresent()) {
            sql.append(" AND nombre = ?");
        }

        try (PreparedStatement ps = conn.prepareStatement(sql.toString(),
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            if (filtros != null && filtros.getTipo().isPresent()) {
                ps.setString(1, filtros.getTipo().get().name());
            }

            try (ResultSet rs = ps.executeQuery()) {
                int total = obtenerLongitudRS(rs);
                TipoAnuncioPrecio[] tipos = new TipoAnuncioPrecio[total];
                int i = 0;

                while (rs.next()) {
                    TipoAnuncioPrecio t = new TipoAnuncioPrecio();
                    t.setTipo(TipoAnuncio.valueOf(rs.getString("nombre")));
                    t.setMonto(rs.getDouble("precio"));
                    tipos[i++] = t;
                }

                return tipos;
            }

        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al leer TipoAnuncio: " + e.getMessage());
        }
    }

    @Override
    public TipoAnuncioPrecio[] leerCompleto(FiltrosTipoAnuncio filtros, Connection conn) throws AccesoDeDatosException {
        return leer(filtros, conn);
    }

    @Override
    public TipoAnuncioPrecio actualizar(TipoAnuncioPrecio entidad, Connection conn) throws AccesoDeDatosException {
        String sql = """
            UPDATE TipoAnuncio
            SET precio = ?
            WHERE nombre = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, entidad.getMonto());
            ps.setString(2, entidad.getTipo().name());
            ps.executeUpdate();
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al actualizar TipoAnuncio: " + e.getMessage());
        }
    }

    @Override
    public TipoAnuncioPrecio eliminar(TipoAnuncioPrecio entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "DELETE FROM TipoAnuncio WHERE nombre = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entidad.getTipo().name());
            ps.executeUpdate();
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al eliminar TipoAnuncio: " + e.getMessage());
        }
    }
}