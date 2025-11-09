/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.filtros;

import java.util.Optional;

/**
 *
 * @author mynordma
 */
public class FiltrosComentarioSala extends Filtros {
    
    private Optional<Integer> idSala;
    private Optional<Integer> idComentario;

    public FiltrosComentarioSala() {
        idSala = Optional.empty();
        idComentario = Optional.empty();
    }

    public Optional<Integer> getIdSala() {
        return idSala;
    }

    public void setIdSala(Optional<Integer> idSala) {
        this.idSala = idSala;
    }

    public Optional<Integer> getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(Optional<Integer> idComentario) {
        this.idComentario = idComentario;
    }
}
