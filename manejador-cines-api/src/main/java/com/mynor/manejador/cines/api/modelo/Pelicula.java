/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.modelo;

import java.time.LocalDate;

/**
 *
 * @author mynordma
 */
public class Pelicula {
    
    private Integer id;
    private Imagen imagen;
    private Clasificacion clasificacion;
    private String titulo;
    private String sinopsis;
    private LocalDate fechaEstreno;
    private Integer duracion;
    private String director;
    private String reparto;
    private Categoria[] categorias;
    private Calificacion[] calificaciones;
    private Comentario[] comentarios;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Imagen getImagen() {
        return imagen;
    }

    public void setImagen(Imagen imagen) {
        this.imagen = imagen;
    }

    public Clasificacion getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(Clasificacion clasificacion) {
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

    public LocalDate getFechaEstreno() {
        return fechaEstreno;
    }

    public void setFechaEstreno(LocalDate fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
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

    public Categoria[] getCategorias() {
        return categorias;
    }

    public void setCategorias(Categoria[] categorias) {
        this.categorias = categorias;
    }

    public Calificacion[] getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(Calificacion[] calificaciones) {
        this.calificaciones = calificaciones;
    }

    public Comentario[] getComentarios() {
        return comentarios;
    }

    public void setComentarios(Comentario[] comentarios) {
        this.comentarios = comentarios;
    }
}
