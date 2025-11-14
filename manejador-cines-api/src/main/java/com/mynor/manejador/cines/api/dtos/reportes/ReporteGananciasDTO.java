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
public class ReporteGananciasDTO {
    
    private String costo;
    private String ingreso;
    private String ganancia;
    private List<CineReporteDTO> cines;
    private List<AnuncioReporteDTO> anuncios;
    private List<BloqueoAnunciosReporteDTO> bloqueos;

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }

    public String getIngreso() {
        return ingreso;
    }

    public void setIngreso(String ingreso) {
        this.ingreso = ingreso;
    }

    public String getGanancia() {
        return ganancia;
    }

    public void setGanancia(String ganancia) {
        this.ganancia = ganancia;
    }

    public List<CineReporteDTO> getCines() {
        return cines;
    }

    public void setCines(List<CineReporteDTO> cines) {
        this.cines = cines;
    }

    public List<AnuncioReporteDTO> getAnuncios() {
        return anuncios;
    }

    public void setAnuncios(List<AnuncioReporteDTO> anuncios) {
        this.anuncios = anuncios;
    }

    public List<BloqueoAnunciosReporteDTO> getBloqueos() {
        return bloqueos;
    }

    public void setBloqueos(List<BloqueoAnunciosReporteDTO> bloqueos) {
        this.bloqueos = bloqueos;
    }

}
