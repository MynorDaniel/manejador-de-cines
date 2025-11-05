/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.servicios;

import com.mynor.manejador.cines.api.bd.BaseDeDatos;
import com.mynor.manejador.cines.api.bd.BloqueoAnunciosCineBD;
import com.mynor.manejador.cines.api.bd.CarteraBD;
import com.mynor.manejador.cines.api.bd.CineBD;
import com.mynor.manejador.cines.api.bd.CostoDiarioCineBD;
import com.mynor.manejador.cines.api.bd.CostoGlobalCinesBD;
import com.mynor.manejador.cines.api.bd.PagoBD;
import com.mynor.manejador.cines.api.bd.PagoCineBD;
import com.mynor.manejador.cines.api.bd.Transaccion;
import com.mynor.manejador.cines.api.dtos.BloqueoAnunciosCineDTO;
import com.mynor.manejador.cines.api.dtos.CineDTO;
import com.mynor.manejador.cines.api.dtos.CostoDiarioCineDTO;
import com.mynor.manejador.cines.api.dtos.CostoGlobalCinesDTO;
import com.mynor.manejador.cines.api.dtos.PagoSalidaDTO;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.excepciones.CineInvalidoException;
import com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException;
import com.mynor.manejador.cines.api.excepciones.PagoInvalidoException;
import com.mynor.manejador.cines.api.filtros.FiltrosBloqueoAnunciosCine;
import com.mynor.manejador.cines.api.filtros.FiltrosCartera;
import com.mynor.manejador.cines.api.filtros.FiltrosCine;
import com.mynor.manejador.cines.api.filtros.FiltrosCostoDiarioCine;
import com.mynor.manejador.cines.api.filtros.FiltrosCostoGlobalCines;
import com.mynor.manejador.cines.api.filtros.FiltrosPago;
import com.mynor.manejador.cines.api.filtros.FiltrosPagoCine;
import com.mynor.manejador.cines.api.modelo.BloqueoAnunciosCine;
import com.mynor.manejador.cines.api.modelo.Cartera;
import com.mynor.manejador.cines.api.modelo.Cine;
import com.mynor.manejador.cines.api.modelo.CostoDiarioCine;
import com.mynor.manejador.cines.api.modelo.CostoGlobalCines;
import com.mynor.manejador.cines.api.modelo.Pago;
import com.mynor.manejador.cines.api.modelo.PagoCine;
import com.mynor.manejador.cines.api.modelo.Usuario;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

/**
 *
 * @author mynordma
 */
public class CineServicio {
    
    private final BaseDeDatos<Cine, FiltrosCine> CINE_BD;
    private final BaseDeDatos<CostoDiarioCine, FiltrosCostoDiarioCine> COSTO_CINE_BD;
    private final BaseDeDatos<CostoGlobalCines, FiltrosCostoGlobalCines> COSTO_GLOBAL_BD;
    private final BaseDeDatos<Pago, FiltrosPago> PAGO_BD;
    private final BaseDeDatos<PagoCine, FiltrosPagoCine> PAGO_CINE_BD;
    private final BaseDeDatos<BloqueoAnunciosCine, FiltrosBloqueoAnunciosCine> BLOQUEO_BD;
    private final BaseDeDatos<Cartera, FiltrosCartera> CARTERA_BD;

    public CineServicio() {
        CINE_BD = new CineBD();
        COSTO_CINE_BD = new CostoDiarioCineBD();
        COSTO_GLOBAL_BD = new CostoGlobalCinesBD();
        PAGO_BD = new PagoBD();
        BLOQUEO_BD = new BloqueoAnunciosCineBD();
        PAGO_CINE_BD = new PagoCineBD();
        CARTERA_BD = new CarteraBD();
    }

