/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.dtos;

import com.mynor.manejador.cines.api.excepciones.BoletoInvalidoException;
import com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException;

/**
 *
 * @author mynordma
 */
public class BoletoDTO extends Validador {
    
    private String id;
    private String idUsuario;
    private String idProyeccion;
    private PagoEntradaDTO pagoDTO;
    private CineDTO cine;
    private SalaDTO sala;
    private PeliculaDTO pelicula;
    private ProyeccionDTO proyeccion;

    @Override
    public void validarEntrada() throws EntidadInvalidaException {
        if(!esEnteroPositivo(idProyeccion)) throw new BoletoInvalidoException("proyeccion inválida");
        pagoDTO.validarEntrada();
    }

    public CineDTO getCine() {
        return cine;
    }

    public ProyeccionDTO getProyeccion() {
        return proyeccion;
    }

    public void setProyeccion(ProyeccionDTO proyeccion) {
        this.proyeccion = proyeccion;
    }

    public void setCine(CineDTO cine) {
        this.cine = cine;
    }

    public SalaDTO getSala() {
        return sala;
    }

    public void setSala(SalaDTO sala) {
        this.sala = sala;
    }

    public PeliculaDTO getPelicula() {
        return pelicula;
    }

    public void setPelicula(PeliculaDTO pelicula) {
        this.pelicula = pelicula;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdProyeccion() {
        return idProyeccion;
    }

    public void setIdProyeccion(String idProyeccion) {
        this.idProyeccion = idProyeccion;
    }

    public PagoEntradaDTO getPagoDTO() {
        return pagoDTO;
    }

    public void setPagoDTO(PagoEntradaDTO pagoDTO) {
        this.pagoDTO = pagoDTO;
    }

}
