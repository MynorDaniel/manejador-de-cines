/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.filtros;

import com.mynor.manejador.cines.api.modelo.TipoAnuncio;
import java.time.LocalDate;
import java.util.Optional;

/**
 *
 * @author mynordma
 */
public class FiltrosAnuncio extends Filtros {
    
    private Optional<Integer> id;
    private Optional<TipoAnuncio> tipo;
    private Optional<Integer> vigencia;
    private Optional<Integer> idUsuario;
    private Optional<LocalDate> fechaInicial;
    private Optional<LocalDate> fechaFinal;

    public FiltrosAnuncio() {
        this.id = Optional.empty();
        this.tipo = Optional.empty();
        this.vigencia = Optional.empty();
        this.idUsuario = Optional.empty();
        this.fechaInicial = Optional.empty();
        this.fechaFinal = Optional.empty();
    }

    public Optional<Integer> getId() {
        return id;
    }

    public void setId(Optional<Integer> id) {
        this.id = id;
    }

    public Optional<TipoAnuncio> getTipo() {
        return tipo;
    }

    public void setTipo(Optional<TipoAnuncio> tipo) {
        this.tipo = tipo;
    }

    public Optional<Integer> getVigencia() {
        return vigencia;
    }

    public void setVigencia(Optional<Integer> vigencia) {
        this.vigencia = vigencia;
    }

    public Optional<Integer> getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Optional<Integer> idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Optional<LocalDate> getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Optional<LocalDate> fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Optional<LocalDate> getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Optional<LocalDate> fechaFinal) {
        this.fechaFinal = fechaFinal;
    }
}
