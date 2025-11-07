/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.servicios;

import com.mynor.manejador.cines.api.bd.BaseDeDatos;
import com.mynor.manejador.cines.api.bd.CalificacionBD;
import com.mynor.manejador.cines.api.bd.CategoriaBD;
import com.mynor.manejador.cines.api.bd.ClasificacionBD;
import com.mynor.manejador.cines.api.bd.PeliculaBD;
import com.mynor.manejador.cines.api.bd.Transaccion;
import com.mynor.manejador.cines.api.dtos.CalificacionDTO;
import com.mynor.manejador.cines.api.dtos.CategoriaDTO;
import com.mynor.manejador.cines.api.dtos.ClasificacionDTO;
import com.mynor.manejador.cines.api.dtos.ComentarioDTO;
import com.mynor.manejador.cines.api.dtos.FiltrosPeliculasDTO;
import com.mynor.manejador.cines.api.dtos.ImagenEntradaDTO;
import com.mynor.manejador.cines.api.dtos.PeliculaDTO;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.filtros.FiltrosCalificacion;
import com.mynor.manejador.cines.api.filtros.FiltrosCategoria;
import com.mynor.manejador.cines.api.filtros.FiltrosClasificacion;
import com.mynor.manejador.cines.api.filtros.FiltrosPelicula;
import com.mynor.manejador.cines.api.modelo.Calificacion;
import com.mynor.manejador.cines.api.modelo.Categoria;
import com.mynor.manejador.cines.api.modelo.Clasificacion;
import com.mynor.manejador.cines.api.modelo.Imagen;
import com.mynor.manejador.cines.api.modelo.Pelicula;
import com.mynor.manejador.cines.api.modelo.Usuario;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author mynordma
 */
public class PeliculaServicio {
    
    private final BaseDeDatos<Pelicula, FiltrosPelicula> PELICULA_BD;
    private final BaseDeDatos<Clasificacion, FiltrosClasificacion> CLASIFICACION_BD;
    private final BaseDeDatos<Categoria, FiltrosCategoria> CATEGORIA_BD;
    private final BaseDeDatos<Calificacion, FiltrosCalificacion> CALIFICACION_BD;
    
    public PeliculaServicio(){
        PELICULA_BD = new PeliculaBD();
        CLASIFICACION_BD = new ClasificacionBD();
        CATEGORIA_BD = new CategoriaBD();
        CALIFICACION_BD = new CalificacionBD();
    }

    public void crearPelicula(PeliculaDTO peliculaDTO) throws AccesoDeDatosException {
        Pelicula pelicula = new Pelicula();
        pelicula.setDirector(peliculaDTO.getDirector());
        pelicula.setDuracion(Integer.valueOf(peliculaDTO.getDuracion()));
        pelicula.setFechaEstreno(LocalDate.parse(peliculaDTO.getFechaEstreno()));
        pelicula.setReparto(peliculaDTO.getReparto());
        pelicula.setSinopsis(peliculaDTO.getSinopsis());
        pelicula.setTitulo(peliculaDTO.getTitulo());
        
        Imagen imagen = new Imagen();
        imagen.setId(Integer.valueOf(peliculaDTO.getImagen().getId()));
        
        Clasificacion clasificacion = new Clasificacion();
        clasificacion.setCodigo(peliculaDTO.getClasificacion().getCodigo());
        
        Categoria[] categorias = new Categoria[peliculaDTO.getCategorias().length];
        for (int i = 0; i < categorias.length; i++) {
            CategoriaDTO categoriaDTO = peliculaDTO.getCategorias()[i];
            Categoria categoria = new Categoria();
            categoria.setId(Integer.valueOf(categoriaDTO.getId()));
            categorias[i] = categoria;
        }
        
        pelicula.setImagen(imagen);
        pelicula.setClasificacion(clasificacion);
        pelicula.setCategorias(categorias);
        
        try(Transaccion t = new Transaccion()){
            PELICULA_BD.crear(pelicula, t.obtenerConexion());
            t.commit();
        }
    }

