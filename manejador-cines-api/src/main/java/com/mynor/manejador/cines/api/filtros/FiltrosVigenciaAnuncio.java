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
public class FiltrosVigenciaAnuncio extends Filtros {
    
    private Optional<Integer> dias;

    public FiltrosVigenciaAnuncio() {
        this.dias = Optional.empty();
    }

    public Optional<Integer> getDias() {
        return dias;
    }

    public void setDias(Optional<Integer> dias) {
        this.dias = dias;
    }
}
