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
public class FiltrosCine extends Filtros {
    
    private Optional<Integer> id;
    private Optional<Integer> idUsuario;
    private Optional<Boolean> activado;

    public FiltrosCine() {
        this.id = Optional.empty();
        this.idUsuario = Optional.empty();
        this.activado = Optional.empty();
    }

    public Optional<Integer> getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Optional<Integer> idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Optional<Integer> getId() {
        return id;
    }

    public void setId(Optional<Integer> id) {
        this.id = id;
    }

    public Optional<Boolean> getActivado() {
        return activado;
    }

    public void setActivado(Optional<Boolean> activado) {
        this.activado = activado;
    }
}
