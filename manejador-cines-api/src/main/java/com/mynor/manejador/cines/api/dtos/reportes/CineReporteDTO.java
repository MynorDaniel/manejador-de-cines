/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.dtos.reportes;

/**
 *
 * @author mynordma
 */
public class CineReporteDTO {
    
    private String idCine;
    private String nombre;
    private String montoAdeudado;
    private String montoPagado;

    public String getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(String montoPagado) {
        this.montoPagado = montoPagado;
    }

    public String getMontoAdeudado() {
        return montoAdeudado;
    }

    public void setMontoAdeudado(String montoAdeudado) {
        this.montoAdeudado = montoAdeudado;
    }

    public String getIdCine() {
        return idCine;
    }

    public void setIdCine(String idCine) {
        this.idCine = idCine;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
