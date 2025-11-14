/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.dtos.reportes;

/**
 *
 * @author mynordma
 */
public class UsuarioReporteDTO {
    
    private String nombre;
    private String correo;
    private String rol;
    private AnuncioReporteDTO anunciosComprados;

    public AnuncioReporteDTO getAnunciosComprados() {
        return anunciosComprados;
    }

    public void setAnunciosComprados(AnuncioReporteDTO anunciosComprados) {
        this.anunciosComprados = anunciosComprados;
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
