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
import com.mynor.manejador.cines.api.dtos.AnuncioSalidaDTO;
import com.mynor.manejador.cines.api.dtos.PagoSalidaDTO;
import com.mynor.manejador.cines.api.dtos.TipoAnuncioPrecioDTO;
import com.mynor.manejador.cines.api.dtos.VigenciaAnuncioDTO;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.excepciones.AnuncioInvalidoException;
import com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException;
import com.mynor.manejador.cines.api.excepciones.PagoInvalidoException;
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
import java.util.Arrays;
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
        anuncio.calcularMonto(obtenerPrecioTipo(anuncio.getTipo()), obtenerPrecioVigencia(anuncio.getVigencia()));
        
        if(!saldoSuficiente(cartera, pago)) throw new PagoInvalidoException("Saldo insuficiente");
        
        try(Transaccion t = new Transaccion()){
            PAGO_BD.crear(pago, t.obtenerConexion());
            ANUNCIO_BD.crear(anuncio, t.obtenerConexion());
            cartera.setSaldo(cartera.getSaldo() - pago.getMonto());
            CARTERA_BD.actualizar(cartera, t.obtenerConexion());
            t.commit();
        }
    }
    
    protected Double obtenerPrecioTipo(TipoAnuncio tipo) throws AccesoDeDatosException{
        FiltrosTipoAnuncio filtros = new FiltrosTipoAnuncio();
        filtros.setTipo(Optional.ofNullable(tipo));
        
        try(Transaccion t = new Transaccion()){
            TipoAnuncioPrecio tipoAnuncio = TIPO_ANUNCIO_BD.leer(filtros, t.obtenerConexion())[0];
            t.commit();
            return tipoAnuncio.getMonto();
        }
        
    }
    
    protected Double obtenerPrecioVigencia(Integer dias) throws AccesoDeDatosException{
        FiltrosVigenciaAnuncio filtros = new FiltrosVigenciaAnuncio();
        filtros.setDias(Optional.ofNullable(dias));
        
        try(Transaccion t = new Transaccion()){
            VigenciaAnuncio vigencia = VIGENCIA_BD.leer(filtros, t.obtenerConexion())[0];
            t.commit();
            return vigencia.getMonto();
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
            VigenciaAnuncio[] vigencias = VIGENCIA_BD.leerCompleto(filtros, t.obtenerConexion());
            
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

    public AnuncioSalidaDTO[] obtenerTodos() throws AccesoDeDatosException {
        FiltrosAnuncio filtros = new FiltrosAnuncio();
        try(Transaccion t = new Transaccion()){
            Anuncio[] anuncios = ANUNCIO_BD.leerCompleto(filtros, t.obtenerConexion());
            t.commit();
            return Arrays.stream(anuncios).map(anuncio -> {
                AnuncioSalidaDTO anuncioDTO = new AnuncioSalidaDTO();
                anuncioDTO.setActivado(anuncio.getActivado());
                anuncioDTO.setId(String.valueOf(anuncio.getId()));
                anuncioDTO.setIdMedia(String.valueOf(anuncio.getIdMedia()));
                anuncioDTO.setTexto(anuncio.getTexto());
                anuncioDTO.setTipo(anuncio.getTipo().name());
                anuncioDTO.setVigencia(String.valueOf(anuncio.getVigencia()));
                
                PagoSalidaDTO pago = new PagoSalidaDTO();
                pago.setFecha(anuncio.getPago().getFecha().toString());
                pago.setMonto(String.valueOf(anuncio.getPago().getMonto()));
                pago.setIdUsuario(String.valueOf(anuncio.getPago().getUsuario().getId()));
                
                anuncioDTO.setPago(pago);
                
                if(anuncio.getActivado()){
                    anuncioDTO.setActivado(sigueVigente(anuncio));
                }
                
                return anuncioDTO;
            }).toArray(AnuncioSalidaDTO[]::new);
        }
    }

    public AnuncioSalidaDTO[] obtenerMostrables() throws AccesoDeDatosException {
        FiltrosAnuncio filtros = new FiltrosAnuncio();
        filtros.setLimite(Optional.ofNullable(8));
        filtros.setVigente(Optional.of(Boolean.TRUE));
        filtros.setActivado(Optional.ofNullable(Boolean.TRUE));
        
        try(Transaccion t = new Transaccion()){
            Anuncio[] anuncios = ANUNCIO_BD.leerCompleto(filtros, t.obtenerConexion());
            t.commit();
            return Arrays.stream(anuncios)
                    .map(anuncio -> {
                        AnuncioSalidaDTO anuncioDTO = new AnuncioSalidaDTO();
                        anuncioDTO.setActivado(anuncio.getActivado());
                        anuncioDTO.setId(String.valueOf(anuncio.getId()));
                        anuncioDTO.setIdMedia(String.valueOf(anuncio.getIdMedia()));
                        anuncioDTO.setTexto(anuncio.getTexto());
                        anuncioDTO.setTipo(anuncio.getTipo().name());
                        anuncioDTO.setVigencia(String.valueOf(anuncio.getVigencia()));
                        
                        PagoSalidaDTO pago = new PagoSalidaDTO();
                        pago.setFecha(anuncio.getPago().getFecha().toString());
                        pago.setMonto(String.valueOf(anuncio.getPago().getMonto()));
                        pago.setIdUsuario(String.valueOf(anuncio.getPago().getUsuario().getId()));

                        anuncioDTO.setPago(pago);
                        
                        if(anuncio.getActivado()){
                            anuncioDTO.setActivado(sigueVigente(anuncio));
                        }

                        return anuncioDTO;
                    }).toArray(AnuncioSalidaDTO[]::new);
        }
    }

    public AnuncioSalidaDTO[] obtenerPorIdUsuario(Integer id) throws AccesoDeDatosException {
        FiltrosAnuncio filtros = new FiltrosAnuncio();
        filtros.setIdUsuario(Optional.ofNullable(id));
        try(Transaccion t = new Transaccion()){
            Anuncio[] anuncios = ANUNCIO_BD.leerCompleto(filtros, t.obtenerConexion());
            t.commit();
            return Arrays.stream(anuncios).map(anuncio -> {
                AnuncioSalidaDTO anuncioDTO = new AnuncioSalidaDTO();
                anuncioDTO.setActivado(anuncio.getActivado());
                anuncioDTO.setId(String.valueOf(anuncio.getId()));
                anuncioDTO.setIdMedia(String.valueOf(anuncio.getIdMedia()));
                anuncioDTO.setTexto(anuncio.getTexto());
                anuncioDTO.setTipo(anuncio.getTipo().name());
                anuncioDTO.setVigencia(String.valueOf(anuncio.getVigencia()));
                
                PagoSalidaDTO pago = new PagoSalidaDTO();
                pago.setFecha(anuncio.getPago().getFecha().toString());
                pago.setMonto(String.valueOf(anuncio.getPago().getMonto()));
                pago.setIdUsuario(String.valueOf(anuncio.getPago().getUsuario().getId()));
                
                anuncioDTO.setPago(pago);
                
                if(anuncio.getActivado()){
                    anuncioDTO.setActivado(sigueVigente(anuncio));
                }
                
                return anuncioDTO;
            }).toArray(AnuncioSalidaDTO[]::new);
        }
    }

    public void editarAnuncio(AnuncioEntradaDTO anuncioDTO) throws AccesoDeDatosException, AnuncioInvalidoException {
        Anuncio anuncio = leerPorId(Integer.valueOf(anuncioDTO.getId()));
        anuncio.setId(Integer.valueOf(anuncioDTO.getId()));
        if(anuncio.getIdMedia() != null){
           anuncio.setIdMedia(Integer.valueOf(anuncioDTO.getIdMedia())); 
        }
        if(Boolean.parseBoolean(anuncioDTO.getActivado()) && !anuncio.getActivado()){
            throw new AnuncioInvalidoException("Los anuncios no se pueden activar manualmente");
        }
        anuncio.setActivado(Boolean.valueOf(anuncioDTO.getActivado()));
        anuncio.setTexto(anuncioDTO.getTexto());
        
        try(Transaccion t = new Transaccion()){
            ANUNCIO_BD.actualizar(anuncio, t.obtenerConexion());
            t.commit();
        }
        
    }

    public void editarTipoAnuncio(TipoAnuncioPrecioDTO tipoDTO) throws AccesoDeDatosException {
        TipoAnuncioPrecio tipo = new TipoAnuncioPrecio();
        tipo.setMonto(Double.valueOf(tipoDTO.getMonto()));
        tipo.setTipo(TipoAnuncio.valueOf(tipoDTO.getTipo()));
        
        try(Transaccion t = new Transaccion()){
            TIPO_ANUNCIO_BD.actualizar(tipo, t.obtenerConexion());
            t.commit();
        }
    }

    public void editarVigenciaAnuncio(VigenciaAnuncioDTO vigenciaDTO) throws AccesoDeDatosException {
        VigenciaAnuncio vigencia = new VigenciaAnuncio();
        vigencia.setMonto(Double.valueOf(vigenciaDTO.getMonto()));
        vigencia.setDias(Integer.valueOf(vigenciaDTO.getDias()));
        
        try(Transaccion t = new Transaccion()){
            VIGENCIA_BD.actualizar(vigencia, t.obtenerConexion());
            t.commit();
        }
    }
    
    private boolean sigueVigente(Anuncio anuncio) { 
        System.out.println("Evaluando anuncio: " + anuncio.getId()); 
        LocalDate fechaPago = anuncio.getPago().getFecha(); 
        Integer diasVigencia = anuncio.getVigencia(); 
        LocalDate fechaVencimiento = fechaPago.plusDays(diasVigencia); 
        LocalDate hoy = LocalDate.now(); 
        return !hoy.isAfter(fechaVencimiento); 
    }

    public Anuncio leerPorId(Integer id) throws AccesoDeDatosException {
        FiltrosAnuncio filtros = new FiltrosAnuncio();
        filtros.setId(Optional.ofNullable(id));
        try(Transaccion t = new Transaccion()){
            Anuncio anuncio = ANUNCIO_BD.leerCompleto(filtros, t.obtenerConexion())[0];
            t.commit();
            return anuncio;
        }
    }

    
}