/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.dtos;

import com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException;
import com.mynor.manejador.cines.api.excepciones.SalaInvalidaException;

/**
 *
 * @author mynordma
 */
public class SalaDTO extends Validador {
    
    private String id;
    private String idCine;
    private String filasAsientos;
    private String columnasAsientos;
    private String calificacionesBloqueadas;
    private String comentariosBloqueados;
    private String visible;
    private Integer calificacion;
    private Integer calificacionDelUsuarioActual;
    private Integer idCalificacionUsuario;

    @Override
    public void validarEntrada() throws EntidadInvalidaException {
        if(!esEnteroPositivo(idCine)) throw new SalaInvalidaException("Id del cine inválido");
        if(!esEnteroPositivo(filasAsientos)) throw new SalaInvalidaException("filas inválidas");
        if(!esEnteroPositivo(columnasAsientos)) throw new SalaInvalidaException("columnas inválidas");
    }
    
    public void validarEdicion() throws SalaInvalidaException {
        if(!esEnteroPositivo(id)) throw new SalaInvalidaException("Id del cine inválido");
        if(!esEnteroPositivo(filasAsientos)) throw new SalaInvalidaException("filas inválidas");
        if(!esEnteroPositivo(columnasAsientos)) throw new SalaInvalidaException("columnas inválidas");
        if(!esBoolean(calificacionesBloqueadas)) throw new SalaInvalidaException("Estado de las calificaciones inválido");
        if(!esBoolean(comentariosBloqueados)) throw new SalaInvalidaException("Estado de los comentarios inválido");
        if(!esBoolean(visible)) throw new SalaInvalidaException("Estado de visibilidad inválido");
    }

    public Integer getIdCalificacionUsuario() {
        return idCalificacionUsuario;
    }

    public void setIdCalificacionUsuario(Integer idCalificacionUsuario) {
        this.idCalificacionUsuario = idCalificacionUsuario;
    }

    public Integer getCalificacionDelUsuarioActual() {
        return calificacionDelUsuarioActual;
    }

    public void setCalificacionDelUsuarioActual(Integer calificacionDelUsuarioActual) {
        this.calificacionDelUsuarioActual = calificacionDelUsuarioActual;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCine() {
        return idCine;
    }

    public void setIdCine(String idCine) {
        this.idCine = idCine;
    }

    public String getFilasAsientos() {
        return filasAsientos;
    }

    public void setFilasAsientos(String filasAsientos) {
        this.filasAsientos = filasAsientos;
    }

    public String getColumnasAsientos() {
        return columnasAsientos;
    }

    public void setColumnasAsientos(String columnasAsientos) {
        this.columnasAsientos = columnasAsientos;
    }

    public String getCalificacionesBloqueadas() {
        return calificacionesBloqueadas;
    }

    public void setCalificacionesBloqueadas(String calificacionesBloqueadas) {
        this.calificacionesBloqueadas = calificacionesBloqueadas;
    }

    public String getComentariosBloqueados() {
        return comentariosBloqueados;
    }

    public void setComentariosBloqueados(String comentariosBloqueados) {
        this.comentariosBloqueados = comentariosBloqueados;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }
    
}
