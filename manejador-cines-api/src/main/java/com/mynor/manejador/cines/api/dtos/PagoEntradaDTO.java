/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.dtos;

import com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException;
import com.mynor.manejador.cines.api.excepciones.PagoInvalidoException;
import com.mynor.manejador.cines.api.excepciones.UsuarioInvalidoException;

/**
 *
 * @author mynordma
 */
public class PagoEntradaDTO extends Validador {
    
    private String id;
    private String idUsuario;
    private String fecha;
    private String monto;

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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    @Override
    public void validarEntrada() throws EntidadInvalidaException {
        if(!esEnteroPositivo(idUsuario)) throw new UsuarioInvalidoException("Id del usuario incorrecto");
        if(!montoValido(monto)) throw new PagoInvalidoException("Monto incorrecto");
        if(!fechaValida(fecha)) throw new PagoInvalidoException("Fecha incorrecta");
    }
    
    
}
