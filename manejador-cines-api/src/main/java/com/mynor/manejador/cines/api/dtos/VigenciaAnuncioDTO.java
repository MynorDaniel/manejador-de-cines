/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.dtos;

import com.mynor.manejador.cines.api.excepciones.AnuncioInvalidoException;
import com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException;

/**
 *
 * @author mynordma
 */
public class VigenciaAnuncioDTO extends Validador {
    
    private String dias;
    private String monto;

    public String getDias() {
        return dias;
    }

    public void setDias(String tipo) {
        this.dias = tipo;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    @Override
    public void validarEntrada() throws EntidadInvalidaException {
        if(!esEnteroPositivo(dias)) throw new AnuncioInvalidoException("Vigencia de anuncio inválido");
        if(!montoValido(monto)) throw new AnuncioInvalidoException("Monto incorrecto");
    }
    
    
}
