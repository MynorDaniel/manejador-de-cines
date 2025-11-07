/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.bd;

import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.filtros.FiltrosClasificacion;
import com.mynor.manejador.cines.api.modelo.Clasificacion;
import java.sql.*;

/**
 *
 * @author mynordma
 */
public class ClasificacionBD implements BaseDeDatos<Clasificacion, FiltrosClasificacion> {

    @Override
    public Clasificacion crear(Clasificacion entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "INSERT INTO Clasificacion (codigo, descripcion) VALUES (?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entidad.getCodigo());
            stmt.setString(2, entidad.getDescripcion());
            
            stmt.executeUpdate();
            
            return entidad;
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al crear clasificación");
        }
    }

    @Override
    public Clasificacion[] leer(FiltrosClasificacion filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder(
            "SELECT codigo, descripcion FROM Clasificacion WHERE 1=1"
        );
        
        if (filtros.getCodigo().isPresent()) {
            sql.append(" AND codigo = ?");
        }
        
        sql.append(" ORDER BY codigo");
        
        try (PreparedStatement stmt = conn.prepareStatement(sql.toString(),
                                                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                            ResultSet.CONCUR_READ_ONLY)) {
            int paramIndex = 1;
            
            if (filtros.getCodigo().isPresent()) {
                stmt.setString(paramIndex++, filtros.getCodigo().get());
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                int longitud = obtenerLongitudRS(rs);
                Clasificacion[] clasificaciones = new Clasificacion[longitud];
                int index = 0;
                
                while (rs.next()) {
                    Clasificacion clasificacion = new Clasificacion();
                    clasificacion.setCodigo(rs.getString("codigo"));
                    clasificacion.setDescripcion(rs.getString("descripcion"));
                    
                    clasificaciones[index++] = clasificacion;
                }
                
                return clasificaciones;
            }
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al leer clasificaciones");
        }
    }

    @Override
    public Clasificacion[] leerCompleto(FiltrosClasificacion filtros, Connection conn) throws AccesoDeDatosException {
        return leer(filtros, conn);
    }

    @Override
    public Clasificacion actualizar(Clasificacion entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "UPDATE Clasificacion SET descripcion = ? WHERE codigo = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entidad.getDescripcion());
            stmt.setString(2, entidad.getCodigo());
            
            int filasActualizadas = stmt.executeUpdate();
            
            if (filasActualizadas == 0) {
                throw new AccesoDeDatosException("No se encontró la clasificación con código: " + entidad.getCodigo());
            }
            
            return entidad;
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al actualizar clasificación");
        }
    }

    @Override
    public Clasificacion eliminar(Clasificacion entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "DELETE FROM Clasificacion WHERE codigo = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entidad.getCodigo());
            
            int filasEliminadas = stmt.executeUpdate();
            
            if (filasEliminadas == 0) {
                throw new AccesoDeDatosException("No se encontró la clasificación con código: " + entidad.getCodigo());
            }
            
            return entidad;
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al eliminar clasificación");
        }
    }
}
