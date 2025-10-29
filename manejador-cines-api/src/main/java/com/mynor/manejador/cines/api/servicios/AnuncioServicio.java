/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.servicios;

import com.mynor.manejador.cines.api.bd.AnuncioBD;
import com.mynor.manejador.cines.api.bd.BaseDeDatos;
import com.mynor.manejador.cines.api.bd.CarteraBD;
import com.mynor.manejador.cines.api.bd.ImagenBD;
import com.mynor.manejador.cines.api.bd.PagoBD;
import com.mynor.manejador.cines.api.bd.TipoAnuncioBD;
import com.mynor.manejador.cines.api.bd.Transaccion;
import com.mynor.manejador.cines.api.bd.VideoBD;
import com.mynor.manejador.cines.api.bd.VigenciaAnuncioBD;
import com.mynor.manejador.cines.api.dtos.AnuncioEntradaDTO;
import com.mynor.manejador.cines.api.dtos.TipoAnuncioPrecioDTO;
import com.mynor.manejador.cines.api.dtos.VigenciaAnuncioDTO;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException;
import com.mynor.manejador.cines.api.excepciones.PagoInvalidoException;
import com.mynor.manejador.cines.api.excepciones.UsuarioInvalidoException;
import com.mynor.manejador.cines.api.filtros.FiltrosAnuncio;
import com.mynor.manejador.cines.api.filtros.FiltrosCartera;
import com.mynor.manejador.cines.api.filtros.FiltrosImagen;
import com.mynor.manejador.cines.api.filtros.FiltrosPago;
import com.mynor.manejador.cines.api.filtros.FiltrosTipoAnuncio;
import com.mynor.manejador.cines.api.filtros.FiltrosVideo;
import com.mynor.manejador.cines.api.filtros.FiltrosVigenciaAnuncio;
import com.mynor.manejador.cines.api.modelo.Anuncio;
import com.mynor.manejador.cines.api.modelo.Cartera;
import com.mynor.manejador.cines.api.modelo.Imagen;
import com.mynor.manejador.cines.api.modelo.Pago;
import com.mynor.manejador.cines.api.modelo.TipoAnuncio;
import com.mynor.manejador.cines.api.modelo.TipoAnuncioPrecio;
import com.mynor.manejador.cines.api.modelo.Usuario;
import com.mynor.manejador.cines.api.modelo.Video;
import com.mynor.manejador.cines.api.modelo.VigenciaAnuncio;
import java.time.LocalDate;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author mynordma
 */
public class AnuncioServicio {
    
    private final BaseDeDatos<Cartera, FiltrosCartera> CARTERA_BD;
    private final BaseDeDatos<Pago, FiltrosPago> PAGO_BD;
    private final BaseDeDatos<Anuncio, FiltrosAnuncio> ANUNCIO_BD;
    private final BaseDeDatos<Imagen, FiltrosImagen> IMAGEN_BD;
    private final BaseDeDatos<Video, FiltrosVideo> VIDEO_BD;
    private final BaseDeDatos<TipoAnuncioPrecio, FiltrosTipoAnuncio> TIPO_ANUNCIO_BD;
    private final BaseDeDatos<VigenciaAnuncio, FiltrosVigenciaAnuncio> VIGENCIA_BD;
    
    public AnuncioServicio(){
        CARTERA_BD = new CarteraBD();
        PAGO_BD = new PagoBD();
        ANUNCIO_BD = new AnuncioBD();
        IMAGEN_BD = new ImagenBD();
        VIDEO_BD = new VideoBD();
        TIPO_ANUNCIO_BD = new TipoAnuncioBD();
        VIGENCIA_BD = new VigenciaAnuncioBD();
        
    }

