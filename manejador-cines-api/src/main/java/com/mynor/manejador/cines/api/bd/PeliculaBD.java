/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.bd;

import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.filtros.FiltrosPelicula;
import com.mynor.manejador.cines.api.modelo.Calificacion;
import com.mynor.manejador.cines.api.modelo.Categoria;
import com.mynor.manejador.cines.api.modelo.Clasificacion;
import com.mynor.manejador.cines.api.modelo.Comentario;
import com.mynor.manejador.cines.api.modelo.Imagen;
import com.mynor.manejador.cines.api.modelo.Pelicula;
import com.mynor.manejador.cines.api.modelo.Usuario;
import java.sql.*;

/**
 *
 * @author mynordma
 */
public class PeliculaBD implements BaseDeDatos<Pelicula, FiltrosPelicula> {

    @Override
    public Pelicula crear(Pelicula entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "INSERT INTO Pelicula (imagen, clasificacion, titulo, sinopsis, fecha_estreno, duracion, director, reparto) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setObject(1, entidad.getImagen() != null ? entidad.getImagen().getId() : null, Types.INTEGER);
            stmt.setString(2, entidad.getClasificacion() != null ? entidad.getClasificacion().getCodigo() : null);
            stmt.setString(3, entidad.getTitulo());
            stmt.setString(4, entidad.getSinopsis());
            stmt.setDate(5, Date.valueOf(entidad.getFechaEstreno()));
            stmt.setInt(6, entidad.getDuracion());
            stmt.setString(7, entidad.getDirector());
            stmt.setString(8, entidad.getReparto());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    entidad.setId(rs.getInt(1));
                }
            }
            
            if (entidad.getCategorias() != null && entidad.getCategorias().length > 0) {
                insertarCategorias(entidad.getId(), entidad.getCategorias(), conn);
            }
            
            return entidad;
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al crear película");
        }
    }

    @Override
    public Pelicula[] leer(FiltrosPelicula filtros, Connection conn) throws AccesoDeDatosException {
        StringBuilder sql = new StringBuilder(
            "SELECT p.id, p.imagen, p.clasificacion, p.titulo, p.sinopsis, p.fecha_estreno, " +
            "p.duracion, p.director, p.reparto, " +
            "i.link as imagen_link, " +
            "c.descripcion as clasificacion_desc " +
            "FROM Pelicula p " +
            "LEFT JOIN Imagen i ON p.imagen = i.id " +
            "LEFT JOIN Clasificacion c ON p.clasificacion = c.codigo " +
            "WHERE 1=1"
        );
        
        if (filtros.getId().isPresent()) {
            sql.append(" AND p.id = ?");
        }
        if (filtros.getTitulo().isPresent()) {
            sql.append(" AND LOWER(p.titulo) LIKE LOWER(?)");
        }
        if (filtros.getIdCategoria().isPresent()) {
            sql.append(" AND p.id IN (SELECT pelicula FROM CategoriaPelicula WHERE categoria = ?)");
        }
        
        sql.append(" ORDER BY p.titulo");
        
        try (PreparedStatement stmt = conn.prepareStatement(sql.toString(),
                                                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                            ResultSet.CONCUR_READ_ONLY)) {
            int paramIndex = 1;
            
            if (filtros.getId().isPresent()) {
                stmt.setInt(paramIndex++, filtros.getId().get());
            }
            if (filtros.getTitulo().isPresent()) {
                stmt.setString(paramIndex++, "%" + filtros.getTitulo().get() + "%");
            }
            if (filtros.getIdCategoria().isPresent()) {
                stmt.setInt(paramIndex++, filtros.getIdCategoria().get());
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                int longitud = obtenerLongitudRS(rs);
                Pelicula[] peliculas = new Pelicula[longitud];
                int index = 0;
                
                while (rs.next()) {
                    peliculas[index++] = mapearPeliculaBasica(rs);
                }
                
                return peliculas;
            }
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al leer películas");
        }
    }

    @Override
    public Pelicula[] leerCompleto(FiltrosPelicula filtros, Connection conn) throws AccesoDeDatosException {
        Pelicula[] peliculas = leer(filtros, conn);
        
        for (Pelicula pelicula : peliculas) {
            pelicula.setCategorias(cargarCategorias(pelicula.getId(), conn));
            pelicula.setCalificaciones(cargarCalificaciones(pelicula.getId(), conn));
            pelicula.setComentarios(cargarComentarios(pelicula.getId(), conn));
        }
        
        return peliculas;
    }

    @Override
    public Pelicula actualizar(Pelicula entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "UPDATE Pelicula SET imagen = ?, clasificacion = ?, titulo = ?, sinopsis = ?, " +
                     "fecha_estreno = ?, duracion = ?, director = ?, reparto = ? WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setObject(1, entidad.getImagen() != null ? entidad.getImagen().getId() : null, Types.INTEGER);
            stmt.setString(2, entidad.getClasificacion() != null ? entidad.getClasificacion().getCodigo() : null);
            stmt.setString(3, entidad.getTitulo());
            stmt.setString(4, entidad.getSinopsis());
            stmt.setDate(5, Date.valueOf(entidad.getFechaEstreno()));
            stmt.setInt(6, entidad.getDuracion());
            stmt.setString(7, entidad.getDirector());
            stmt.setString(8, entidad.getReparto());
            stmt.setInt(9, entidad.getId());
            
            int filasActualizadas = stmt.executeUpdate();
            
            if (filasActualizadas == 0) {
                throw new AccesoDeDatosException("No se encontró la película con id: " + entidad.getId());
            }
            
            eliminarCategorias(entidad.getId(), conn);
            if (entidad.getCategorias() != null && entidad.getCategorias().length > 0) {
                insertarCategorias(entidad.getId(), entidad.getCategorias(), conn);
            }
            
            return entidad;
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al actualizar película");
        }
    }

    @Override
    public Pelicula eliminar(Pelicula entidad, Connection conn) throws AccesoDeDatosException {
        String sql = "DELETE FROM Pelicula WHERE id = ?";
        
        try {
            eliminarCategorias(entidad.getId(), conn);
            eliminarCalificaciones(entidad.getId(), conn);
            eliminarComentarios(entidad.getId(), conn);
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, entidad.getId());
                
                int filasEliminadas = stmt.executeUpdate();
                
                if (filasEliminadas == 0) {
                    throw new AccesoDeDatosException("No se encontró la película con id: " + entidad.getId());
                }
            }
            
            return entidad;
            
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al eliminar película");
        }
    }
    
    private Pelicula mapearPeliculaBasica(ResultSet rs) throws SQLException {
        Pelicula pelicula = new Pelicula();
        
        pelicula.setId(rs.getInt("id"));
        pelicula.setTitulo(rs.getString("titulo"));
        pelicula.setSinopsis(rs.getString("sinopsis"));
        pelicula.setFechaEstreno(rs.getDate("fecha_estreno").toLocalDate());
        pelicula.setDuracion(rs.getInt("duracion"));
        pelicula.setDirector(rs.getString("director"));
        pelicula.setReparto(rs.getString("reparto"));
        
        if (rs.getObject("imagen") != null) {
            Imagen imagen = new Imagen();
            imagen.setId(rs.getInt("imagen"));
            imagen.setLink(rs.getString("imagen_link"));
            pelicula.setImagen(imagen);
        }
        
        if (rs.getString("clasificacion") != null) {
            Clasificacion clasificacion = new Clasificacion();
            clasificacion.setCodigo(rs.getString("clasificacion"));
            clasificacion.setDescripcion(rs.getString("clasificacion_desc"));
            pelicula.setClasificacion(clasificacion);
        }
        
        return pelicula;
    }
    
    private void insertarCategorias(Integer idPelicula, Categoria[] categorias, Connection conn) throws SQLException {
        String sql = "INSERT INTO CategoriaPelicula (categoria, pelicula) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Categoria categoria : categorias) {
                stmt.setInt(1, categoria.getId());
                stmt.setInt(2, idPelicula);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }
    
    private void eliminarCategorias(Integer idPelicula, Connection conn) throws SQLException {
        String sql = "DELETE FROM CategoriaPelicula WHERE pelicula = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPelicula);
            stmt.executeUpdate();
        }
    }
    
    private Categoria[] cargarCategorias(Integer idPelicula, Connection conn) throws AccesoDeDatosException {
        String sql = "SELECT c.id, c.nombre FROM Categoria c " +
                     "INNER JOIN CategoriaPelicula cp ON c.id = cp.categoria " +
                     "WHERE cp.pelicula = ? ORDER BY c.nombre";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql,
                                                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                            ResultSet.CONCUR_READ_ONLY)) {
            stmt.setInt(1, idPelicula);
            
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
            throw new AccesoDeDatosException("Error al cargar categorías");
        }
    }
    
    private Calificacion[] cargarCalificaciones(Integer idPelicula, Connection conn) throws AccesoDeDatosException {
        String sql = "SELECT c.id, c.usuario, c.valor, c.fecha, " +
                     "u.imagen, u.nombre, u.rol, u.correo, u.activado " +
                     "FROM Calificacion c " +
                     "INNER JOIN CalificacionPelicula cp ON c.id = cp.calificacion " +
                     "LEFT JOIN Usuario u ON c.usuario = u.id " +
                     "WHERE cp.pelicula = ? ORDER BY c.fecha DESC";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql,
                                                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                            ResultSet.CONCUR_READ_ONLY)) {
            stmt.setInt(1, idPelicula);
            
            try (ResultSet rs = stmt.executeQuery()) {
                int longitud = obtenerLongitudRS(rs);
                Calificacion[] calificaciones = new Calificacion[longitud];
                int index = 0;
                
                while (rs.next()) {
                    Calificacion calificacion = new Calificacion();
                    calificacion.setId(rs.getInt("id"));
                    calificacion.setValor(rs.getInt("valor"));
                    calificacion.setFecha(rs.getDate("fecha").toLocalDate());
                    
                    if (rs.getObject("usuario") != null) {
                        Usuario usuario = new Usuario();
                        usuario.setId(rs.getInt("usuario"));
                        usuario.setNombre(rs.getString("nombre"));
                        usuario.setCorreo(rs.getString("correo"));
                        usuario.setActivado(rs.getBoolean("activado"));
                        
                        if (rs.getObject("imagen") != null) {
                            Imagen imagen = new Imagen();
                            imagen.setId(rs.getInt("imagen"));
                            usuario.setImagen(imagen);
                        }
                        
                        calificacion.setUsuario(usuario);
                    }
                    
                    calificaciones[index++] = calificacion;
                }
                
                return calificaciones;
            }
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al cargar calificaciones");
        }
    }
    
    private Comentario[] cargarComentarios(Integer idPelicula, Connection conn) throws AccesoDeDatosException {
        String sql = "SELECT c.id, c.usuario, c.contenido, c.fecha, " +
                     "u.imagen, u.nombre, u.rol, u.correo, u.activado " +
                     "FROM Comentario c " +
                     "INNER JOIN ComentarioPelicula cp ON c.id = cp.comentario " +
                     "LEFT JOIN Usuario u ON c.usuario = u.id " +
                     "WHERE cp.pelicula = ? ORDER BY c.fecha DESC";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql,
                                                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                            ResultSet.CONCUR_READ_ONLY)) {
            stmt.setInt(1, idPelicula);
            
            try (ResultSet rs = stmt.executeQuery()) {
                int longitud = obtenerLongitudRS(rs);
                Comentario[] comentarios = new Comentario[longitud];
                int index = 0;
                
                while (rs.next()) {
                    Comentario comentario = new Comentario();
                    comentario.setId(rs.getInt("id"));
                    comentario.setContenido(rs.getString("contenido"));
                    comentario.setFecha(rs.getDate("fecha").toLocalDate());
                    
                    if (rs.getObject("usuario") != null) {
                        Usuario usuario = new Usuario();
                        usuario.setId(rs.getInt("usuario"));
                        usuario.setNombre(rs.getString("nombre"));
                        usuario.setCorreo(rs.getString("correo"));
                        usuario.setActivado(rs.getBoolean("activado"));
                        
                        if (rs.getObject("imagen") != null) {
                            Imagen imagen = new Imagen();
                            imagen.setId(rs.getInt("imagen"));
                            usuario.setImagen(imagen);
                        }
                        
                        comentario.setUsuario(usuario);
                    }
                    
                    comentarios[index++] = comentario;
                }
                
                return comentarios;
            }
        } catch (SQLException e) {
            throw new AccesoDeDatosException("Error al cargar comentarios");
        }
    }
    
    private void eliminarCalificaciones(Integer idPelicula, Connection conn) throws SQLException {
        String sqlRelacion = "DELETE FROM CalificacionPelicula WHERE pelicula = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sqlRelacion)) {
            stmt.setInt(1, idPelicula);
            stmt.executeUpdate();
        }
        
        String sql = "DELETE FROM Calificacion WHERE id NOT IN (SELECT calificacion FROM CalificacionPelicula)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }
    
    private void eliminarComentarios(Integer idPelicula, Connection conn) throws SQLException {
        String sqlRelacion = "DELETE FROM ComentarioPelicula WHERE pelicula = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sqlRelacion)) {
            stmt.setInt(1, idPelicula);
            stmt.executeUpdate();
        }
        
        String sql = "DELETE FROM Comentario WHERE id NOT IN (SELECT comentario FROM ComentarioPelicula)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }
}