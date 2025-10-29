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
public class CredencialesEntradaDTO extends Validador {
    
    private String correo;
    private String clave;

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
    
    @Override
    public void validarEntrada() throws UsuarioInvalidoException {
        if(!correoValido(correo)) throw new UsuarioInvalidoException("Correo inválido");
        if(StringUtils.isBlank(clave)) throw new UsuarioInvalidoException("Clave inválida");
    }
}
