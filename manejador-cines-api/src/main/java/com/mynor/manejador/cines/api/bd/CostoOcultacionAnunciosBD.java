/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.bd;

import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.filtros.FiltrosCostoOcultacionAnuncios;
import com.mynor.manejador.cines.api.modelo.CostoOcultacionAnuncios;
import java.sql.*;

/**
 *
 * @author mynordma
 */
public class CostoOcultacionAnunciosBD implements BaseDeDatos<CostoOcultacionAnuncios, FiltrosCostoOcultacionAnuncios> {
    
    @Override
    public CostoOcultacionAnuncios crear(CostoOcultacionAnuncios entidad, Connection conn) throws AccesoDeDatosException {
        String sql = """
            INSERT INTO CostoOcultacionAnuncios (dias, monto)
            VALUES (?, ?)
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entidad.getDias());
            ps.setDouble(2, entidad.getMonto());
            ps.executeUpdate();
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public CostoOcultacionAnuncios[] leer(FiltrosCostoOcultacionAnuncios filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder("""
            SELECT dias, monto
            FROM CostoOcultacionAnuncios
            WHERE 1=1
        """);
        
        if (filtros != null && filtros.getDias().isPresent()) {
            sql.append(" AND dias = ?");
        }
        
        try (PreparedStatement ps = conn.prepareStatement(sql.toString(),
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            
            int index = 1;
            if (filtros != null && filtros.getDias().isPresent()) {
                ps.setInt(index++, filtros.getDias().get());
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                int total = obtenerLongitudRS(rs);
                CostoOcultacionAnuncios[] costos = new CostoOcultacionAnuncios[total];
                int i = 0;
                
                while (rs.next()) {
                    CostoOcultacionAnuncios costo = new CostoOcultacionAnuncios();
                    costo.setDias(rs.getInt("dias"));
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
    public CostoOcultacionAnuncios[] leerCompleto(FiltrosCostoOcultacionAnuncios filtros, Connection conn) throws AccesoDeDatosException {
        return leer(filtros, conn);
    }

    @Override
    public CostoOcultacionAnuncios actualizar(CostoOcultacionAnuncios entidad, Connection conn) throws AccesoDeDatosException {
        String sql = """
            UPDATE CostoOcultacionAnuncios 
            SET monto = ?
            WHERE dias = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, entidad.getMonto());
            ps.setInt(2, entidad.getDias());
            ps.executeUpdate();
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public CostoOcultacionAnuncios eliminar(CostoOcultacionAnuncios entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "DELETE FROM CostoOcultacionAnuncios WHERE dias = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entidad.getDias());
            ps.executeUpdate();
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }
}
