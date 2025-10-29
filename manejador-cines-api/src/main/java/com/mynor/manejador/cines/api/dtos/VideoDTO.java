/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.dtos;

import com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException;
import com.mynor.manejador.cines.api.excepciones.VideoInvalidoException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author mynordma
 */
public class VideoDTO extends Validador {
    
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

    @Override
    public void validarEntrada() throws EntidadInvalidaException {
        if(StringUtils.isBlank(link)) throw new VideoInvalidoException("Enlace del video no existe");
    }
}
