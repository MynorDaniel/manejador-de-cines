/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.dtos.reportes;

import java.util.List;

/**
 *
 * @author mynordma
 */
public class SalaReporteDTO {
    
    private String id;
    private String nombreCine;
    private String filas;
    private String columnas;
    private String calificacionesBloqueadas;
    private String comentariosBloqueados;
    private String visible;
    
    private String promedioCalificacion;
    private String totalDineroBoletosVendidos;
    
    private List<ComentarioReporteDTO> comentarios;
    private List<ProyeccionReporteDTO> proyecciones;
    private List<CalificacionReporteDTO> calificaciones;
    private List<BoletoReporteDTO> boletos;

    public String getTotalDineroBoletosVendidos() {
        return totalDineroBoletosVendidos;
    }

    public void setTotalDineroBoletosVendidos(String totalDineroBoletosVendidos) {
        this.totalDineroBoletosVendidos = totalDineroBoletosVendidos;
    }

    public String getPromedioCalificacion() {
        return promedioCalificacion;
    }

    public void setPromedioCalificacion(String promedioCalificacion) {
        this.promedioCalificacion = promedioCalificacion;
    }

    public List<ComentarioReporteDTO> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<ComentarioReporteDTO> comentarios) {
        this.comentarios = comentarios;
    }

    public List<ProyeccionReporteDTO> getProyecciones() {
        return proyecciones;
    }

    public void setProyecciones(List<ProyeccionReporteDTO> proyecciones) {
        this.proyecciones = proyecciones;
    }

    public List<CalificacionReporteDTO> getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(List<CalificacionReporteDTO> calificaciones) {
        this.calificaciones = calificaciones;
    }

    public List<BoletoReporteDTO> getBoletos() {
        return boletos;
    }

    public void setBoletos(List<BoletoReporteDTO> boletos) {
        this.boletos = boletos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreCine() {
        return nombreCine;
    }

    public void setNombreCine(String nombreCine) {
        this.nombreCine = nombreCine;
    }

    public String getFilas() {
        return filas;
    }

    public void setFilas(String filas) {
        this.filas = filas;
    }

    public String getColumnas() {
        return columnas;
    }

    public void setColumnas(String columnas) {
        this.columnas = columnas;
    }

    public String getCalificacionesBloqueadas() {
        return calificacionesBloqueadas;
    }

    public void setCalificacionesBloqueadas(String calificacionesBloqueadas) {
        if(calificacionesBloqueadas.trim().toLowerCase().equals("true")){
            this.calificacionesBloqueadas = "Si";
        }else{
            this.calificacionesBloqueadas = "No";
        }
    }

    public String getComentariosBloqueados() {
        return comentariosBloqueados;
    }

    public void setComentariosBloqueados(String comentariosBloqueados) {
        if(comentariosBloqueados.trim().toLowerCase().equals("true")){
            this.comentariosBloqueados = "Si";
        }else{
            this.comentariosBloqueados = "No";
        }
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        if(visible.trim().toLowerCase().equals("true")){
            this.visible = "Si";
        }else{
            this.visible = "No";
        }
    }
}
