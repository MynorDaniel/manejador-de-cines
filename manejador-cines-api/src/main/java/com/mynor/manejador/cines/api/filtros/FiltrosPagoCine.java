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
public class FiltrosPagoCine extends Filtros {
    
    private Optional<Integer> idCine;
    private Optional<Integer> idPago;
    
    public FiltrosPagoCine() {
        this.idCine = Optional.empty();
        this.idPago = Optional.empty();
    }

    public Optional<Integer> getIdCine() {
        return idCine;
    }

    public void setIdCine(Optional<Integer> idCine) {
        this.idCine = idCine;
    }

    public Optional<Integer> getIdPago() {
        return idPago;
    }

    public void setIdPago(Optional<Integer> idPago) {
        this.idPago = idPago;
    }
}
