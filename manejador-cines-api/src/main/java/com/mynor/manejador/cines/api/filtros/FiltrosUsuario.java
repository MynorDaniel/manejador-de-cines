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
public class FiltrosUsuario extends Filtros {
    
    private Optional<Integer> id;
    private Optional<String> correo;
    private Optional<Boolean> activado;

    public FiltrosUsuario() {
        this.id = Optional.empty();
        this.correo = Optional.empty();
        this.activado = Optional.empty();
    }

    public Optional<Integer> getId() {
        return id;
    }

    public void setId(Optional<Integer> id) {
        this.id = id;
    }

    public Optional<String> getCorreo() {
        return correo;
    }

    public void setCorreo(Optional<String> correo) {
        this.correo = correo;
    }

    public Optional<Boolean> getActivado() {
        return activado;
    }

    public void setActivado(Optional<Boolean> activado) {
        this.activado = activado;
    }

    
}
