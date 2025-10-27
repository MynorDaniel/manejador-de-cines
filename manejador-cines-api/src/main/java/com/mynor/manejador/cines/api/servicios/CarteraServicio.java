/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.servicios;

import com.mynor.manejador.cines.api.bd.BaseDeDatos;
import com.mynor.manejador.cines.api.bd.CarteraBD;
import com.mynor.manejador.cines.api.bd.Transaccion;
import com.mynor.manejador.cines.api.dtos.CarteraDTO;
import com.mynor.manejador.cines.api.dtos.CarteraEntradaDTO;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.excepciones.UsuarioInvalidoException;
import com.mynor.manejador.cines.api.filtros.FiltrosCartera;
import com.mynor.manejador.cines.api.modelo.Cartera;
import java.util.Optional;

/**
 *
 * @author mynordma
 */
public class CarteraServicio {
    
    private final BaseDeDatos<Cartera, FiltrosCartera> CARTERA_BD;
    
    public CarteraServicio(){
        CARTERA_BD = new CarteraBD();
    }

    public CarteraDTO obtenerPorId(Integer id) throws AccesoDeDatosException, UsuarioInvalidoException {
        
        Cartera cartera = leerModeloPorId(id);

        CarteraDTO carteraDTO = new CarteraDTO();
        carteraDTO.setUsuarioId(id);
        carteraDTO.setSaldo(cartera.getSaldo());

        return carteraDTO;
        
    }

    public void editarCartera(CarteraEntradaDTO carteraDTO) throws AccesoDeDatosException, UsuarioInvalidoException {
        try(Transaccion t = new Transaccion()){
            
            Cartera cartera = leerModeloPorId(Integer.valueOf(carteraDTO.getUsuarioId()));
            
            cartera.setSaldo(Double.valueOf(carteraDTO.getSaldo()));
            
            CARTERA_BD.actualizar(cartera, t.obtenerConexion());
            
            t.commit();
        }
    }

    private Cartera leerModeloPorId(Integer id) throws AccesoDeDatosException, UsuarioInvalidoException {
        try(Transaccion t = new Transaccion()){
            FiltrosCartera filtros = new FiltrosCartera();
            filtros.setIdUsuario(Optional.ofNullable(id));
            Cartera[] coincidencias = CARTERA_BD.leer(filtros, t.obtenerConexion());

            if(coincidencias.length < 1) throw new UsuarioInvalidoException("Sin coincidencias");
            
            t.commit();

            return coincidencias[0];
        }
    }
    
}
