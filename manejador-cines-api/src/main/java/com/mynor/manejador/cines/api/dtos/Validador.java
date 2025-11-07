/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.dtos;

import com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException;
import com.mynor.manejador.cines.api.modelo.Rol;
import com.mynor.manejador.cines.api.modelo.TipoAnuncio;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author mynordma
 */
public abstract class Validador {
    
    public abstract void validarEntrada() throws EntidadInvalidaException;
    
    protected boolean esBoolean(String cadena) {
        if (StringUtils.isBlank(cadena)) {
            return false;
        }

        String valor = cadena.trim().toLowerCase();
        return valor.equals("true") || valor.equals("false");
    }
    
    protected boolean fechaValida(String fechaStr){
        try {
            LocalDate.parse(fechaStr);
            return true;
        } catch (DateTimeParseException | NullPointerException e) {
            return false;
        }
    }
    
    protected boolean horaValida(String horaStr){
        try {
            LocalTime.parse(horaStr);
            return true;
        } catch (DateTimeParseException | NullPointerException e) {
            return false;
        }
    }
    
    protected boolean montoValido(String montoStr) {
        try {
            double monto = Double.parseDouble(montoStr);
            return monto >= 0;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }
    
    protected boolean longitudValida(String valor, int longitudMaxima) {
        return !StringUtils.isBlank(valor)
                && valor.trim().length() <= longitudMaxima;
    }
    
    protected boolean esEnteroPositivo(String valor){
        try {
            int entero = Integer.parseInt(valor);
            return entero >= 0;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }
    
    protected boolean correoValido(String correo){
        return !StringUtils.isBlank(correo)
                && correo.trim().length() <= 350
                && correo.matches(".+@.+\\..+");
    }
    
    protected boolean rolValido(String rol){
        if (StringUtils.isBlank(rol)) return false;

        try {
            Rol.valueOf(rol);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    protected boolean tipoAnuncioValido(String tipo){
        if (StringUtils.isBlank(tipo)) return false;

        try {
            TipoAnuncio.valueOf(tipo);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
