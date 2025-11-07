/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.dtos;

import com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException;
import com.mynor.manejador.cines.api.excepciones.PeliculaInvalidaException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author mynordma
 */
public class ClasificacionDTO extends Validador {
    
    private String codigo;
    private String descripcion;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public void validarEntrada() throws EntidadInvalidaException {
        if(StringUtils.isBlank(codigo)) throw new PeliculaInvalidaException("Codigo inválido");
    }
}
