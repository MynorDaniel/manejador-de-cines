/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.filtros;

import java.util.Optional;

/**
 *
 * @author mynordma
 */
public class FiltrosClasificacion extends Filtros {
    
    private Optional<String> codigo;
    
    public FiltrosClasificacion(){
        codigo = Optional.empty();
    }

    public Optional<String> getCodigo() {
        return codigo;
    }

    public void setCodigo(Optional<String> codigo) {
        this.codigo = codigo;
    }
}