    public PeliculaDTO[] verPeliculas(Integer idUsuario, FiltrosPeliculasDTO filtros) throws AccesoDeDatosException {
        Pelicula[] peliculas = leerTodasLasPeliculas(filtros);

        return Arrays.stream(peliculas).map(pelicula -> {
            System.out.println(pelicula.getTitulo());
            PeliculaDTO peliculaDTO = new PeliculaDTO();

            peliculaDTO.setId(pelicula.getId().toString());
            peliculaDTO.setTitulo(pelicula.getTitulo());
            peliculaDTO.setSinopsis(pelicula.getSinopsis());
            peliculaDTO.setFechaEstreno(pelicula.getFechaEstreno().toString());
            peliculaDTO.setDuracion(pelicula.getDuracion().toString());
            peliculaDTO.setDirector(pelicula.getDirector());
            peliculaDTO.setReparto(pelicula.getReparto());

            ImagenEntradaDTO imagenDTO = new ImagenEntradaDTO();
            imagenDTO.setId(pelicula.getImagen().getId().toString());
            peliculaDTO.setImagen(imagenDTO);
            
            ClasificacionDTO clasificacionDTO = new ClasificacionDTO();
            clasificacionDTO.setCodigo(pelicula.getClasificacion().getCodigo());
            peliculaDTO.setClasificacion(clasificacionDTO);

            CategoriaDTO[] categoriasDTO = Arrays.stream(pelicula.getCategorias())
                .map(categoria -> {
                    CategoriaDTO categoriaDTO = new CategoriaDTO();
                    categoriaDTO.setId(categoria.getId().toString());
                    categoriaDTO.setNombre(categoria.getNombre());
                    return categoriaDTO;
                })
                .toArray(CategoriaDTO[]::new);
            peliculaDTO.setCategorias(categoriasDTO);
            
            return peliculaDTO;
        }).toArray(PeliculaDTO[]::new);
    }

    public PeliculaDTO verPelicula(Integer idUsuario, String id) throws AccesoDeDatosException {
        Pelicula pelicula = leerPeliculaPorId(Integer.valueOf(id));

        PeliculaDTO peliculaDTO = new PeliculaDTO();

        peliculaDTO.setId(pelicula.getId().toString());
        peliculaDTO.setTitulo(pelicula.getTitulo());
        peliculaDTO.setSinopsis(pelicula.getSinopsis());
        peliculaDTO.setFechaEstreno(pelicula.getFechaEstreno().toString());
        peliculaDTO.setDuracion(pelicula.getDuracion().toString());
        peliculaDTO.setDirector(pelicula.getDirector());
        peliculaDTO.setReparto(pelicula.getReparto());

        ImagenEntradaDTO imagenDTO = new ImagenEntradaDTO();
        imagenDTO.setId(pelicula.getImagen().getId().toString());
        peliculaDTO.setImagen(imagenDTO);

        ClasificacionDTO clasificacionDTO = new ClasificacionDTO();
        clasificacionDTO.setCodigo(pelicula.getClasificacion().getCodigo());
        peliculaDTO.setClasificacion(clasificacionDTO);

        CategoriaDTO[] categoriasDTO = Arrays.stream(pelicula.getCategorias())
            .map(categoria -> {
                CategoriaDTO categoriaDTO = new CategoriaDTO();
                categoriaDTO.setId(categoria.getId().toString());
                categoriaDTO.setNombre(categoria.getNombre());
                return categoriaDTO;
            })
            .toArray(CategoriaDTO[]::new);
        peliculaDTO.setCategorias(categoriasDTO);

        CalificacionDTO[] calificacionesDTO = Arrays.stream(pelicula.getCalificaciones())
            .map(calificacion -> {
                CalificacionDTO calificacionDTO = new CalificacionDTO();
                calificacionDTO.setId(calificacion.getId().toString());
                calificacionDTO.setValor(calificacion.getValor().toString());
                calificacionDTO.setFecha(calificacion.getFecha().toString());
                calificacionDTO.setIdUsuario(calificacion.getUsuario().getId().toString());

                return calificacionDTO;
            })
            .toArray(CalificacionDTO[]::new);
        peliculaDTO.setCalificaciones(calificacionesDTO);

        ComentarioDTO[] comentariosDTO = Arrays.stream(pelicula.getComentarios())
            .map(comentario -> {
                ComentarioDTO comentarioDTO = new ComentarioDTO();
                comentarioDTO.setId(comentario.getId().toString());
                comentarioDTO.setContenido(comentario.getContenido());
                comentarioDTO.setFecha(comentario.getFecha().toString());
                comentarioDTO.setIdUsuario(comentario.getUsuario().getId().toString());

                return comentarioDTO;
            })
            .toArray(ComentarioDTO[]::new);
        peliculaDTO.setComentarios(comentariosDTO);

        return peliculaDTO;
    }

