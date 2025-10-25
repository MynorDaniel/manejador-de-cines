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
public class ImagenEntradaDTO extends Validador {
    
    private String id;
    private String link;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
    
    public void validarEntrada() throws UsuarioInvalidoException{
        if(!esEnteroPositivo(id)) throw new UsuarioInvalidoException("ID de imagen incorrecto");
        if(StringUtils.isBlank(link)) throw new UsuarioInvalidoException("Link de imagen incorrecto");
    }
}
