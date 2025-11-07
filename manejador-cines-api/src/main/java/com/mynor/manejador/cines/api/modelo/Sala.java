/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.modelo;

/**
 *
 * @author mynordma
 */
public class Sala {
    
    private Integer id;
    private Cine cine;
    private Integer filasAsientos;
    private Integer columnasAsientos;
    private Boolean calificacionesBloqueadas;
    private Boolean comentariosBloqueados;
    private Boolean visible;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cine getCine() {
        return cine;
    }

    public void setCine(Cine cine) {
        this.cine = cine;
    }

    public Integer getFilasAsientos() {
        return filasAsientos;
    }

    public void setFilasAsientos(Integer filasAsientos) {
        this.filasAsientos = filasAsientos;
    }

    public Integer getColumnasAsientos() {
        return columnasAsientos;
    }

    public void setColumnasAsientos(Integer columnasAsientos) {
        this.columnasAsientos = columnasAsientos;
    }

    public Boolean getCalificacionesBloqueadas() {
        return calificacionesBloqueadas;
    }

    public void setCalificacionesBloqueadas(Boolean calificacionesBloqueadas) {
        this.calificacionesBloqueadas = calificacionesBloqueadas;
    }

    public Boolean getComentariosBloqueados() {
        return comentariosBloqueados;
    }

    public void setComentariosBloqueados(Boolean comentariosBloqueados) {
        this.comentariosBloqueados = comentariosBloqueados;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
    
    
}
