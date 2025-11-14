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
import com.mynor.manejador.cines.api.dtos.CineDTO;
import com.mynor.manejador.cines.api.dtos.PeliculaDTO;
import com.mynor.manejador.cines.api.dtos.ProyeccionDTO;
import com.mynor.manejador.cines.api.dtos.SalaDTO;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.excepciones.BoletoInvalidoException;
import com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException;
import com.mynor.manejador.cines.api.excepciones.PagoInvalidoException;
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

    public void comprarBoleto(BoletoDTO boletoDTO) throws AccesoDeDatosException, EntidadInvalidaException {
        Usuario usuario = new Usuario();
        usuario.setId(Integer.valueOf(boletoDTO.getIdUsuario()));
        
        ProyeccionServicio proyeccionServicio = new ProyeccionServicio();
        Proyeccion proyeccion = proyeccionServicio.leerPorId(Integer.valueOf(boletoDTO.getIdProyeccion()));
        
        // TODO: Validar disponibilidad de la sala
        if(!salaDisponible(proyeccion) || !asientosDisponibles(proyeccion.getSala())) throw new BoletoInvalidoException("Sala no disponible");
        
        Pago pago = new Pago();
        pago.setFecha(LocalDate.parse(boletoDTO.getPagoDTO().getFecha()));
        pago.setMonto(proyeccion.getPrecio());
        pago.setUsuario(usuario);
        
        Boleto boleto = new Boleto();
        boleto.setProyeccion(proyeccion);
        boleto.setUsuario(usuario);
        
        CarteraServicio carteraServicio = new CarteraServicio();
        Cartera cartera = carteraServicio.leerModeloPorId(usuario.getId());
        if(!(cartera.getSaldo() >= pago.getMonto())) throw new PagoInvalidoException("Saldo insuficiente");
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
            boletoDTO.setIdUsuario(boleto.getUsuario().getId().toString());
            boletoDTO.setIdProyeccion(boleto.getProyeccion().getId().toString());

            // Mapear Proyección
            if (boleto.getProyeccion() != null) {
                ProyeccionDTO proyeccionDTO = new ProyeccionDTO();
                proyeccionDTO.setId(boleto.getProyeccion().getId().toString());
                proyeccionDTO.setFecha(boleto.getProyeccion().getFecha().toString());
                proyeccionDTO.setHora(boleto.getProyeccion().getHora().toString());
                proyeccionDTO.setPrecio(boleto.getProyeccion().getPrecio().toString());

                if (boleto.getProyeccion().getSala() != null) {
                    SalaDTO salaDTO = new SalaDTO();
                    salaDTO.setId(boleto.getProyeccion().getSala().getId().toString());
                    proyeccionDTO.setIdSala(boleto.getProyeccion().getSala().getId().toString());
                    boletoDTO.setSala(salaDTO);

                    if (boleto.getProyeccion().getSala().getCine() != null) {
                        CineDTO cineDTO = new CineDTO();
                        cineDTO.setId(boleto.getProyeccion().getSala().getCine().getId().toString());
                        cineDTO.setNombre(boleto.getProyeccion().getSala().getCine().getNombre());
                        cineDTO.setUbicacion(boleto.getProyeccion().getSala().getCine().getUbicacion());
                        salaDTO.setIdCine(boleto.getProyeccion().getSala().getCine().getId().toString());
                        boletoDTO.setCine(cineDTO);
                    }
                }

                if (boleto.getProyeccion().getPelicula() != null) {
                    PeliculaDTO peliculaDTO = new PeliculaDTO();
                    peliculaDTO.setId(boleto.getProyeccion().getPelicula().getId().toString());
                    peliculaDTO.setTitulo(boleto.getProyeccion().getPelicula().getTitulo());
                    proyeccionDTO.setIdPelicula(boleto.getProyeccion().getPelicula().getId().toString());
                    boletoDTO.setPelicula(peliculaDTO);
                }

                boletoDTO.setProyeccion(proyeccionDTO);
            }

            return boletoDTO;
        }).toArray(BoletoDTO[]::new);
    }

    private Optional<Boleto[]> leerPorUsuario(Integer id) throws AccesoDeDatosException {
        FiltrosBoleto filtros = new FiltrosBoleto();
        filtros.setIdUsuario(Optional.ofNullable(id));
        
        try(Transaccion t = new Transaccion()){
            Boleto[] boletos = BOLETO_BD.leerCompleto(filtros, t.obtenerConexion());
            t.commit();
            
            if(boletos.length < 1) return Optional.empty();
            
            return Optional.of(boletos);
        }
    }

    private boolean salaDisponible(Proyeccion proyeccion) {
        return true;
    }

    private boolean asientosDisponibles(Sala sala) {
        return true;
    }
    
}
