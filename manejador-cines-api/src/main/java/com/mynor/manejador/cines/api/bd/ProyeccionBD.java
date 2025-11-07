/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.bd;

import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.filtros.FiltrosPelicula;
import com.mynor.manejador.cines.api.filtros.FiltrosProyeccion;
import com.mynor.manejador.cines.api.filtros.FiltrosSala;
import com.mynor.manejador.cines.api.modelo.Pelicula;
import com.mynor.manejador.cines.api.modelo.Proyeccion;
import com.mynor.manejador.cines.api.modelo.Sala;
import java.sql.*;
import java.util.Optional;

/**
 *
 * @author mynordma
 */
public class ProyeccionBD implements BaseDeDatos<Proyeccion, FiltrosProyeccion> {

    private final SalaBD SALA_BD = new SalaBD();
    private final PeliculaBD PELICULA_BD = new PeliculaBD();

    @Override
    public Proyeccion crear(Proyeccion entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "INSERT INTO Proyeccion (sala, pelicula, fecha, hora) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, entidad.getSala().getId());
            stmt.setInt(2, entidad.getPelicula().getId());
            stmt.setDate(3, Date.valueOf(entidad.getFecha()));
            stmt.setTime(4, Time.valueOf(entidad.getHora()));
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new AccesoDeDatosException("Error al crear la proyección");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entidad.setId(generatedKeys.getInt(1));
                } else {
                    throw new AccesoDeDatosException("Error al obtener el ID de la proyección creada");
                }
            }
            
            return entidad;
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al crear proyección: " + e.getMessage());
        }
    }

    @Override
    public Proyeccion[] leer(FiltrosProyeccion filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder("SELECT * FROM Proyeccion WHERE 1=1");
        int contadorParametros = 0;
        
        if (filtros.getId().isPresent()) {
            sql.append(" AND id = ?");
            contadorParametros++;
        }
        
        if (filtros.getIdSala().isPresent()) {
            sql.append(" AND sala = ?");
            contadorParametros++;
        }
        
        if (filtros.getIdPelicula().isPresent()) {
            sql.append(" AND pelicula = ?");
            contadorParametros++;
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(sql.toString(), 
                ResultSet.TYPE_SCROLL_INSENSITIVE, 
                ResultSet.CONCUR_READ_ONLY)) {
            
            int index = 1;
            
            if (filtros.getId().isPresent()) {
                stmt.setInt(index++, filtros.getId().get());
            }
            
            if (filtros.getIdSala().isPresent()) {
                stmt.setInt(index++, filtros.getIdSala().get());
            }
            
            if (filtros.getIdPelicula().isPresent()) {
                stmt.setInt(index++, filtros.getIdPelicula().get());
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                int longitud = obtenerLongitudRS(rs);
                Proyeccion[] proyecciones = new Proyeccion[longitud];
                
                int i = 0;
                while (rs.next()) {
                    Proyeccion proyeccion = new Proyeccion();
                    proyeccion.setId(rs.getInt("id"));
                    proyeccion.setFecha(rs.getDate("fecha").toLocalDate());
                    proyeccion.setHora(rs.getTime("hora").toLocalTime());
                    proyeccion.setPrecio(rs.getDouble("precio"));
                    
                    Sala sala = new Sala();
                    sala.setId(rs.getInt("sala"));
                    proyeccion.setSala(sala);
                    
                    Pelicula pelicula = new Pelicula();
                    pelicula.setId(rs.getInt("pelicula"));
                    proyeccion.setPelicula(pelicula);
                    
                    proyecciones[i++] = proyeccion;
                }
                
                return proyecciones;
            }
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al leer proyecciones: " + e.getMessage());
        }
    }

    @Override
    public Proyeccion[] leerCompleto(FiltrosProyeccion filtros, Connection conn) throws AccesoDeDatosException {
        Proyeccion[] proyecciones = leer(filtros, conn);
        
        for (Proyeccion proyeccion : proyecciones) {
            try {
                FiltrosSala filtrosSala = new FiltrosSala();
                filtrosSala.setId(Optional.of(proyeccion.getSala().getId()));
                Sala[] salas = SALA_BD.leerCompleto(filtrosSala, conn);
                if (salas.length > 0) {
                    proyeccion.setSala(salas[0]);
                }
                FiltrosPelicula filtrosPelicula = new FiltrosPelicula();
                filtrosPelicula.setId(Optional.of(proyeccion.getPelicula().getId()));
                Pelicula[] peliculas = PELICULA_BD.leerCompleto(filtrosPelicula, conn);
                if (peliculas.length > 0) {
                    proyeccion.setPelicula(peliculas[0]);
                }
            } catch (AccesoDeDatosException e) {
                System.err.println("Error al cargar datos completos de proyección " + proyeccion.getId() + ": " + e.getMessage());
            }
        }
        
        return proyecciones;
    }

    @Override
    public Proyeccion actualizar(Proyeccion entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "UPDATE Proyeccion SET sala = ?, pelicula = ?, fecha = ?, hora = ? WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, entidad.getSala().getId());
            stmt.setInt(2, entidad.getPelicula().getId());
            stmt.setDate(3, Date.valueOf(entidad.getFecha()));
            stmt.setTime(4, Time.valueOf(entidad.getHora()));
            stmt.setInt(5, entidad.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new AccesoDeDatosException("No se encontró la proyección con ID: " + entidad.getId());
            }
            
            return entidad;
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al actualizar proyección: " + e.getMessage());
        }
    }

    @Override
    public Proyeccion eliminar(Proyeccion entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "DELETE FROM Proyeccion WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, entidad.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new AccesoDeDatosException("No se encontró la proyección con ID: " + entidad.getId());
            }
            
            return entidad;
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al eliminar proyección: " + e.getMessage());
        }
    }
}