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
public class FiltrosBloqueoAnunciosCine extends Filtros {
    
    private Optional<Integer> idCine;
    
    public FiltrosBloqueoAnunciosCine() {
        this.idCine = Optional.empty();
    }

    public Optional<Integer> getIdCine() {
        return idCine;
    }

    public void setIdCine(Optional<Integer> idCine) {
        this.idCine = idCine;
    }
}
