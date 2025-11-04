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
public class BloqueoAnunciosCineDTO extends Validador{
    
    private String idCine;
    private PagoEntradaDTO pago;
    private String dias;

    @Override
    public void validarEntrada() throws EntidadInvalidaException {
        if(!esEnteroPositivo(idCine)) throw new CineInvalidoException("Id del cine es inválido");
        pago.validarEntrada();
        if(!esEnteroPositivo(dias)) throw new CineInvalidoException("Los dias son inválidos");
    }

    public String getIdCine() {
        return idCine;
    }

    public void setIdCine(String idCine) {
        this.idCine = idCine;
    }

    public PagoEntradaDTO getPago() {
        return pago;
    }

    public void setPago(PagoEntradaDTO pago) {
        this.pago = pago;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }
    
}
