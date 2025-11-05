/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.dtos;

import com.mynor.manejador.cines.api.excepciones.CineInvalidoException;
import com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException;

/**
 *
 * @author mynordma
 */
public class CineDTO extends Validador {
    
    private String id;
    private String idUsuarioCreador;
    private String nombre;
    private String ubicacion;
    private String activado;
    private String fechaCreacion;
    private Boolean bloqueoActivo;
    private String fechaUltimoCambioDeCosto;

    public String getFechaUltimoCambioDeCosto() {
        return fechaUltimoCambioDeCosto;
    }

    public void setFechaUltimoCambioDeCosto(String fechaUltimoCambioDeCosto) {
        this.fechaUltimoCambioDeCosto = fechaUltimoCambioDeCosto;
    }
    
    @Override
    public void validarEntrada() throws EntidadInvalidaException {
        if(!longitudValida(nombre, 200)) throw new CineInvalidoException("Nombre del cine demasiado grande");
        if(!fechaValida(fechaCreacion)) throw new CineInvalidoException("Fecha inválida");
    }
    
    public void validarEdicion() throws CineInvalidoException {
        if(!longitudValida(nombre, 200)) throw new CineInvalidoException("Nombre del cine demasiado grande");
        if(!esBoolean(activado)) throw new CineInvalidoException("Estado del cine inválido");
        if(!esEnteroPositivo(idUsuarioCreador)) throw new CineInvalidoException("Id del usuario inválido");
        if(!esEnteroPositivo(id)) throw new CineInvalidoException("Id del cine inválido");
    }

    public Boolean getBloqueoActivo() {
        return bloqueoActivo;
    }

    public void setBloqueoActivo(Boolean bloqueoActivo) {
        this.bloqueoActivo = bloqueoActivo;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuarioCreador() {
        return idUsuarioCreador;
    }

    public void setIdUsuarioCreador(String idUsuarioCreador) {
        this.idUsuarioCreador = idUsuarioCreador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getActivado() {
        return activado;
    }

    public void setActivado(String activado) {
        this.activado = activado;
    }
    
}
