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
public class TipoAnuncioPrecioDTO extends Validador{
    
    private String tipo;
    private String monto;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    @Override
    public void validarEntrada() throws EntidadInvalidaException {
        if(!tipoAnuncioValido(tipo)) throw new AnuncioInvalidoException("Tipo de anuncio inválido");
        if(!montoValido(monto)) throw new AnuncioInvalidoException("Monto incorrecto");
    }
    
    
    
}
