/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.servicios;

import com.mynor.manejador.cines.api.bd.BaseDeDatos;
import com.mynor.manejador.cines.api.bd.BoletoBD;
import com.mynor.manejador.cines.api.bd.CarteraBD;
import com.mynor.manejador.cines.api.bd.PagoBD;
import com.mynor.manejador.cines.api.bd.Transaccion;
import com.mynor.manejador.cines.api.dtos.BoletoDTO;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.excepciones.UsuarioInvalidoException;
import com.mynor.manejador.cines.api.filtros.FiltrosBoleto;
import com.mynor.manejador.cines.api.filtros.FiltrosCartera;
import com.mynor.manejador.cines.api.filtros.FiltrosPago;
import com.mynor.manejador.cines.api.modelo.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

/**
 *
 * @author mynordma
 */
public class BoletoServicio {
    
    private final BaseDeDatos<Boleto, FiltrosBoleto> BOLETO_BD;
    private final BaseDeDatos<Pago, FiltrosPago> PAGO_BD;
    private final BaseDeDatos<Cartera, FiltrosCartera> CARTERA_BD;
    
    public BoletoServicio(){
        BOLETO_BD = new BoletoBD();
        PAGO_BD = new PagoBD();
        CARTERA_BD = new CarteraBD();
    }

    public void comprarBoleto(BoletoDTO boletoDTO) throws AccesoDeDatosException, UsuarioInvalidoException {
        Usuario usuario = new Usuario();
        usuario.setId(Integer.valueOf(boletoDTO.getIdUsuario()));
        
        ProyeccionServicio proyeccionServicio = new ProyeccionServicio();
        Proyeccion proyeccion = proyeccionServicio.leerPorId(Integer.valueOf(boletoDTO.getIdProyeccion()));
        
        Pago pago = new Pago();
        pago.setFecha(LocalDate.parse(boletoDTO.getPagoDTO().getFecha()));
        pago.setMonto(proyeccion.getPrecio());
        pago.setUsuario(usuario);
        
        Boleto boleto = new Boleto();
        boleto.setProyeccion(proyeccion);
        boleto.setUsuario(usuario);
        
        CarteraServicio carteraServicio = new CarteraServicio();
        Cartera cartera = carteraServicio.leerModeloPorId(usuario.getId());
        cartera.setSaldo(cartera.getSaldo() - pago.getMonto());
        
        try(Transaccion t = new Transaccion()){
            pago = PAGO_BD.crear(pago, t.obtenerConexion());
            boleto.setPago(pago);
            CARTERA_BD.actualizar(cartera, t.obtenerConexion());
            BOLETO_BD.crear(boleto, t.obtenerConexion());
            t.commit();
        }
    }

    public BoletoDTO[] verBoletosPorUsuario(Integer id) throws AccesoDeDatosException {
        Optional<Boleto[]> boletos = leerPorUsuario(id);
        if(boletos.isEmpty()) throw new AccesoDeDatosException("Sin boletos");
        
        return Arrays.stream(boletos.get()).map(boleto -> {
            BoletoDTO boletoDTO = new BoletoDTO();
            boletoDTO.setId(boleto.getId().toString());
            boletoDTO.setIdProyeccion(boleto.getProyeccion().getId().toString());
            boletoDTO.setIdUsuario(boleto.getUsuario().getId().toString());
            return boletoDTO;
        }).toArray(BoletoDTO[]::new);
    }

    private Optional<Boleto[]> leerPorUsuario(Integer id) throws AccesoDeDatosException {
        FiltrosBoleto filtros = new FiltrosBoleto();
        filtros.setIdUsuario(Optional.ofNullable(id));
        
        try(Transaccion t = new Transaccion()){
            Boleto[] boletos = BOLETO_BD.leer(filtros, t.obtenerConexion());
            t.commit();
            
            if(boletos.length < 1) return Optional.empty();
            
            return Optional.of(boletos);
        }
    }
    
}
