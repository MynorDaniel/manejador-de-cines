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
public class FiltrosImagen {
    
    private Optional<Integer> id;
    private Optional<String> link;
    private Optional<Boolean> ultimoId;
    private Optional<Integer> idAnuncio;

    public Optional<Integer> getIdAnuncio() {
        return idAnuncio;
    }

    public void setIdAnuncio(Optional<Integer> idAnuncio) {
        this.idAnuncio = idAnuncio;
    }

    public Optional<Boolean> getUltimoId() {
        return ultimoId;
    }

    public void setUltimoId(Optional<Boolean> ultimoId) {
        this.ultimoId = ultimoId;
    }

    public FiltrosImagen() {
        this.ultimoId = Optional.empty();
        this.id = Optional.empty();
        this.link = Optional.empty();
        this.idAnuncio = Optional.empty();
    }

    public Optional<Integer> getId() {
        return id;
    }

    public void setId(Optional<Integer> id) {
        this.id = id;
    }

    public Optional<String> getLink() {
        return link;
    }

    public void setLink(Optional<String> link) {
        this.link = link;
    }
}
