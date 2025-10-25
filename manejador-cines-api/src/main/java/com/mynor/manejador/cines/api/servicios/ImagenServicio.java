/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.servicios;

import com.mynor.manejador.cines.api.bd.BaseDeDatos;
import com.mynor.manejador.cines.api.bd.ImagenBD;
import com.mynor.manejador.cines.api.bd.Transaccion;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.excepciones.ImagenInvalidaException;
import com.mynor.manejador.cines.api.filtros.FiltrosImagen;
import com.mynor.manejador.cines.api.modelo.Imagen;
import java.util.Optional;

/**
 *
 * @author mynordma
 */
public class ImagenServicio {
    
    private final BaseDeDatos<Imagen, FiltrosImagen> IMAGEN_BD;
    
    public ImagenServicio(){
        IMAGEN_BD = new ImagenBD();
    }
    
    public Imagen obtenerImagen(Integer id) throws AccesoDeDatosException, ImagenInvalidaException{
        FiltrosImagen filtros = new FiltrosImagen();
        filtros.setId(Optional.ofNullable(id));
        
        Imagen[] coincidencias;
        try(Transaccion transaccion = new Transaccion()){
            coincidencias = IMAGEN_BD.leer(filtros, transaccion.obtenerConexion());
        }
        
        if(coincidencias.length < 1) throw new ImagenInvalidaException("Imagen no encontrada");
        
        return coincidencias[0];
        
    }
    
    public Imagen guardarImagen() throws AccesoDeDatosException{
        
        try(Transaccion t = new Transaccion()){
            FiltrosImagen filtros = new FiltrosImagen();
            filtros.setUltimoId(Optional.ofNullable(true));
            Imagen[] imagenes = IMAGEN_BD.leer(filtros, t.obtenerConexion());
            int ultimoId = imagenes[0].getId();
            System.out.println("ultimo id imagen: " + ultimoId);
            Imagen imagenNueva = new Imagen();
            imagenNueva.setLink((ultimoId + 1) + ".png");
            
            Imagen imagenCreada = IMAGEN_BD.crear(imagenNueva, t.obtenerConexion());
            
            t.commit();
            
            return imagenCreada;
          
        }
    }
    
    
}
