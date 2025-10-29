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
public class FiltrosPago extends Filtros {
    
    private Optional<Integer> id;
    private Optional<Integer> idUsuario;

    public FiltrosPago() {
        this.id = Optional.empty();
        this.idUsuario = Optional.empty();
    }

    public Optional<Integer> getId() {
        return id;
    }

    public void setId(Optional<Integer> id) {
        this.id = id;
    }

    public Optional<Integer> getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Optional<Integer> idUsuario) {
        this.idUsuario = idUsuario;
    }
}