    public void editarPelicula(PeliculaDTO peliculaDTO) throws AccesoDeDatosException {
        Pelicula pelicula = leerPeliculaPorId(Integer.valueOf(peliculaDTO.getId()));

        pelicula.setTitulo(peliculaDTO.getTitulo());
        pelicula.setSinopsis(peliculaDTO.getSinopsis());
        pelicula.setFechaEstreno(LocalDate.parse(peliculaDTO.getFechaEstreno()));
        pelicula.setDuracion(Integer.valueOf(peliculaDTO.getDuracion()));
        pelicula.setDirector(peliculaDTO.getDirector());
        pelicula.setReparto(peliculaDTO.getReparto());

        Imagen imagen = new Imagen();
        imagen.setId(Integer.valueOf(peliculaDTO.getImagen().getId()));
        pelicula.setImagen(imagen);

        Clasificacion clasificacion = new Clasificacion();
        clasificacion.setCodigo(peliculaDTO.getClasificacion().getCodigo());
        pelicula.setClasificacion(clasificacion);

        Categoria[] categorias = Arrays.stream(peliculaDTO.getCategorias())
            .map(categoriaDTO -> {
                Categoria categoria = new Categoria();
                categoria.setId(Integer.valueOf(categoriaDTO.getId()));
                categoria.setNombre(categoriaDTO.getNombre());
                return categoria;
            })
            .toArray(Categoria[]::new);
        pelicula.setCategorias(categorias);

        try (Transaccion t = new Transaccion()) {
            PELICULA_BD.actualizar(pelicula, t.obtenerConexion());
            t.commit();
        }
    }

    public void eliminarPelicula(Integer id) throws AccesoDeDatosException {
        Pelicula pelicula = new Pelicula();
        pelicula.setId(id);

        try (Transaccion t = new Transaccion()) {
            PELICULA_BD.eliminar(pelicula, t.obtenerConexion());
            t.commit();
        }
    }

    public void cambiarCalificacion(CalificacionDTO calificacionDTO, String id, Integer idUsuario) throws AccesoDeDatosException {

        Optional<Calificacion> calificacionExistente = leerCalificacionPelicula(idUsuario, Integer.valueOf(id));

        if (calificacionExistente.isPresent()) {
            Calificacion calificacion = calificacionExistente.get();
            calificacion.setValor(Integer.valueOf(calificacionDTO.getValor()));

            try (Transaccion t = new Transaccion()) {
                CALIFICACION_BD.actualizar(calificacion, t.obtenerConexion());
                t.commit();
            }

        } else {
            Calificacion calificacion = new Calificacion();
            calificacion.setFecha(LocalDate.now());
            calificacion.setValor(Integer.valueOf(calificacionDTO.getValor()));

            Usuario usuario = new Usuario();
            usuario.setId(idUsuario);
            calificacion.setUsuario(usuario);

            try (Transaccion t = new Transaccion()) {
                Calificacion calificacionCreada = CALIFICACION_BD.crear(calificacion, t.obtenerConexion());
                ((CalificacionBD) CALIFICACION_BD).crearCalificacionPelicula(calificacionCreada.getId(), Integer.valueOf(id), t.obtenerConexion());
                t.commit();
            }
        }
    }

