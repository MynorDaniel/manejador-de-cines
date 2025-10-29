/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.dtos;

import com.mynor.manejador.cines.api.excepciones.UsuarioInvalidoException;

/**
 *
 * @author mynordma
 */
public class UsuarioEditadoDTO extends Validador {
    
    private String id;
    private String nombre;
    private String correo;
    private String clave;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
    
    /**
     * Todos los campos no pueden ser nulos se hayan editado o no, a excepcion de la clave
     * @throws UsuarioInvalidoException 
     */
    @Override
    public void validarEntrada() throws UsuarioInvalidoException {
        if(!correoValido(correo)) throw new UsuarioInvalidoException("Correo inválido");
        
        if(!longitudValida(correo, 200)) throw new UsuarioInvalidoException("Nombre inválido");
        if(!esEnteroPositivo(id)) throw new UsuarioInvalidoException("ID incorrecto");
    }
}
