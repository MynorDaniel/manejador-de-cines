/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.bd;

import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.filtros.FiltrosImagen;
import com.mynor.manejador.cines.api.modelo.Imagen;
import java.sql.*;

/**
 *
 * @author mynordma
 */
public class ImagenBD implements BaseDeDatos<Imagen, FiltrosImagen> {
    
    @Override
    public Imagen crear(Imagen imagen, Connection conn) throws AccesoDeDatosException {
        System.out.println(imagen.getLink());
        String sql = """
            INSERT INTO Imagen (link)
            VALUES (?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, imagen.getLink());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    imagen.setId(rs.getInt(1));
                }
            }

            return imagen;

        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public Imagen[] leer(FiltrosImagen filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder("""
            SELECT id, link
            FROM Imagen
            WHERE 1=1
        """);

        if (filtros != null) {
            
            if (filtros.getUltimoId().isPresent()) return leerUltimo(conn);
            
            if (filtros.getId().isPresent()) {
                sql.append(" AND id = ?");
            }
            if (filtros.getLink().isPresent()) {
                sql.append(" AND link = ?");
            }
        }

        try (PreparedStatement ps = conn.prepareStatement(sql.toString(), 
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            int index = 1;

            if (filtros != null) {
                if (filtros.getId().isPresent()) {
                    ps.setInt(index++, filtros.getId().get());
                }
                if (filtros.getLink().isPresent()) {
                    ps.setString(index++, filtros.getLink().get());
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                int total = obtenerLongitudRS(rs);
                Imagen[] imagenes = new Imagen[total];
                int i = 0;

                while (rs.next()) {
                    Imagen img = new Imagen();
                    img.setId(rs.getInt("id"));
                    img.setLink(rs.getString("link"));

                    imagenes[i++] = img;
                }

                return imagenes;
            }

        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public Imagen[] leerCompleto(FiltrosImagen filtros, Connection conn) throws AccesoDeDatosException {
        return leer(filtros, conn);
    }

    @Override
    public Imagen actualizar(Imagen imagen, Connection conn) throws AccesoDeDatosException {
        String sql = """
            UPDATE Imagen
            SET link = ?
            WHERE id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, imagen.getLink());
            ps.setInt(2, imagen.getId());

            ps.executeUpdate();
            return imagen;

        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public Imagen eliminar(Imagen imagen, Connection conn) throws AccesoDeDatosException {
        String sql = "DELETE FROM Imagen WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, imagen.getId());
            ps.executeUpdate();
            return imagen;

        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    private Imagen[] leerUltimo(Connection conn) throws AccesoDeDatosException {
        String sql = """
            SELECT id, link
            FROM Imagen
            ORDER BY id DESC
            LIMIT 1
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                Imagen imagen = new Imagen();
                imagen.setId(rs.getInt("id"));
                imagen.setLink(rs.getString("link"));
                return new Imagen[]{imagen};
            }

            return null;

        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }
}