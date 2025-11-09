/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.dtos;

import com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException;
import com.mynor.manejador.cines.api.excepciones.InteraccionInvalidaException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author mynordma
 */
public class ComentarioDTO extends Validador {
    
    private String id;
    private String idUsuario;
    private String contenido;
    private String fecha;
    private UsuarioSalidaDTO usuario;

    @Override
    public void validarEntrada() throws EntidadInvalidaException {
        if(StringUtils.isBlank(contenido)) throw new InteraccionInvalidaException("Contenido inválido");
    }
    
    public void validarEdicion() throws EntidadInvalidaException {
        if(StringUtils.isBlank(contenido)) throw new InteraccionInvalidaException("Contenido inválido");
        if(!esEnteroPositivo(id))throw new InteraccionInvalidaException("Id inválido");
    }

    public UsuarioSalidaDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioSalidaDTO usuario) {
        this.usuario = usuario;
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

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    
}
