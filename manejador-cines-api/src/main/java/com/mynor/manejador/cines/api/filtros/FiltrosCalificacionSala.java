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
public class FiltrosCalificacionSala extends Filtros {
    
    private Optional<Integer> idCalificacion;
    private Optional<Integer> idSala;
    private Optional<Integer> idUsuario;
    
    public FiltrosCalificacionSala(){
        idCalificacion = Optional.empty();
        idSala = Optional.empty();
        idUsuario = Optional.empty();
    }

    public Optional<Integer> getIdCalificacion() {
        return idCalificacion;
    }

    public void setIdCalificacion(Optional<Integer> idCalificacion) {
        this.idCalificacion = idCalificacion;
    }

    public Optional<Integer> getIdSala() {
        return idSala;
    }

    public void setIdSala(Optional<Integer> idSala) {
        this.idSala = idSala;
    }

    public Optional<Integer> getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Optional<Integer> idUsuario) {
        this.idUsuario = idUsuario;
    }
}
