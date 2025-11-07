/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.bd;

import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.filtros.FiltrosCalificacionSala;
import com.mynor.manejador.cines.api.modelo.CalificacionSala;
import com.mynor.manejador.cines.api.modelo.Cine;
import com.mynor.manejador.cines.api.modelo.Imagen;
import com.mynor.manejador.cines.api.modelo.Rol;
import com.mynor.manejador.cines.api.modelo.Sala;
import com.mynor.manejador.cines.api.modelo.Usuario;
import java.sql.*;

/**
 *
 * @author mynordma
 */
public class CalificacionSalaBD implements BaseDeDatos<CalificacionSala, FiltrosCalificacionSala> {

    @Override
    public CalificacionSala crear(CalificacionSala entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "INSERT INTO CalificacionSala (calificacion, sala) VALUES (?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, entidad.getId());
            stmt.setInt(2, entidad.getSala().getId());
            
            stmt.executeUpdate();
            
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al crear calificación de sala");
        }
    }

    @Override
    public CalificacionSala[] leer(FiltrosCalificacionSala filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder(
            "SELECT cs.calificacion, cs.sala, " +
            "c.usuario, c.valor, c.fecha " +
            "FROM CalificacionSala cs " +
            "INNER JOIN Calificacion c ON cs.calificacion = c.id " +
            "WHERE 1=1"
        );
        
        if (filtros.getIdCalificacion().isPresent()) {
            sql.append(" AND cs.calificacion = ?");
        }
        if (filtros.getIdSala().isPresent()) {
            sql.append(" AND cs.sala = ?");
        }
        if (filtros.getIdUsuario().isPresent()) {
            sql.append(" AND c.usuario = ?");
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(sql.toString(),
                                                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                            ResultSet.CONCUR_READ_ONLY)) {
            int paramIndex = 1;
            
            if (filtros.getIdCalificacion().isPresent()) {
                stmt.setInt(paramIndex++, filtros.getIdCalificacion().get());
            }
            if (filtros.getIdSala().isPresent()) {
                stmt.setInt(paramIndex++, filtros.getIdSala().get());
            }
            if (filtros.getIdUsuario().isPresent()) {
                stmt.setInt(paramIndex++, filtros.getIdUsuario().get());
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                int longitud = obtenerLongitudRS(rs);
                CalificacionSala[] calificaciones = new CalificacionSala[longitud];
                int index = 0;
                
                while (rs.next()) {
                    CalificacionSala calificacionSala = new CalificacionSala();
                    
                    calificacionSala.setId(rs.getInt("calificacion"));
                    
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("usuario"));
                    calificacionSala.setUsuario(usuario);
                    
                    calificacionSala.setValor(rs.getInt("valor"));
                    calificacionSala.setFecha(rs.getDate("fecha").toLocalDate());
                    
                    Sala sala = new Sala();
                    sala.setId(rs.getInt("sala"));
                    calificacionSala.setSala(sala);
                    
                    calificaciones[index++] = calificacionSala;
                }
                
                return calificaciones;
            }
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al leer calificaciones de sala");
        }
    }

    @Override
    public CalificacionSala[] leerCompleto(FiltrosCalificacionSala filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder(
            "SELECT cs.calificacion, cs.sala, " +
            "c.usuario, c.valor, c.fecha, " +
            "u.id AS usuario_id, u.imagen, u.nombre AS usuario_nombre, u.rol, u.correo, u.clave, u.activado, " +
            "s.cine, s.filas_asientos, s.columnas_asientos, s.calificaciones_bloqueadas, " +
            "s.comentarios_bloqueados, s.visible, " +
            "ci.id AS cine_id, ci.usuario_creador, ci.nombre AS cine_nombre, ci.ubicacion, ci.activado AS cine_activado, " +
            "uc.id AS usuario_creador_id, uc.imagen AS usuario_creador_imagen, " +
            "uc.nombre AS usuario_creador_nombre, uc.rol AS usuario_creador_rol, " +
            "uc.correo AS usuario_creador_correo, uc.clave AS usuario_creador_clave, " +
            "uc.activado AS usuario_creador_activado " +
            "FROM CalificacionSala cs " +
            "INNER JOIN Calificacion c ON cs.calificacion = c.id " +
            "LEFT JOIN Usuario u ON c.usuario = u.id " +
            "LEFT JOIN Sala s ON cs.sala = s.id " +
            "LEFT JOIN Cine ci ON s.cine = ci.id " +
            "LEFT JOIN Usuario uc ON ci.usuario_creador = uc.id " +
            "WHERE 1=1"
        );
        
        if (filtros.getIdCalificacion().isPresent()) {
            sql.append(" AND cs.calificacion = ?");
        }
        if (filtros.getIdSala().isPresent()) {
            sql.append(" AND cs.sala = ?");
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(sql.toString(),
                                                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                            ResultSet.CONCUR_READ_ONLY)) {
            int paramIndex = 1;
            
            if (filtros.getIdCalificacion().isPresent()) {
                stmt.setInt(paramIndex++, filtros.getIdCalificacion().get());
            }
            if (filtros.getIdSala().isPresent()) {
                stmt.setInt(paramIndex++, filtros.getIdSala().get());
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                int longitud = obtenerLongitudRS(rs);
                CalificacionSala[] calificaciones = new CalificacionSala[longitud];
                int index = 0;
                
                while (rs.next()) {
                    CalificacionSala calificacionSala = new CalificacionSala();
                    
                    calificacionSala.setId(rs.getInt("calificacion"));
                    calificacionSala.setValor(rs.getInt("valor"));
                    calificacionSala.setFecha(rs.getDate("fecha").toLocalDate());
                    
                    if (rs.getObject("usuario_id") != null) {
                        Usuario usuario = new Usuario();
                        usuario.setId(rs.getInt("usuario_id"));
                        
                        Imagen imagen = new Imagen();
                        imagen.setId(rs.getInt("imagen"));
                        
                        usuario.setImagen(imagen);
                        usuario.setNombre(rs.getString("usuario_nombre"));
                        usuario.setRol(Rol.valueOf(rs.getString("rol")));
                        usuario.setCorreo(rs.getString("correo"));
                        usuario.setClave(rs.getString("clave"));
                        usuario.setActivado(rs.getBoolean("activado"));
                        calificacionSala.setUsuario(usuario);
                    }
                    
                    Sala sala = new Sala();
                    sala.setId(rs.getInt("sala"));
                    sala.setFilasAsientos(rs.getInt("filas_asientos"));
                    sala.setColumnasAsientos(rs.getInt("columnas_asientos"));
                    sala.setCalificacionesBloqueadas(rs.getBoolean("calificaciones_bloqueadas"));
                    sala.setComentariosBloqueados(rs.getBoolean("comentarios_bloqueados"));
                    sala.setVisible(rs.getBoolean("visible"));
                    
                    if (rs.getObject("cine_id") != null) {
                        Cine cine = new Cine();
                        cine.setId(rs.getInt("cine_id"));
                        cine.setNombre(rs.getString("cine_nombre"));
                        cine.setUbicacion(rs.getString("ubicacion"));
                        cine.setActivado(rs.getBoolean("cine_activado"));
                        
                        if (rs.getObject("usuario_creador_id") != null) {
                            Usuario usuarioCreador = new Usuario();
                            usuarioCreador.setId(rs.getInt("usuario_creador_id"));
                            
                            Imagen imagen = new Imagen();
                            imagen.setId(rs.getInt("usuario_creador_imagen"));
                            
                            usuarioCreador.setImagen(imagen);
                            usuarioCreador.setNombre(rs.getString("usuario_creador_nombre"));
                            usuarioCreador.setRol(Rol.valueOf(rs.getString("usuario_creador_rol")));
                            usuarioCreador.setCorreo(rs.getString("usuario_creador_correo"));
                            usuarioCreador.setClave(rs.getString("usuario_creador_clave"));
                            usuarioCreador.setActivado(rs.getBoolean("usuario_creador_activado"));
                            cine.setUsuarioCreador(usuarioCreador);
                        }
                        
                        sala.setCine(cine);
                    }
                    
                    calificacionSala.setSala(sala);
                    calificaciones[index++] = calificacionSala;
                }
                
                return calificaciones;
            }
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al leer calificaciones de sala completas");
        }
    }

    @Override
    public CalificacionSala actualizar(CalificacionSala entidad, Connection conn) throws AccesoDeDatosException {
        throw new UnsupportedOperationException("Sin implementacion");
    }

    @Override
    public CalificacionSala eliminar(CalificacionSala entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "DELETE FROM CalificacionSala WHERE calificacion = ? AND sala = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, entidad.getId());
            stmt.setInt(2, entidad.getSala().getId());
            
            int filasEliminadas = stmt.executeUpdate();
            if (filasEliminadas == 0) {
                throw new AccesoDeDatosException("No se encontró la calificación de sala");
            }
            
            return entidad;
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al eliminar calificación de sala");
        }
    }
}
