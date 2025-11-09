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
public class FiltrosComentarioPelicula extends Filtros {
    
    private Optional<Integer> idPelicula;
    private Optional<Integer> idComentario;

    public FiltrosComentarioPelicula() {
        idPelicula = Optional.empty();
        idComentario = Optional.empty();
    }

    public Optional<Integer> getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(Optional<Integer> idPelicula) {
        this.idPelicula = idPelicula;
    }

    public Optional<Integer> getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(Optional<Integer> idComentario) {
        this.idComentario = idComentario;
    }
    
}