    /**
     * Verifica el saldo en la cartera
     * Realiza el pago
     * Registra el anuncio
     * @param anuncioDTO 
     * @throws com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException 
     * @throws com.mynor.manejador.cines.api.excepciones.UsuarioInvalidoException 
     */
    public void comprarAnuncio(AnuncioEntradaDTO anuncioDTO) throws AccesoDeDatosException, EntidadInvalidaException {
        
        // Mapear DTO -> Modelo
        
        Cartera cartera = leerCarteraPorId(Integer.valueOf(anuncioDTO.getPago().getIdUsuario()));
        
        UsuarioServicio us = new UsuarioServicio();
        Usuario usuario = us.leerPorId(Integer.valueOf(anuncioDTO.getPago().getIdUsuario()));
        
        Pago pago = new Pago();
        pago.setFecha(LocalDate.parse(anuncioDTO.getPago().getFecha()));
        pago.setMonto(Double.valueOf(anuncioDTO.getPago().getMonto()));
        pago.setUsuario(usuario);
        
        Anuncio anuncio = new Anuncio();
        anuncio.setActivado(Boolean.TRUE);
        if(!StringUtils.isBlank(anuncioDTO.getIdMedia())){
            anuncio.setIdMedia(Integer.valueOf(anuncioDTO.getIdMedia()));
        }
        anuncio.setPago(pago);
        anuncio.setTexto(anuncioDTO.getTexto());
        anuncio.setTipo(TipoAnuncio.valueOf(anuncioDTO.getTipo()));
        anuncio.setVigencia(Integer.valueOf(anuncioDTO.getVigencia()));
        
        if(!saldoSuficiente(cartera, pago)) throw new PagoInvalidoException("Saldo insuficiente");
        
        try(Transaccion t = new Transaccion()){
            PAGO_BD.crear(pago, t.obtenerConexion());
            ANUNCIO_BD.crear(anuncio, t.obtenerConexion());
            cartera.setSaldo(cartera.getSaldo() - pago.getMonto());
            CARTERA_BD.actualizar(cartera, t.obtenerConexion());
            t.commit();
        }
    }

    protected Cartera leerCarteraPorId(Integer idUsuario) throws AccesoDeDatosException {
        try(Transaccion t = new Transaccion()){
            FiltrosCartera f = new FiltrosCartera();
            f.setIdUsuario(Optional.ofNullable(idUsuario));
            
            Cartera[] coincidencias = CARTERA_BD.leer(f, t.obtenerConexion());
            
            if(coincidencias.length < 1) throw new AccesoDeDatosException("Sin coincidencias al obtener la cartera");
            
            t.commit();
            
            return coincidencias[0];
            
        }
    }

    private boolean saldoSuficiente(Cartera cartera, Pago pago) {
        return cartera.getSaldo() >= pago.getMonto();
    }

    public TipoAnuncioPrecioDTO[] obtenerPreciosDeTipos() throws AccesoDeDatosException {
        try(Transaccion t = new Transaccion()){
            FiltrosTipoAnuncio filtros = new FiltrosTipoAnuncio();
            TipoAnuncioPrecio[] tipos = TIPO_ANUNCIO_BD.leer(filtros, t.obtenerConexion());
            
            TipoAnuncioPrecioDTO[] tiposDTO = new TipoAnuncioPrecioDTO[tipos.length];
            
            for (int i = 0; i < tiposDTO.length; i++) {
                TipoAnuncioPrecio tipo = tipos[i];
                
                TipoAnuncioPrecioDTO tipoDTO = new TipoAnuncioPrecioDTO();
                tipoDTO.setMonto(String.valueOf(tipo.getMonto()));
                tipoDTO.setTipo(tipo.getTipo().name());
                
                tiposDTO[i] = tipoDTO;
                
            }
            t.commit();
            return tiposDTO;
        }
    }

    public VigenciaAnuncioDTO[] obtenerPreciosDeVigencias() throws AccesoDeDatosException {
        try(Transaccion t = new Transaccion()){
            FiltrosVigenciaAnuncio filtros = new FiltrosVigenciaAnuncio();
            VigenciaAnuncio[] vigencias = VIGENCIA_BD.leer(filtros, t.obtenerConexion());
            
            VigenciaAnuncioDTO[] vigenciasDTO = new VigenciaAnuncioDTO[vigencias.length];
            
            for (int i = 0; i < vigenciasDTO.length; i++) {
                VigenciaAnuncio vigencia = vigencias[i];
                
                VigenciaAnuncioDTO vigenciaDTO = new VigenciaAnuncioDTO();
                vigenciaDTO.setMonto(String.valueOf(vigencia.getMonto()));
                vigenciaDTO.setDias(String.valueOf(vigencia.getDias()));
                
                vigenciasDTO[i] = vigenciaDTO;
                
            }
            t.commit();
            return vigenciasDTO;
        }
    }
    
}