/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.bd;

import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.filtros.FiltrosCostoGlobalCines;
import com.mynor.manejador.cines.api.modelo.CostoGlobalCines;
import java.sql.*;

/**
 *
 * @author mynordma
 */
public class CostoGlobalCinesBD implements BaseDeDatos<CostoGlobalCines, FiltrosCostoGlobalCines> {
    
    @Override
    public CostoGlobalCines crear(CostoGlobalCines entidad, Connection conn) throws AccesoDeDatosException {
        String sql = """
            INSERT INTO CostoGlobalDiarioCines (monto)
            VALUES (?)
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, entidad.getMonto());
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
    public CostoGlobalCines[] leer(FiltrosCostoGlobalCines filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder("""
            SELECT id, monto
            FROM CostoGlobalDiarioCines
            WHERE 1=1
        """);
        
        if (filtros != null && filtros.getId().isPresent()) {
            sql.append(" AND id = ?");
        }
        
        try (PreparedStatement ps = conn.prepareStatement(sql.toString(),
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            
            int index = 1;
            if (filtros != null && filtros.getId().isPresent()) {
                ps.setInt(index++, filtros.getId().get());
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                int total = obtenerLongitudRS(rs);
                CostoGlobalCines[] costos = new CostoGlobalCines[total];
                int i = 0;
                
                while (rs.next()) {
                    CostoGlobalCines costo = new CostoGlobalCines();
                    costo.setId(rs.getInt("id"));
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
    public CostoGlobalCines[] leerCompleto(FiltrosCostoGlobalCines filtros, Connection conn) throws AccesoDeDatosException {
        return leer(filtros, conn);
    }

    @Override
    public CostoGlobalCines actualizar(CostoGlobalCines entidad, Connection conn) throws AccesoDeDatosException {
        String sql = """
            UPDATE CostoGlobalDiarioCines 
            SET monto = ?
            WHERE id = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, entidad.getMonto());
            ps.setInt(2, entidad.getId());
            ps.executeUpdate();
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public CostoGlobalCines eliminar(CostoGlobalCines entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "DELETE FROM CostoGlobalDiarioCines WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entidad.getId());
            ps.executeUpdate();
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }
}