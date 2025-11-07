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
public class FiltrosPelicula extends Filtros {
    
    private Optional<Integer> id;
    private Optional<String> titulo;
    private Optional<Integer> idCategoria;

    public FiltrosPelicula() {
        id = Optional.empty();
        titulo = Optional.empty();
        idCategoria = Optional.empty();
    }

    public Optional<Integer> getId() {
        return id;
    }

    public void setId(Optional<Integer> id) {
        this.id = id;
    }

    public Optional<String> getTitulo() {
        return titulo;
    }

    public void setTitulo(Optional<String> titulo) {
        this.titulo = titulo;
    }

    public Optional<Integer> getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Optional<Integer> idCategoria) {
        this.idCategoria = idCategoria;
    }
}
