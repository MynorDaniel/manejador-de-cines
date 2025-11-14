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
public class ReporteAnunciosCompradosDTO {
    
    private List<AnuncioReporteDTO> anuncios;

    public List<AnuncioReporteDTO> getAnuncios() {
        return anuncios;
    }

    public void setAnuncios(List<AnuncioReporteDTO> anuncios) {
        this.anuncios = anuncios;
    }

}