    /**
     * Crea el cine
     * Asigna el costo diario desde el global
     * @param cineDTO
     * @throws AccesoDeDatosException 
     */
    public void crearCine(CineDTO cineDTO) throws AccesoDeDatosException {
        Cine cine = new Cine();
        cine.setActivado(Boolean.TRUE);
        cine.setNombre(cineDTO.getNombre());
        cine.setUbicacion(cineDTO.getUbicacion());
        cine.setFechaCreacion(LocalDate.parse(cineDTO.getFechaCreacion()));
        
        Usuario usuarioCreador = new Usuario();
        usuarioCreador.setId(Integer.valueOf(cineDTO.getIdUsuarioCreador()));
        cine.setUsuarioCreador(usuarioCreador);
        
        CostoGlobalCines costoGlobal = leerCostoGlobalCines();
        CostoDiarioCine costoDiario = new CostoDiarioCine();
        costoDiario.setCine(cine);
        costoDiario.setFechaCambio(cine.getFechaCreacion());
        costoDiario.setMonto(costoGlobal.getMonto());
        
        try(Transaccion t = new Transaccion()){
            CINE_BD.crear(cine, t.obtenerConexion());
            COSTO_CINE_BD.crear(costoDiario, t.obtenerConexion());
            
            t.commit();
        }
    }
    
    private CostoGlobalCines leerCostoGlobalCines() throws AccesoDeDatosException{
        FiltrosCostoGlobalCines filtros = new FiltrosCostoGlobalCines();
        filtros.setId(Optional.ofNullable(1));
        
        try(Transaccion t = new Transaccion()){
            CostoGlobalCines[] coincidencias = COSTO_GLOBAL_BD.leer(filtros, t.obtenerConexion());
            
            if(coincidencias.length < 1) throw new AccesoDeDatosException("Sin coincidencias al buscar los costos");
           
            t.commit();
            
            return coincidencias[0];
        }
    }

