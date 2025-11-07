/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.dtos;

import com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException;
import com.mynor.manejador.cines.api.excepciones.InteraccionInvalidaException;

/**
 *
 * @author mynordma
 */
public class CalificacionDTO extends Validador {
    
    private String id;
    private String idUsuario;
    private String valor;
    private String fecha;

    @Override
    public void validarEntrada() throws EntidadInvalidaException {
        if(!esEnteroPositivo(valor) || Integer.parseInt(valor) == 0 || Integer.parseInt(valor) > 5) throw new InteraccionInvalidaException("Valor inválido");
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

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    
}
