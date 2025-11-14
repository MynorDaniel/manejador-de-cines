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
public class ReportePeliculasProyectadasDTO {
    
    private List<SalaReporteDTO> salas;

    public List<SalaReporteDTO> getSalas() {
        return salas;
    }

    public void setSalas(List<SalaReporteDTO> salas) {
        this.salas = salas;
    }
}
