/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.dtos;

import com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException;
import com.mynor.manejador.cines.api.excepciones.PeliculaInvalidaException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author mynordma
 */
public class PeliculaDTO extends Validador {
    
    private String id;
    private ImagenEntradaDTO imagen;
    private ClasificacionDTO clasificacion;
    private String titulo;
    private String sinopsis;
    private String fechaEstreno;
    private String duracion;
    private String director;
    private String reparto;
    private CategoriaDTO[] categorias;
    private CalificacionDTO[] calificaciones;
    private ComentarioDTO[] comentarios;

    @Override
    public void validarEntrada() throws EntidadInvalidaException {
        imagen.validarEntrada();
        
        validarCampos();
    }
    
    public void validarEdicion() throws EntidadInvalidaException {
        if(!esEnteroPositivo(id)) throw new PeliculaInvalidaException("Id inválido");
        validarCampos();
    }
    
    public void validarCampos() throws EntidadInvalidaException{
        if(StringUtils.isBlank(titulo)) throw new PeliculaInvalidaException("Titulo inválido");
        if(StringUtils.isBlank(sinopsis)) throw new PeliculaInvalidaException("Sinopsis inválida");
        if(!fechaValida(fechaEstreno)) throw new PeliculaInvalidaException("Fecha inválida");
        if(!esEnteroPositivo(duracion)) throw new PeliculaInvalidaException("Duración inválida");
        if(StringUtils.isBlank(director)) throw new PeliculaInvalidaException("Director inválido");
        if(StringUtils.isBlank(reparto)) throw new PeliculaInvalidaException("Reparto inválido");
        
        clasificacion.validarEntrada();
        if(categorias == null || categorias.length < 1) throw new PeliculaInvalidaException("Ingresa al menos una categoria");
        for (CategoriaDTO categoria : categorias) {
            categoria.validarEntrada();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ImagenEntradaDTO getImagen() {
        return imagen;
    }

    public void setImagen(ImagenEntradaDTO imagen) {
        this.imagen = imagen;
    }

    public ClasificacionDTO getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(ClasificacionDTO clasificacion) {
        this.clasificacion = clasificacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getFechaEstreno() {
        return fechaEstreno;
    }

    public void setFechaEstreno(String fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getReparto() {
        return reparto;
    }

    public void setReparto(String reparto) {
        this.reparto = reparto;
    }

    public CategoriaDTO[] getCategorias() {
        return categorias;
    }

    public void setCategorias(CategoriaDTO[] categorias) {
        this.categorias = categorias;
    }

    public CalificacionDTO[] getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(CalificacionDTO[] calificaciones) {
        this.calificaciones = calificaciones;
    }

    public ComentarioDTO[] getComentarios() {
        return comentarios;
    }

    public void setComentarios(ComentarioDTO[] comentarios) {
        this.comentarios = comentarios;
    }

    
}
