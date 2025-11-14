/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.dtos.reportes;

/**
 *
 * @author mynordma
 */
public class BloqueoAnunciosReporteDTO {
    
    private String montoPagado;
    private String dias;
    private String administradorCine;

    public String getAdministradorCine() {
        return administradorCine;
    }

    public void setAdministradorCine(String administradorCine) {
        this.administradorCine = administradorCine;
    }

    public String getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(String montoPagado) {
        this.montoPagado = montoPagado;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }
}
