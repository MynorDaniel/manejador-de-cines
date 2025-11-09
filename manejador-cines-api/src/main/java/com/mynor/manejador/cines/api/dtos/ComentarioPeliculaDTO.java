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
public class ComentarioPeliculaDTO extends Validador{
    
    private ComentarioDTO comentario;
    private String idPelicula;

    @Override
    public void validarEntrada() throws EntidadInvalidaException {
        comentario.validarEntrada();
        if(!esEnteroPositivo(idPelicula)) throw new InteraccionInvalidaException("Id de la pelicula inválido");
    }

    public ComentarioDTO getComentario() {
        return comentario;
    }

    public void setComentario(ComentarioDTO comentario) {
        this.comentario = comentario;
    }

    public String getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(String idPelicula) {
        this.idPelicula = idPelicula;
    }
}
