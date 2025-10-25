/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mynor.manejador.cines.api.bd;

import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author mynordma
 * @param <E>
 * @param <F>
 */
public interface BaseDeDatos<E, F> {
    
    public E crear(E entidad, Connection conn) throws AccesoDeDatosException;
    public E[] leer(F filtros, Connection conn) throws AccesoDeDatosException;
    public E[] leerCompleto(F filtros, Connection conn) throws AccesoDeDatosException;
    public E actualizar(E entidad, Connection conn) throws AccesoDeDatosException;
    public E eliminar(E entidad, Connection conn) throws AccesoDeDatosException;
    
    default int obtenerLongitudRS(ResultSet rs) throws SQLException{
        if (rs.last()) {
            int longitud = rs.getRow();
            rs.beforeFirst();
            return longitud;
        }else{
            return 0;
        }
    }
}