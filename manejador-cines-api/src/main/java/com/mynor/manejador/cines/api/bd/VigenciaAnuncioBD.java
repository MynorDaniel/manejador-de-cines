/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.bd;

import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.filtros.FiltrosVigenciaAnuncio;
import com.mynor.manejador.cines.api.modelo.VigenciaAnuncio;
import java.sql.*;

/**
 *
 * @author mynordma
 */
public class VigenciaAnuncioBD implements BaseDeDatos<VigenciaAnuncio, FiltrosVigenciaAnuncio> {

    @Override
    public VigenciaAnuncio crear(VigenciaAnuncio entidad, Connection conn) throws AccesoDeDatosException {
        String sql = """
            INSERT INTO VigenciaAnuncio (dias, precio)
            VALUES (?, ?)
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entidad.getDias());
            ps.setDouble(2, entidad.getMonto());
            ps.executeUpdate();
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al crear VigenciaAnuncio: " + e.getMessage());
        }
    }

    @Override
    public VigenciaAnuncio[] leer(FiltrosVigenciaAnuncio filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder("""
            SELECT dias, precio
            FROM VigenciaAnuncio
            WHERE 1=1
        """);

        if (filtros != null && filtros.getDias().isPresent()) {
            sql.append(" AND dias = ?");
        }

        try (PreparedStatement ps = conn.prepareStatement(sql.toString(),
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            if (filtros != null && filtros.getDias().isPresent()) {
                ps.setInt(1, filtros.getDias().get());
            }

            try (ResultSet rs = ps.executeQuery()) {
                int total = obtenerLongitudRS(rs);
                VigenciaAnuncio[] vigencias = new VigenciaAnuncio[total];
                int i = 0;

                while (rs.next()) {
                    VigenciaAnuncio v = new VigenciaAnuncio();
                    v.setDias(rs.getInt("dias"));
                    v.setMonto(rs.getDouble("precio"));
                    vigencias[i++] = v;
                }

                return vigencias;
            }

        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al leer VigenciaAnuncio: " + e.getMessage());
        }
    }

    @Override
    public VigenciaAnuncio[] leerCompleto(FiltrosVigenciaAnuncio filtros, Connection conn) throws AccesoDeDatosException {
        return leer(filtros, conn);
    }

    @Override
    public VigenciaAnuncio actualizar(VigenciaAnuncio entidad, Connection conn) throws AccesoDeDatosException {
        String sql = """
            UPDATE VigenciaAnuncio
            SET precio = ?
            WHERE dias = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, entidad.getMonto());
            ps.setInt(2, entidad.getDias());
            ps.executeUpdate();
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al actualizar VigenciaAnuncio: " + e.getMessage());
        }
    }

    @Override
    public VigenciaAnuncio eliminar(VigenciaAnuncio entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "DELETE FROM VigenciaAnuncio WHERE dias = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entidad.getDias());
            ps.executeUpdate();
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al eliminar VigenciaAnuncio: " + e.getMessage());
        }
    }
}

