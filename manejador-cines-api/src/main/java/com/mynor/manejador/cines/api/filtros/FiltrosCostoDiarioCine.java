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
public class FiltrosCostoDiarioCine extends Filtros {
    
    private Optional<Integer> id;
    private Optional<Integer> idCine;

    public FiltrosCostoDiarioCine() {
        this.id = Optional.empty();
        this.idCine = Optional.empty();
    }

    public Optional<Integer> getId() {
        return id;
    }

    public void setId(Optional<Integer> id) {
        this.id = id;
    }

    public Optional<Integer> getIdCine() {
        return idCine;
    }

    public void setIdCine(Optional<Integer> idCine) {
        this.idCine = idCine;
    }
}
