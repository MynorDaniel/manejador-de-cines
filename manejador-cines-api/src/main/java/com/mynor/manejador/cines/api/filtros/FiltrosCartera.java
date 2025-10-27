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
public class FiltrosCartera extends Filtros {
    
    private Optional<Integer> idUsuario;

    public FiltrosCartera() {
        this.idUsuario = Optional.empty();
    }

    public Optional<Integer> getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Optional<Integer> idUsuario) {
        this.idUsuario = idUsuario;
    }
}
