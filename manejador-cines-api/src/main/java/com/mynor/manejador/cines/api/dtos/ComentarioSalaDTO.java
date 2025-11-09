/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.dtos;

import com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException;
import com.mynor.manejador.cines.api.excepciones.InteraccionInvalidaException;

/**
 *
 * @author mynordma
 */
public class ComentarioSalaDTO extends Validador{
    
    private ComentarioDTO comentario;
    private String idSala;

    @Override
    public void validarEntrada() throws EntidadInvalidaException {
        comentario.validarEntrada();
        if(!esEnteroPositivo(idSala)) throw new InteraccionInvalidaException("Id de la sala inválido");
    }

    public ComentarioDTO getComentario() {
        return comentario;
    }

    public void setComentario(ComentarioDTO comentario) {
        this.comentario = comentario;
    }

    public String getIdSala() {
        return idSala;
    }

    public void setIdSala(String idSala) {
        this.idSala = idSala;
    }
}
