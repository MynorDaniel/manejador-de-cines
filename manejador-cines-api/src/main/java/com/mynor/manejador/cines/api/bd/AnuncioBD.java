/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.bd;

import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.filtros.FiltrosAnuncio;
import com.mynor.manejador.cines.api.modelo.Anuncio;
import com.mynor.manejador.cines.api.modelo.Imagen;
import com.mynor.manejador.cines.api.modelo.Pago;
import com.mynor.manejador.cines.api.modelo.Rol;
import com.mynor.manejador.cines.api.modelo.TipoAnuncio;
import com.mynor.manejador.cines.api.modelo.Usuario;
import java.sql.Connection;
import java.sql.*;

/**
 *
 * @author mynordma
 */
public class AnuncioBD implements BaseDeDatos<Anuncio, FiltrosAnuncio> {
    
    @Override
    public Anuncio crear(Anuncio entidad, Connection conn) throws AccesoDeDatosException {
        String sql = """
            INSERT INTO Anuncio (vigencia, tipo, texto, activado)
            VALUES (?, ?, ?, ?)
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entidad.getVigencia());
            ps.setString(2, entidad.getTipo().name());
            ps.setString(3, entidad.getTexto());
            ps.setBoolean(4, entidad.getActivado());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entidad.setId(rs.getInt(1));
                }
            }

            // PagoAnuncio
            if (entidad.getPago() != null && entidad.getPago().getId() != null) {
                String sqlPagoAnuncio = "INSERT INTO PagoAnuncio (anuncio, pago) VALUES (?, ?)";
                try (PreparedStatement psPago = conn.prepareStatement(sqlPagoAnuncio)) {
                    psPago.setInt(1, entidad.getId());
                    psPago.setInt(2, entidad.getPago().getId());
                    psPago.executeUpdate();
                }
            }
            
            // Relacion a Imagen o Video
            if (entidad.getIdMedia() != null) {
                switch (entidad.getTipo()) {
                    case TEXTO_IMAGEN -> {
                        String sqlAnuncioImagen = "INSERT INTO AnuncioImagen (anuncio, imagen) VALUES (?, ?)";
                        try (PreparedStatement psImagen = conn.prepareStatement(sqlAnuncioImagen)) {
                            psImagen.setInt(1, entidad.getId());
                            psImagen.setInt(2, entidad.getIdMedia());
                            psImagen.executeUpdate();
                        }
                    }
                    case TEXTO_VIDEO -> {
                        String sqlAnuncioVideo = "INSERT INTO AnuncioVideo (anuncio, video) VALUES (?, ?)";
                        try (PreparedStatement psVideo = conn.prepareStatement(sqlAnuncioVideo)) {
                            psVideo.setInt(1, entidad.getId());
                            psVideo.setInt(2, entidad.getIdMedia());
                            psVideo.executeUpdate();
                        }
                    }
                }
            }

            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public Anuncio[] leer(FiltrosAnuncio filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder("""
            SELECT a.id, a.vigencia, a.tipo, a.texto, a.activado
            FROM Anuncio a
            WHERE 1=1
        """);
        
        if (filtros != null) {
            if (filtros.getId().isPresent()) {
                sql.append(" AND a.id = ?");
            }
            if (filtros.getTipo().isPresent()) {
                sql.append(" AND a.tipo = ?");
            }
            if (filtros.getVigencia().isPresent()) {
                sql.append(" AND a.vigencia = ?");
            }
            if (filtros.getIdUsuario().isPresent() || filtros.getFechaInicial().isPresent() || filtros.getFechaFinal().isPresent()) {
                sql.append(" AND EXISTS (SELECT 1 FROM PagoAnuncio pa INNER JOIN Pago p ON pa.pago = p.id WHERE pa.anuncio = a.id");
                
                if (filtros.getIdUsuario().isPresent()) {
                    sql.append(" AND p.usuario = ?");
                }
                if (filtros.getFechaInicial().isPresent() && filtros.getFechaFinal().isPresent()) {
                    sql.append(" AND p.fecha BETWEEN ? AND ?");
                } else if (filtros.getFechaInicial().isPresent()) {
                    sql.append(" AND p.fecha >= ?");
                } else if (filtros.getFechaFinal().isPresent()) {
                    sql.append(" AND p.fecha <= ?");
                }
                
                sql.append(")");
            }
        }
        
        try (PreparedStatement ps = conn.prepareStatement(sql.toString(),
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            
            int index = 1;
            if (filtros != null) {
                if (filtros.getId().isPresent()) {
                    ps.setInt(index++, filtros.getId().get());
                }
                if (filtros.getTipo().isPresent()) {
                    ps.setString(index++, filtros.getTipo().get().name());
                }
                if (filtros.getVigencia().isPresent()) {
                    ps.setInt(index++, filtros.getVigencia().get());
                }
                if (filtros.getIdUsuario().isPresent()) {
                    ps.setInt(index++, filtros.getIdUsuario().get());
                }
                if (filtros.getFechaInicial().isPresent() && filtros.getFechaFinal().isPresent()) {
                    ps.setDate(index++, Date.valueOf(filtros.getFechaInicial().get()));
                    ps.setDate(index++, Date.valueOf(filtros.getFechaFinal().get()));
                } else if (filtros.getFechaInicial().isPresent()) {
                    ps.setDate(index++, Date.valueOf(filtros.getFechaInicial().get()));
                } else if (filtros.getFechaFinal().isPresent()) {
                    ps.setDate(index++, Date.valueOf(filtros.getFechaFinal().get()));
                }
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                int total = obtenerLongitudRS(rs);
                Anuncio[] anuncios = new Anuncio[total];
                int i = 0;
                
                while (rs.next()) {
                    Anuncio anuncio = new Anuncio();
                    anuncio.setId(rs.getInt("id"));
                    anuncio.setVigencia(rs.getInt("vigencia"));
                    anuncio.setTipo(TipoAnuncio.valueOf(rs.getString("tipo")));
                    anuncio.setTexto(rs.getString("texto"));
                    anuncio.setActivado(rs.getBoolean("activado"));
                    anuncios[i++] = anuncio;
                }
                return anuncios;
            }
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public Anuncio[] leerCompleto(FiltrosAnuncio filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder("""
            SELECT 
                a.id, 
                a.vigencia, 
                a.tipo, 
                a.texto, 
                a.activado,
                p.id as pago_id,
                p.usuario as pago_usuario,
                p.fecha as pago_fecha,
                p.monto as pago_monto,
                u.id as usuario_id,
                u.imagen as usuario_imagen,
                u.nombre as usuario_nombre,
                u.rol as usuario_rol,
                u.correo as usuario_correo,
                u.clave as usuario_clave,
                u.activado as usuario_activado
            FROM Anuncio a
            LEFT JOIN PagoAnuncio pa ON a.id = pa.anuncio
            LEFT JOIN Pago p ON pa.pago = p.id
            LEFT JOIN Usuario u ON p.usuario = u.id
            WHERE 1=1
        """);
        
        if (filtros != null) {
            if (filtros.getId().isPresent()) {
                sql.append(" AND a.id = ?");
            }
            if (filtros.getTipo().isPresent()) {
                sql.append(" AND a.tipo = ?");
            }
            if (filtros.getVigencia().isPresent()) {
                sql.append(" AND a.vigencia = ?");
            }
            if (filtros.getIdUsuario().isPresent()) {
                sql.append(" AND p.usuario = ?");
            }
            if (filtros.getFechaInicial().isPresent() && filtros.getFechaFinal().isPresent()) {
                sql.append(" AND p.fecha BETWEEN ? AND ?");
            } else if (filtros.getFechaInicial().isPresent()) {
                sql.append(" AND p.fecha >= ?");
            } else if (filtros.getFechaFinal().isPresent()) {
                sql.append(" AND p.fecha <= ?");
            }
        }
        
        try (PreparedStatement ps = conn.prepareStatement(sql.toString(),
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            
            int index = 1;
            if (filtros != null) {
                if (filtros.getId().isPresent()) {
                    ps.setInt(index++, filtros.getId().get());
                }
                if (filtros.getTipo().isPresent()) {
                    ps.setString(index++, filtros.getTipo().get().name());
                }
                if (filtros.getVigencia().isPresent()) {
                    ps.setInt(index++, filtros.getVigencia().get());
                }
                if (filtros.getIdUsuario().isPresent()) {
                    ps.setInt(index++, filtros.getIdUsuario().get());
                }
                if (filtros.getFechaInicial().isPresent() && filtros.getFechaFinal().isPresent()) {
                    ps.setDate(index++, Date.valueOf(filtros.getFechaInicial().get()));
                    ps.setDate(index++, Date.valueOf(filtros.getFechaFinal().get()));
                } else if (filtros.getFechaInicial().isPresent()) {
                    ps.setDate(index++, Date.valueOf(filtros.getFechaInicial().get()));
                } else if (filtros.getFechaFinal().isPresent()) {
                    ps.setDate(index++, Date.valueOf(filtros.getFechaFinal().get()));
                }
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                int total = obtenerLongitudRS(rs);
                Anuncio[] anuncios = new Anuncio[total];
                int i = 0;
                
                while (rs.next()) {
                    Anuncio anuncio = new Anuncio();
                    anuncio.setId(rs.getInt("id"));
                    anuncio.setVigencia(rs.getInt("vigencia"));
                    anuncio.setTipo(TipoAnuncio.valueOf(rs.getString("tipo")));
                    anuncio.setTexto(rs.getString("texto"));
                    anuncio.setActivado(rs.getBoolean("activado"));
                    
                    if (rs.getObject("pago_id") != null) {
                        Pago pago = new Pago();
                        pago.setId(rs.getInt("pago_id"));
                        pago.setFecha(rs.getDate("pago_fecha").toLocalDate());
                        pago.setMonto(rs.getDouble("pago_monto"));
                        
                        if (rs.getObject("usuario_id") != null) {
                            Usuario usuario = new Usuario();
                            usuario.setId(rs.getInt("usuario_id"));
                            
                            Imagen img = new Imagen();
                            img.setId(rs.getInt("usuario_imagen"));
                            usuario.setImagen(img);
                            
                            usuario.setNombre(rs.getString("usuario_nombre"));
                            usuario.setRol(Rol.valueOf(rs.getString("usuario_rol")));
                            usuario.setCorreo(rs.getString("usuario_correo"));
                            usuario.setClave(rs.getString("usuario_clave"));
                            usuario.setActivado(rs.getBoolean("usuario_activado"));
                            
                            pago.setUsuario(usuario);
                        }
                        
                        anuncio.setPago(pago);
                    }
                    
                    anuncios[i++] = anuncio;
                }
                return anuncios;
            }
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public Anuncio actualizar(Anuncio entidad, Connection conn) throws AccesoDeDatosException {
        String sql = """
            UPDATE Anuncio 
            SET vigencia = ?, tipo = ?, texto = ?, activado = ?
            WHERE id = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entidad.getVigencia());
            ps.setString(2, entidad.getTipo().name());
            ps.setString(3, entidad.getTexto());
            ps.setBoolean(4, entidad.getActivado());
            ps.setInt(5, entidad.getId());
            ps.executeUpdate();
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }

    @Override
    public Anuncio eliminar(Anuncio entidad, Connection conn) throws AccesoDeDatosException {
        String sqlPagoAnuncio = "DELETE FROM PagoAnuncio WHERE anuncio = ?";
        try (PreparedStatement psPago = conn.prepareStatement(sqlPagoAnuncio)) {
            psPago.setInt(1, entidad.getId());
            psPago.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
        
        String sql = "DELETE FROM Anuncio WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entidad.getId());
            ps.executeUpdate();
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException(e.getMessage());
        }
    }
}
