/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.bd;

import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.filtros.FiltrosCategoria;
import com.mynor.manejador.cines.api.modelo.Categoria;
import java.sql.*;

/**
 *
 * @author mynordma
 */
public class CategoriaBD implements BaseDeDatos<Categoria, FiltrosCategoria> {

    @Override
    public Categoria crear(Categoria entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "INSERT INTO Categoria (nombre) VALUES (?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, entidad.getNombre());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    entidad.setId(rs.getInt(1));
                }
            }
            
            return entidad;
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al crear categoría");
        }
    }

    @Override
    public Categoria[] leer(FiltrosCategoria filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder(
            "SELECT id, nombre FROM Categoria WHERE 1=1"
        );
        
        if (filtros.getId().isPresent()) {
            sql.append(" AND id = ?");
        }
        
        sql.append(" ORDER BY nombre");
        
        try (PreparedStatement stmt = conn.prepareStatement(sql.toString(),
                                                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                            ResultSet.CONCUR_READ_ONLY)) {
            int paramIndex = 1;
            
            if (filtros.getId().isPresent()) {
                stmt.setInt(paramIndex++, filtros.getId().get());
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                int longitud = obtenerLongitudRS(rs);
                Categoria[] categorias = new Categoria[longitud];
                int index = 0;
                
                while (rs.next()) {
                    Categoria categoria = new Categoria();
                    categoria.setId(rs.getInt("id"));
                    categoria.setNombre(rs.getString("nombre"));
                    
                    categorias[index++] = categoria;
                }
                
                return categorias;
            }
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al leer categorías");
        }
    }

    @Override
    public Categoria[] leerCompleto(FiltrosCategoria filtros, Connection conn) throws AccesoDeDatosException {
        return leer(filtros, conn);
    }

    @Override
    public Categoria actualizar(Categoria entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "UPDATE Categoria SET nombre = ? WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entidad.getNombre());
            stmt.setInt(2, entidad.getId());
            
            int filasActualizadas = stmt.executeUpdate();
            
            if (filasActualizadas == 0) {
                throw new AccesoDeDatosException("No se encontró la categoría con id: " + entidad.getId());
            }
            
            return entidad;
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al actualizar categoría");
        }
    }

    @Override
    public Categoria eliminar(Categoria entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "DELETE FROM Categoria WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, entidad.getId());
            
            int filasEliminadas = stmt.executeUpdate();
            
            if (filasEliminadas == 0) {
                throw new AccesoDeDatosException("No se encontró la categoría con id: " + entidad.getId());
            }
            
            return entidad;
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al eliminar categoría");
        }
    }
}