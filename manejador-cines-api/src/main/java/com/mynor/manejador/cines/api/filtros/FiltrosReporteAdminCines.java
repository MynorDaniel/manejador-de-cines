/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.filtros;

import com.mynor.manejador.cines.api.dtos.Validador;
import com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException;
import com.mynor.manejador.cines.api.excepciones.ReporteInvalidoException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mynordma
 */
public class FiltrosReporteAdminCines extends FiltrosReportes{
    private String fechaInicio;
    private String fechaFin;
    private String idSala;

    public Map<String, Object> obtenerParametros() {
        Map<String, Object> parametros = new HashMap<>();
        
        if(fechaInicio != null && !fechaInicio.isEmpty() && 
            fechaFin != null && !fechaFin.isEmpty()) {
            parametros.put("fechaInicio", fechaInicio);
            parametros.put("fechaFin", fechaFin);
            parametros.put("existeFecha", true);
        }else{
            parametros.put("existeFecha", false);
        }
        
        if(idSala != null){
            parametros.put("idSala", idSala);
            parametros.put("existeIdSala", true);
        }else{
            parametros.put("existeIdSala", false);
        }
        
        return parametros;
    }

    @Override
    public void validarEntrada() throws EntidadInvalidaException {
        if(fechaInicio != null && !fechaInicio.isEmpty() && 
            fechaFin != null && !fechaFin.isEmpty()) {
            if(!fechaValida(fechaFin)) throw new ReporteInvalidoException("Fecha invalida");
            if(!fechaValida(fechaInicio)) throw new ReporteInvalidoException("Fecha invalida");
        }
        
        if(idSala != null){
            if(!esEnteroPositivo(idSala)) throw new ReporteInvalidoException("Id de la sala invalido");
        }
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getIdSala() {
        return idSala;
    }

    public void setIdSala(String idSala) {
        this.idSala = idSala;
    }
}
