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
public class FiltrosBoleto extends Filtros {
    
    private Optional<Integer> id;
    private Optional<Integer> idUsuario;
    private Optional<Integer> idSala;
    
    public FiltrosBoleto(){
        id = Optional.empty();
        idUsuario = Optional.empty();
        idSala = Optional.empty();
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

    public Optional<Integer> getIdSala() {
        return idSala;
    }

    public void setIdSala(Optional<Integer> idSala) {
        this.idSala = idSala;
    }
}
