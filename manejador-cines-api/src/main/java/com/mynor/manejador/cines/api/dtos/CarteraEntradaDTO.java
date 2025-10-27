/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.dtos;

import com.mynor.manejador.cines.api.excepciones.UsuarioInvalidoException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author mynordma
 */
public class CarteraEntradaDTO extends Validador {
    
    private String usuarioId;
    private String saldo;

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }
    
    public void validarEntrada() throws UsuarioInvalidoException{
        if(!esEnteroPositivo(usuarioId)) throw new UsuarioInvalidoException("ID de imagen incorrecto");
        if(!montoValido(saldo)) throw new UsuarioInvalidoException("Monto incorrecto");
    }
}
