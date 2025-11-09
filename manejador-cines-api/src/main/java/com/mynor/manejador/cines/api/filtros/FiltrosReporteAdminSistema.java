/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.filtros;

import com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException;
import com.mynor.manejador.cines.api.excepciones.ReporteInvalidoException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author mynordma
 */
public class FiltrosReporteAdminSistema extends FiltrosReportes {
    
    private String fechaInicio;
    private String fechaFin;
    private String tipoAnuncio;
    private String vigencia;
    private String anuncianteId;

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
        
        if(tipoAnuncio != null){
            parametros.put("tipoAnuncio", tipoAnuncio);
            parametros.put("existeTipo", true);
        }else{
            parametros.put("existeTipo", false);
        }
        
        if(vigencia != null){
            parametros.put("vigenciaDias", vigencia);
            parametros.put("existeVigencia", true);
        }else{
            parametros.put("existeVigencia", false);
        }
        
        if(anuncianteId != null){
            parametros.put("usuarioId", anuncianteId);
            parametros.put("existeUsuario", true);
        }else{
            parametros.put("existeUsuario", false);
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
        
        if(tipoAnuncio != null){
            if(StringUtils.isBlank(tipoAnuncio)) throw new ReporteInvalidoException("Tipo invalido");
        }
        
        if(vigencia != null){
            if(!esEnteroPositivo(vigencia)) throw new ReporteInvalidoException("Vigencia invalida");
        }
        
        if(anuncianteId != null){
            if(!esEnteroPositivo(anuncianteId)) throw new ReporteInvalidoException("Anunciante invalido");
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

    public String getTipoAnuncio() {
        return tipoAnuncio;
    }

    public void setTipoAnuncio(String tipoAnuncio) {
        this.tipoAnuncio = tipoAnuncio;
    }

    public String getVigencia() {
        return vigencia;
    }

    public void setVigencia(String vigencia) {
        this.vigencia = vigencia;
    }

    public String getAnuncianteId() {
        return anuncianteId;
    }

    public void setAnuncianteId(String anuncianteId) {
        this.anuncianteId = anuncianteId;
    }
    
}
