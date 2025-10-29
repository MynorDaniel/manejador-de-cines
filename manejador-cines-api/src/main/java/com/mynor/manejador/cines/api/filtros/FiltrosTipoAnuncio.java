/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.filtros;

import com.mynor.manejador.cines.api.modelo.TipoAnuncio;
import java.util.Optional;

/**
 *
 * @author mynordma
 */
public class FiltrosTipoAnuncio extends Filtros {
    
    private Optional<TipoAnuncio> tipo;

    public FiltrosTipoAnuncio() {
        this.tipo = Optional.empty();
    }

    public Optional<TipoAnuncio> getTipo() {
        return tipo;
    }

    public void setTipo(Optional<TipoAnuncio> tipo) {
        this.tipo = tipo;
    }
}
