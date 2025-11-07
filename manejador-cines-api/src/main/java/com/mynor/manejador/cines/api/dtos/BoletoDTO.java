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

    @Override
    public void validarEntrada() throws EntidadInvalidaException {
        if(!esEnteroPositivo(idProyeccion)) throw new BoletoInvalidoException("proyeccion inválida");
        pagoDTO.validarEntrada();
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