    public CineDTO[] verCines() throws AccesoDeDatosException {
        Cine[] cines = leerTodosLosCines();
        CineDTO[] cinesDTO = Arrays.stream(cines).map(cine -> {
            CineDTO cineDTO = new CineDTO();
            cineDTO.setActivado(String.valueOf(cine.getActivado()));
            cineDTO.setId(String.valueOf(cine.getId()));
            cineDTO.setIdUsuarioCreador(String.valueOf(cine.getUsuarioCreador().getId()));
            cineDTO.setNombre(cine.getNombre());
            cineDTO.setUbicacion(cine.getUbicacion());
            cineDTO.setBloqueoActivo(cineTieneBloqueoActivo(cine.getId()));
            try {
                cineDTO.setFechaUltimoCambioDeCosto(verUltimoCambioDeCosto(cine.getId()).getFechaCambio().toString());
            } catch (AccesoDeDatosException ex) {
                System.getLogger(CineServicio.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            return cineDTO;
        }).toArray(CineDTO[]::new);
        
        return cinesDTO;
    }
    
    private Cine[] leerTodosLosCines() throws AccesoDeDatosException{
        FiltrosCine filtros = new FiltrosCine();
        try(Transaccion t = new Transaccion()){
            Cine[] coincidencias = CINE_BD.leer(filtros, t.obtenerConexion());
            if(coincidencias.length < 1) throw new AccesoDeDatosException("Sin coincidencias al buscar los cines");
            t.commit();
            return coincidencias;
        }
    }

    public CineDTO verCine(Integer id) throws AccesoDeDatosException {
        Cine cine = leerCinePorId(id);
        CineDTO cineDTO = new CineDTO();
        cineDTO.setActivado(String.valueOf(cine.getActivado()));
        cineDTO.setId(String.valueOf(cine.getId()));
        cineDTO.setIdUsuarioCreador(String.valueOf(cine.getUsuarioCreador().getId()));
        cineDTO.setNombre(cine.getNombre());
        cineDTO.setUbicacion(cine.getUbicacion());
        cineDTO.setBloqueoActivo(cineTieneBloqueoActivo(cine.getId()));
        cineDTO.setFechaUltimoCambioDeCosto(verUltimoCambioDeCosto(cine.getId()).getFechaCambio().toString());
       
        return cineDTO;
        
    }
    
    public Cine leerCinePorId(Integer id) throws AccesoDeDatosException{
        FiltrosCine filtros = new FiltrosCine();
        filtros.setId(Optional.ofNullable(id));
        try(Transaccion t = new Transaccion()){
            Cine[] coincidencias = CINE_BD.leer(filtros, t.obtenerConexion());
            if(coincidencias.length < 1) throw new AccesoDeDatosException("Sin coincidencias al buscar los cines");
            t.commit();
            return coincidencias[0];
        }
    }

    public CineDTO[] verCinesPorUsuario(Integer id) throws AccesoDeDatosException {
        Cine[] cines = leerCinePorIdUsuario(id);
        return Arrays.stream(cines).map(cine -> {
            CineDTO cineDTO = new CineDTO();
            cineDTO.setActivado(String.valueOf(cine.getActivado()));
            cineDTO.setId(String.valueOf(cine.getId()));
            cineDTO.setIdUsuarioCreador(String.valueOf(cine.getUsuarioCreador().getId()));
            cineDTO.setNombre(cine.getNombre());
            cineDTO.setUbicacion(cine.getUbicacion());
            cineDTO.setBloqueoActivo(cineTieneBloqueoActivo(cine.getId()));
            try {
                cineDTO.setFechaUltimoCambioDeCosto(verUltimoCambioDeCosto(cine.getId()).getFechaCambio().toString());
            } catch (AccesoDeDatosException ex) {
                System.getLogger(CineServicio.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            return cineDTO;
        }).toArray(CineDTO[]::new);
    }
    
    private Cine[] leerCinePorIdUsuario(Integer id) throws AccesoDeDatosException{
        FiltrosCine filtros = new FiltrosCine();
        filtros.setIdUsuario(Optional.ofNullable(id));
        try(Transaccion t = new Transaccion()){
            Cine[] coincidencias = CINE_BD.leerCompleto(filtros, t.obtenerConexion());
            if(coincidencias.length < 1) throw new AccesoDeDatosException("Sin coincidencias al buscar los cines");
            t.commit();
            return coincidencias;
        }
    }

    public void editarCine(CineDTO cineDTO) throws AccesoDeDatosException {
        Cine cine = leerCinePorId(Integer.valueOf(cineDTO.getId()));
        
        cine.setActivado(Boolean.valueOf(cineDTO.getActivado()));
        cine.setNombre(cineDTO.getNombre());
        cine.setUbicacion(cineDTO.getUbicacion());
        
        try(Transaccion t = new Transaccion()){
            CINE_BD.actualizar(cine, t.obtenerConexion());
            t.commit();
        }
    }

    public void crearBloqueoDeAnuncios(BloqueoAnunciosCineDTO bloqueoDTO) throws AccesoDeDatosException, EntidadInvalidaException {
        BloqueoAnunciosCine bloqueo = new BloqueoAnunciosCine();
        
        Cine cine = new Cine();
        cine.setId(Integer.valueOf(bloqueoDTO.getIdCine()));
        
        if(cineTieneBloqueoActivo(cine.getId())) throw new CineInvalidoException("Ya tienes bloqueados los anuncios");
        
        bloqueo.setCine(cine);
        bloqueo.setDias(30); // cambiar
        
        Usuario usuario = new Usuario();
        usuario.setId(Integer.valueOf(bloqueoDTO.getPago().getIdUsuario()));
        
        Pago pago = new Pago();
        pago.setFecha(LocalDate.parse(bloqueoDTO.getPago().getFecha()));
        pago.setMonto(Double.valueOf(bloqueoDTO.getPago().getMonto()));
        pago.setUsuario(usuario);
        
        CarteraServicio carteraServicio = new CarteraServicio();
        Cartera cartera = carteraServicio.leerModeloPorId(usuario.getId());
        
        if(cartera.getSaldo() < pago.getMonto()) throw new PagoInvalidoException("Saldo insuficiente");
        
        cartera.setSaldo(cartera.getSaldo() - pago.getMonto());
        
        try(Transaccion t = new Transaccion()){
            pago = PAGO_BD.crear(pago, t.obtenerConexion());
            bloqueo.setPago(pago);
            CARTERA_BD.actualizar(cartera, t.obtenerConexion());
            BLOQUEO_BD.crear(bloqueo, t.obtenerConexion());
            
            t.commit();
        }
    }

    public void crearCostoDiarioACine(CostoDiarioCineDTO costoDTO) throws AccesoDeDatosException, CineInvalidoException {
        Cine cine = new Cine();
        cine.setId(Integer.valueOf(costoDTO.getIdCine()));
        
        CostoDiarioCine costo = new CostoDiarioCine();
        costo.setCine(cine);
        costo.setMonto(Double.valueOf(costoDTO.getMonto()));
        costo.setFechaCambio(LocalDate.parse(costoDTO.getFechaCambio()));
        
        if(!fechaDeCambioValida(costo.getFechaCambio(), leerCostosPorCine(costo.getCine().getId()))) throw new CineInvalidoException("La fecha debe ir después de la fecha de cambio anterior");
        
        try(Transaccion t = new Transaccion()){
            COSTO_CINE_BD.crear(costo, t.obtenerConexion());
            t.commit();
        }
        
    }
    
    private CostoDiarioCine[] leerCostosPorCine(Integer idCine) throws AccesoDeDatosException{
        FiltrosCostoDiarioCine filtros = new FiltrosCostoDiarioCine();
        filtros.setIdCine(Optional.ofNullable(idCine));
        
        try(Transaccion t = new Transaccion()){
            CostoDiarioCine[] costos = COSTO_CINE_BD.leerCompleto(filtros, t.obtenerConexion());
            
            if(costos.length < 1) throw new AccesoDeDatosException("Sin coincidencias al leer los costos diarios");
            
            t.commit();
            return costos;
        }
    }

    private boolean fechaDeCambioValida(LocalDate fechaCambio, CostoDiarioCine[] costos) {
        
        for (CostoDiarioCine costo : costos) {
            LocalDate fechaCosto = costo.getFechaCambio();
            if (!(fechaCambio.isAfter(fechaCosto) || fechaCambio.isEqual(fechaCosto))) {
                return false;
            }
            
        }
        
        return true;
    }

    public void crearCostoGlobalDiario(CostoGlobalCinesDTO costoGlobalDTO) throws AccesoDeDatosException {
        CostoGlobalCines costoGlobal = new CostoGlobalCines();
        costoGlobal.setId(Integer.valueOf(costoGlobalDTO.getId()));
        costoGlobal.setMonto(Double.valueOf(costoGlobalDTO.getMonto()));
        
        try(Transaccion t = new Transaccion()){
            COSTO_GLOBAL_BD.actualizar(costoGlobal, t.obtenerConexion());
            t.commit();
        }
    }

    /**
     * Obtiene los costos diarios del cine
     * Calcula la deuda
     * Resta los pagos ya realizados
     * Crea el pago
     * @param idUsuario
     * @param idCine 
     * @throws com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException 
     * @throws com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException 
     */
    public void pagarCostoDiarioCine(Integer idUsuario, Integer idCine) throws AccesoDeDatosException, EntidadInvalidaException {
        
        Double deudaTotal = calcularDeudaTotal(idCine);
        Double montoPagado = calcularMontoPagado(idCine);
        Double deudaReal = deudaTotal - montoPagado;
        
        if(deudaReal == 0) throw new PagoInvalidoException("No tienes pagos pendientes");
        
        Cine cine = new Cine();
        cine.setId(idCine);
        
        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);
        
        Pago pago = new Pago();
        pago.setFecha(LocalDate.now());
        pago.setMonto(deudaReal);
        pago.setUsuario(usuario);
        
        CarteraServicio carteraServicio = new CarteraServicio();
        Cartera cartera = carteraServicio.leerModeloPorId(usuario.getId());
        cartera.setSaldo(cartera.getSaldo() - deudaReal);
        
        if(cartera.getSaldo() < pago.getMonto()) throw new PagoInvalidoException("Saldo insuficiente");
        
        PagoCine pagoCine = new PagoCine();
        pagoCine.setCine(cine);
        
        try(Transaccion t = new Transaccion()){
            
            pago = PAGO_BD.crear(pago, t.obtenerConexion());
            pagoCine.setPago(pago);
            
            CARTERA_BD.actualizar(cartera, t.obtenerConexion());
            PAGO_CINE_BD.crear(pagoCine, t.obtenerConexion());
            
            t.commit();
        }
        
    }

    private Double calcularDeudaTotal(Integer idCine) throws AccesoDeDatosException {
        CostoDiarioCine[] costos = leerCostosPorCine(idCine);
        Double deuda = 0.0;

        for (int i = 0; i < costos.length; i++) {
            CostoDiarioCine costo = costos[i];
            LocalDate fechaCambio = costo.getFechaCambio();
            LocalDate fechaCambioSiguiente = LocalDate.now();

            if(i + 1 < costos.length && costos[i + 1] != null){
                fechaCambioSiguiente = costos[i + 1].getFechaCambio();
            }

            for (LocalDate fecha = fechaCambio; fecha.isBefore(fechaCambioSiguiente); fecha = fecha.plusDays(1)) {
                deuda += costo.getMonto();
            }
        }

        return deuda;
    }

    private Double calcularMontoPagado(Integer idCine) throws AccesoDeDatosException {
        PagoCine[] pagos = obtenerPagosPorCine(idCine);
        
        if(pagos == null) return 0.0;
        
        Double pagado = 0.0;
        
        for (PagoCine pago : pagos) {
            pagado += pago.getPago().getMonto();
        }
        
        return pagado;
    }
    
    private PagoCine[] obtenerPagosPorCine(Integer idCine) throws AccesoDeDatosException{
        FiltrosPagoCine filtros = new FiltrosPagoCine();
        filtros.setIdCine(Optional.ofNullable(idCine));
        
        try(Transaccion t = new Transaccion()){
            PagoCine[] pagos = PAGO_CINE_BD.leerCompleto(filtros, t.obtenerConexion());
            t.commit();
            
            if(pagos.length < 1) return null;
            
            return pagos;
        }
    }

    public Optional<BloqueoAnunciosCine> verUltimoBloqueoDeAnuncios(Integer idCine) throws AccesoDeDatosException {
        BloqueoAnunciosCine[] bloqueos = leerBloqueosPorCine(idCine);

        return Arrays.stream(bloqueos)
            .max(Comparator.comparing(bloqueo -> bloqueo.getPago().getId()));
    }

    public CostoDiarioCine verUltimoCambioDeCosto(Integer idCine) throws AccesoDeDatosException {
        CostoDiarioCine[] costos = leerCostosPorCine(idCine);

        Optional<CostoDiarioCine> costo = Arrays.stream(costos)
            .max(Comparator.comparing(c -> c.getId()));
        
        if(!costo.isPresent()) throw new AccesoDeDatosException("No hay costos relacionados a este cine");
        
        return costo.get();
        
    }
    
    private BloqueoAnunciosCine[] leerBloqueosPorCine(Integer idCine) throws AccesoDeDatosException {
        FiltrosBloqueoAnunciosCine filtros = new FiltrosBloqueoAnunciosCine();
        filtros.setIdCine(Optional.ofNullable(idCine));
        
        try(Transaccion t = new Transaccion()){
            BloqueoAnunciosCine[] bloqueos = BLOQUEO_BD.leerCompleto(filtros, t.obtenerConexion());
            t.commit();
            return bloqueos;
        }
    }

    public CostoGlobalCinesDTO verCostoGlobalDiario() throws AccesoDeDatosException {
        CostoGlobalCines costo = leerCostoGlobalCines();
        CostoGlobalCinesDTO costoDTO = new CostoGlobalCinesDTO();
        costoDTO.setId(costo.getId().toString());
        costoDTO.setMonto(costo.getMonto().toString());
        return costoDTO;
    }

    public PagoSalidaDTO verDeuda(Integer idCine) throws AccesoDeDatosException {
        Double deudaTotal = calcularDeudaTotal(idCine);
        Double montoPagado = calcularMontoPagado(idCine);
        Double deudaReal = deudaTotal - montoPagado;
        
        PagoSalidaDTO pago = new PagoSalidaDTO();
        pago.setMonto(deudaReal.toString());
        
        return pago;
    }

    private boolean cineTieneBloqueoActivo(Integer id) {
        try {
            Optional<BloqueoAnunciosCine> ultimoBloqueo = verUltimoBloqueoDeAnuncios(id);
            
            if(!ultimoBloqueo.isPresent()) return false;
            
            BloqueoAnunciosCine bloqueo = ultimoBloqueo.get();
            
            Integer dias = bloqueo.getDias();
            LocalDate fechaPago = bloqueo.getPago().getFecha();
            LocalDate hoy = LocalDate.now();
            
            LocalDate fechaVencimiento = fechaPago.plusDays(dias);
            
            return !hoy.isAfter(fechaVencimiento);
        } catch (AccesoDeDatosException ex) {
            return false;
        }
        
    }
    
}
