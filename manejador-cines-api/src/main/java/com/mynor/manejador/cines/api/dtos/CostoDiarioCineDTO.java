/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.dtos;

import com.mynor.manejador.cines.api.excepciones.CineInvalidoException;
import com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException;

/**
 *
 * @author mynordma
 */
public class CostoDiarioCineDTO extends Validador {
    
    private String id;
    private String idCine;
    private String fechaCambio;
    private String monto;

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

    public String getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(String fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    @Override
    public void validarEntrada() throws EntidadInvalidaException {
        if(!esEnteroPositivo(idCine)) throw new CineInvalidoException("Id del cine inválido");
        if(!montoValido(monto)) throw new CineInvalidoException("Monto inválido");
    }
    
}
