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
public class CostoGlobalCinesDTO extends Validador{

    private String id;
    private String monto;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }
    
    @Override
    public void validarEntrada() throws EntidadInvalidaException {
        if(!montoValido(monto)) throw new CineInvalidoException("Monto inválido");
        if(!esEnteroPositivo(id)) throw new CineInvalidoException("Id inválido");
    }
    
}
