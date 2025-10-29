/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.bd;

import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.filtros.FiltrosVideo;
import com.mynor.manejador.cines.api.modelo.Video;
import java.sql.*;

/**
 *
 * @author mynordma
 */
public class VideoBD implements BaseDeDatos<Video, FiltrosVideo> {
    
    @Override
    public Video crear(Video entidad, Connection conn) throws AccesoDeDatosException {
        String sql = """
            INSERT INTO Video (link)
            VALUES (?)
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entidad.getLink());
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
    public Video[] leer(FiltrosVideo filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder("""
            SELECT id, link
            FROM Video
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
                Video[] videos = new Video[total];
                int i = 0;
                
                while (rs.next()) {
                    Video video = new Video();
                    video.setId(rs.getInt("id"));
                    video.setLink(rs.getString("link"));
                    videos[i++] = video;
                }
                return videos;
            }
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public Video[] leerCompleto(FiltrosVideo filtros, Connection conn) throws AccesoDeDatosException {
        return leer(filtros, conn);
    }

    @Override
    public Video actualizar(Video entidad, Connection conn) throws AccesoDeDatosException {
        String sql = """
            UPDATE Video 
            SET link = ?
            WHERE id = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entidad.getLink());
            ps.setInt(2, entidad.getId());
            ps.executeUpdate();
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public Video eliminar(Video entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "DELETE FROM Video WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entidad.getId());
            ps.executeUpdate();
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }
}
