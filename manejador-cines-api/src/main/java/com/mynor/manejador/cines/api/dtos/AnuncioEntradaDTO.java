/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.dtos;

import com.mynor.manejador.cines.api.excepciones.AnuncioInvalidoException;
import com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException;
import com.mynor.manejador.cines.api.modelo.TipoAnuncio;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author mynordma
 */
public class AnuncioEntradaDTO extends Validador {
    
    private String vigencia;
    private String tipo;
    private String texto;
    private PagoEntradaDTO pago;
    private String idMedia;
    private String activado;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActivado() {
        return activado;
    }

    public void setActivado(String activado) {
        this.activado = activado;
    }

    public String getVigencia() {
        return vigencia;
    }

    public void setVigencia(String vigencia) {
        this.vigencia = vigencia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public PagoEntradaDTO getPago() {
        return pago;
    }

    public void setPago(PagoEntradaDTO pago) {
        this.pago = pago;
    }

    public String getIdMedia() {
        return idMedia;
    }

    public void setIdMedia(String idMedia) {
        this.idMedia = idMedia;
    }

    @Override
    public void validarEntrada() throws EntidadInvalidaException {
        if(!esEnteroPositivo(vigencia)) throw new AnuncioInvalidoException("Vigencia debe ser un entero positivo");
        if(!tipoAnuncioValido(tipo)) throw new AnuncioInvalidoException("Tipo de anuncio no coincide con los esperados");
        if(StringUtils.isBlank(texto)) throw new AnuncioInvalidoException("Texto no puede ir vacío");
        
        if(!esSoloTexto(tipo)){
            if(!esEnteroPositivo(idMedia)) throw new AnuncioInvalidoException("Id del contenido no es un entero positivo");
        }
        pago.validarEntrada();
    }

    private boolean esSoloTexto(String tipo) {
        return TipoAnuncio.TEXTO.name().equals(tipo);
    }
    
    public void validarEdicion() throws EntidadInvalidaException {
        if(StringUtils.isBlank(texto)) throw new AnuncioInvalidoException("Texto no puede ir vacío");
        
        if(!esSoloTexto(tipo)){
            if(!esEnteroPositivo(idMedia)) throw new AnuncioInvalidoException("Id del contenido no es un entero positivo");
        }
        
        if(!esBoolean(activado)) throw new AnuncioInvalidoException("Estado del anuncio inválido");
        
        if(!esEnteroPositivo(id)) throw new AnuncioInvalidoException("Id no es un entero positivo");
    }
}