    private Optional<Calificacion> leerCalificacionPelicula(Integer idUsuario, Integer idPelicula) throws AccesoDeDatosException {

        try (Transaccion t = new Transaccion()) {
            Optional<Calificacion> calificacionOpt = ((CalificacionBD) CALIFICACION_BD).leerCalificacionPelicula(idUsuario, idPelicula, t.obtenerConexion());
            t.commit();
            return calificacionOpt;
        }
    }

    public ClasificacionDTO[] verClasificaciones() throws AccesoDeDatosException {
        FiltrosClasificacion filtros = new FiltrosClasificacion();

        try (Transaccion t = new Transaccion()) {
            Clasificacion[] clasificaciones = CLASIFICACION_BD.leer(filtros, t.obtenerConexion());
            t.commit();

            return Arrays.stream(clasificaciones)
                .map(clasificacion -> {
                    ClasificacionDTO clasificacionDTO = new ClasificacionDTO();
                    clasificacionDTO.setCodigo(clasificacion.getCodigo());
                    clasificacionDTO.setDescripcion(clasificacion.getDescripcion());
                    return clasificacionDTO;
                })
                .toArray(ClasificacionDTO[]::new);
        }
    }

    public CategoriaDTO[] verCategorias() throws AccesoDeDatosException {
        FiltrosCategoria filtros = new FiltrosCategoria();

        try (Transaccion t = new Transaccion()) {
            Categoria[] categorias = CATEGORIA_BD.leer(filtros, t.obtenerConexion());
            t.commit();

            return Arrays.stream(categorias)
                .map(categoria -> {
                    CategoriaDTO categoriaDTO = new CategoriaDTO();
                    categoriaDTO.setId(categoria.getId().toString());
                    categoriaDTO.setNombre(categoria.getNombre());
                    return categoriaDTO;
                })
                .toArray(CategoriaDTO[]::new);
        }
    }
    
    private Pelicula[] leerTodasLasPeliculas(FiltrosPeliculasDTO filtrosDTO) throws AccesoDeDatosException{
        FiltrosPelicula filtros = new FiltrosPelicula();
        
        if(!StringUtils.isBlank(filtrosDTO.getId())){
            filtros.setId(Optional.ofNullable(Integer.valueOf(filtrosDTO.getId())));
        }
        
        if(!StringUtils.isBlank(filtrosDTO.getIdCategoria())){
            filtros.setIdCategoria(Optional.ofNullable(Integer.valueOf(filtrosDTO.getIdCategoria())));
        }
        
        if(!StringUtils.isBlank(filtrosDTO.getTitulo())){
            filtros.setTitulo(Optional.ofNullable(filtrosDTO.getTitulo()));
        }
        
        try(Transaccion t = new Transaccion()){
            Pelicula[] peliculas = PELICULA_BD.leerCompleto(filtros, t.obtenerConexion());
            t.commit();
            
            if(peliculas.length < 1) throw new AccesoDeDatosException("Sin coincidencias");
            
            return peliculas;
        }
    }
    
    private Pelicula leerPeliculaPorId(Integer id) throws AccesoDeDatosException{
        FiltrosPelicula filtros = new FiltrosPelicula();
        filtros.setId(Optional.ofNullable(id));
        
        try(Transaccion t = new Transaccion()){
            Pelicula[] peliculas = PELICULA_BD.leerCompleto(filtros, t.obtenerConexion());
            t.commit();
            
            if(peliculas.length < 1) throw new AccesoDeDatosException("Sin coincidencias");
            
            return peliculas[0];
        }
    }
    
}
