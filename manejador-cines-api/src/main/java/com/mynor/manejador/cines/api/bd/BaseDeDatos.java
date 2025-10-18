/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mynor.manejador.cines.api.bd;

import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;

/**
 *
 * @author mynordma
 * @param <E>
 * @param <F>
 */
public interface BaseDeDatos<E, F> {
    
    public E crear(E entidad) throws AccesoDeDatosException;
    public E[] leer(F filtros) throws AccesoDeDatosException;
    public E[] leerCompleto(F filtros) throws AccesoDeDatosException;
    public E actualizar(E entidad) throws AccesoDeDatosException;
    public E eliminar(E entidad) throws AccesoDeDatosException;
    
}