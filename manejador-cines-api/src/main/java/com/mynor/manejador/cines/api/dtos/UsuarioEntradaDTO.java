/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.dtos;

import com.mynor.manejador.cines.api.excepciones.UsuarioInvalidoException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author mynordma
 */
public class UsuarioEntradaDTO extends Validador {
    
    private String nombre;
    private String rol;
    private String correo;
    private String clave;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public void validarEntrada() throws UsuarioInvalidoException {
        if(!correoValido(correo)) throw new UsuarioInvalidoException("Correo inválido");
        if(StringUtils.isBlank(clave)) throw new UsuarioInvalidoException("Clave inválida");
        if(!longitudValida(correo, 200)) throw new UsuarioInvalidoException("Nombre inválido");
        if(!rolValido(rol)) throw new UsuarioInvalidoException("Rol no permitido");
    }
    
}
