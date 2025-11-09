/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.dtos;

import com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException;
import com.mynor.manejador.cines.api.excepciones.ProyeccionInvalidaException;

/**
 *
 * @author mynordma
 */
public class ProyeccionDTO extends Validador {
    
    private String id;
    private String idSala;
    private String idPelicula;
    private String nombrePelicula;
    private String fecha;
    private String hora;
    private String precio;
    
    @Override
    public void validarEntrada() throws EntidadInvalidaException {
        if(!esEnteroPositivo(idSala)) throw new ProyeccionInvalidaException("Id de la sala inválido");
        if(!esEnteroPositivo(idPelicula)) throw new ProyeccionInvalidaException("Id de la pelicula inválido");
        if(!fechaValida(fecha)) throw new ProyeccionInvalidaException("Fecha inválida");
        if(!horaValida(hora)) throw new ProyeccionInvalidaException("Hora inválida");
    }
    
    public void validarEdicion() throws EntidadInvalidaException{
        if(!esEnteroPositivo(id)) throw new ProyeccionInvalidaException("Id inválido");
        validarEntrada();
    }

    public String getNombrePelicula() {
        return nombrePelicula;
    }

    public void setNombrePelicula(String nombrePelicula) {
        this.nombrePelicula = nombrePelicula;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdSala() {
        return idSala;
    }

    public void setIdSala(String idSala) {
        this.idSala = idSala;
    }

    public String getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(String idPelicula) {
        this.idPelicula = idPelicula;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

}
