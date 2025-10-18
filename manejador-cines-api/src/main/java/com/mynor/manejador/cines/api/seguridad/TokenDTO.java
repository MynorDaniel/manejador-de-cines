/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.seguridad;

import com.mynor.manejador.cines.api.dtos.UsuarioSalidaDTO;

/**
 *
 * @author mynordma
 */
public class TokenDTO {
    
    private String token;
    private String tipo;
    private long expiraEn;
    private UsuarioSalidaDTO usuario;

    public TokenDTO() {}

    public TokenDTO(String token, String tipo, long expiraEn, UsuarioSalidaDTO usuario) {
        this.token = token;
        this.tipo = tipo;
        this.expiraEn = expiraEn;
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public long getExpiraEn() {
        return expiraEn;
    }

    public void setExpiraEn(long expiraEn) {
        this.expiraEn = expiraEn;
    }

    public UsuarioSalidaDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioSalidaDTO usuario) {
        this.usuario = usuario;
    }
}
