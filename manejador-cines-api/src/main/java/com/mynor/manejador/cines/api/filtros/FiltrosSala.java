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
public class FiltrosSala extends Filtros{
    
    private Optional<Integer> id;
    private Optional<Integer> idCine;
    private Optional<Boolean> visible;
    private Optional<Integer> idCreador;
    
    public FiltrosSala(){
        id = Optional.empty();
        idCine = Optional.empty();
        visible = Optional.empty();
        idCreador = Optional.empty();
    }

    public Optional<Integer> getIdCreador() {
        return idCreador;
    }

    public void setIdCreador(Optional<Integer> idCreador) {
        this.idCreador = idCreador;
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

    public Optional<Boolean> getVisible() {
        return visible;
    }

    public void setVisible(Optional<Boolean> visible) {
        this.visible = visible;
    }
    
}
