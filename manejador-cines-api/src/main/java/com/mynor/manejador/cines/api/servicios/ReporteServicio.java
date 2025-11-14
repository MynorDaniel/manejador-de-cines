/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.servicios;

import com.mynor.manejador.cines.api.bd.BaseDeDatos;
import com.mynor.manejador.cines.api.bd.BloqueoAnunciosCineBD;
import com.mynor.manejador.cines.api.bd.BoletoBD;
import com.mynor.manejador.cines.api.bd.CalificacionBD;
import com.mynor.manejador.cines.api.bd.CalificacionSalaBD;
import com.mynor.manejador.cines.api.bd.ComentarioSalaBD;
import com.mynor.manejador.cines.api.bd.ProyeccionBD;
import com.mynor.manejador.cines.api.bd.SalaBD;
import com.mynor.manejador.cines.api.bd.Transaccion;
import com.mynor.manejador.cines.api.dtos.AnuncioSalidaDTO;
import com.mynor.manejador.cines.api.dtos.reportes.*;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.filtros.FiltrosAnuncio;
import com.mynor.manejador.cines.api.filtros.FiltrosBloqueoAnunciosCine;
import com.mynor.manejador.cines.api.filtros.FiltrosBoleto;
import com.mynor.manejador.cines.api.filtros.FiltrosCalificacion;
import com.mynor.manejador.cines.api.filtros.FiltrosCalificacionSala;
import com.mynor.manejador.cines.api.filtros.FiltrosComentarioSala;
import com.mynor.manejador.cines.api.filtros.FiltrosCostoDiarioCine;
import com.mynor.manejador.cines.api.filtros.FiltrosProyeccion;
import com.mynor.manejador.cines.api.filtros.FiltrosSala;
import com.mynor.manejador.cines.api.modelo.BloqueoAnunciosCine;
import com.mynor.manejador.cines.api.modelo.Boleto;
import com.mynor.manejador.cines.api.modelo.Calificacion;
import com.mynor.manejador.cines.api.modelo.CalificacionSala;
import com.mynor.manejador.cines.api.modelo.Cine;
import com.mynor.manejador.cines.api.modelo.ComentarioSala;
import com.mynor.manejador.cines.api.modelo.Proyeccion;
import com.mynor.manejador.cines.api.modelo.Sala;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author mynordma
 */
public class ReporteServicio {
    
    private final BaseDeDatos<Sala, FiltrosSala> SALA_BD;
    private final BaseDeDatos<CalificacionSala, FiltrosCalificacionSala> CALIFICACION_SALA_BD;
    private final BaseDeDatos<Calificacion, FiltrosCalificacion> CALIFICACION_BD;
    private final BaseDeDatos<Proyeccion, FiltrosProyeccion> PROYECCION_BD;
    private final BaseDeDatos<Boleto, FiltrosBoleto> BOLETO_BD;
    private final BaseDeDatos<ComentarioSala, FiltrosComentarioSala> COMENTARIO_SALA_BD;
    private final BaseDeDatos<BloqueoAnunciosCine, FiltrosBloqueoAnunciosCine> BLOQUEO_BD;
    
    public ReporteServicio(){
        SALA_BD = new SalaBD();
        CALIFICACION_SALA_BD = new CalificacionSalaBD();
        CALIFICACION_BD = new CalificacionBD();
        COMENTARIO_SALA_BD = new ComentarioSalaBD();
        PROYECCION_BD = new ProyeccionBD();
        BOLETO_BD = new BoletoBD();
        BLOQUEO_BD = new BloqueoAnunciosCineBD();
    }
    
    // Admin cines

    public ReporteComentariosSalasDTO obtenerReporteComentariosSalas(String fechaInicial, String fechaFinal, String idSala, Integer idCreador) 
            throws NumberFormatException, AccesoDeDatosException, DateTimeParseException {

        LocalDate fechaInicialDate = StringUtils.isBlank(fechaInicial) ? null : LocalDate.parse(fechaInicial);
        LocalDate fechaFinalDate = StringUtils.isBlank(fechaFinal) ? null : LocalDate.parse(fechaFinal);

        // Obtener salas
        FiltrosSala filtrosSala = new FiltrosSala();
        filtrosSala.setIdCreador(Optional.ofNullable(idCreador));
        if(!StringUtils.isBlank(idSala)){
            filtrosSala.setId(Optional.ofNullable(Integer.valueOf(idSala)));
        }
        SalaReporteDTO[] salasReporte = obtenerSalasReporte(filtrosSala);

        for (SalaReporteDTO salaReporteDTO : salasReporte) {
            FiltrosComentarioSala filtrosComentario = new FiltrosComentarioSala();
            filtrosComentario.setIdSala(Optional.ofNullable(Integer.valueOf(salaReporteDTO.getId())));

            ComentarioReporteDTO[] comentariosReporte = obtenerComentariosReporte(filtrosComentario);
            
            // Filtrar por fecha
            if(comentariosReporte != null) {
                comentariosReporte = filtrarPorFecha(Arrays.stream(comentariosReporte), fechaInicialDate, fechaFinalDate).toArray(ComentarioReporteDTO[]::new);
                salaReporteDTO.setComentarios(Arrays.asList(comentariosReporte));
            }
            
            
        }

        // Asignar salas con comentarios no vacios
        ReporteComentariosSalasDTO reporte = new ReporteComentariosSalasDTO();
        reporte.setSalas(Arrays.stream(salasReporte)
            .filter(sala -> sala.getComentarios() != null && !sala.getComentarios().isEmpty())
            .toList());

        return reporte;
    }

    public ReportePeliculasProyectadasDTO obtenerReportePeliculasProyectadas(String fechaInicial, String fechaFinal, String idSala, Integer idCreador) 
            throws AccesoDeDatosException, DateTimeParseException, NumberFormatException {
        LocalDate fechaInicialDate = StringUtils.isBlank(fechaInicial) ? null : LocalDate.parse(fechaInicial);
        LocalDate fechaFinalDate = StringUtils.isBlank(fechaFinal) ? null : LocalDate.parse(fechaFinal);

        // Obtener salas
        FiltrosSala filtrosSala = new FiltrosSala();
        filtrosSala.setIdCreador(Optional.ofNullable(idCreador));
        if(!StringUtils.isBlank(idSala)){
            filtrosSala.setId(Optional.ofNullable(Integer.valueOf(idSala)));
        }
        SalaReporteDTO[] salasReporte = obtenerSalasReporte(filtrosSala);
        
        // Obtener proyecciones
        for (SalaReporteDTO salaReporteDTO : salasReporte) {
            FiltrosProyeccion filtrosProyeccion = new FiltrosProyeccion();
            filtrosProyeccion.setIdSala(Optional.ofNullable(Integer.valueOf(salaReporteDTO.getId())));
            ProyeccionReporteDTO[] proyeccionesReporte = obtenerProyeccionesReporte(filtrosProyeccion);
            
            // Filtrar por fecha
            if(proyeccionesReporte != null) {
                proyeccionesReporte = filtrarPorFecha(Arrays.stream(proyeccionesReporte), fechaInicialDate, fechaFinalDate).toArray(ProyeccionReporteDTO[]::new);
                salaReporteDTO.setProyecciones(Arrays.asList(proyeccionesReporte));
            }
            
        }
        
        // Asignar salas con proyecciones no vacias
        ReportePeliculasProyectadasDTO reporte = new ReportePeliculasProyectadasDTO();
        reporte.setSalas(Arrays.stream(salasReporte).filter(sala -> sala.getProyecciones()!= null).toList());
        
        return reporte;
    }

    public ReporteSalasMasGustadasDTO obtenerReporteSalasMasGustadas(String fechaInicial, String fechaFinal, String idSala, Integer idCreador) 
            throws AccesoDeDatosException, DateTimeParseException, NumberFormatException  {
        LocalDate fechaInicialDate = StringUtils.isBlank(fechaInicial) ? null : LocalDate.parse(fechaInicial);
        LocalDate fechaFinalDate = StringUtils.isBlank(fechaFinal) ? null : LocalDate.parse(fechaFinal);

        // Obtener salas
        FiltrosSala filtrosSala = new FiltrosSala();
        filtrosSala.setIdCreador(Optional.ofNullable(idCreador));
        if(!StringUtils.isBlank(idSala)){
            filtrosSala.setId(Optional.ofNullable(Integer.valueOf(idSala)));
        }
        SalaReporteDTO[] salasReporte = obtenerSalasReporte(filtrosSala);
        
        // Obtener proyecciones
        for (SalaReporteDTO salaReporteDTO : salasReporte) {
            FiltrosCalificacionSala filtrosCalificacionSala = new FiltrosCalificacionSala();
            filtrosCalificacionSala.setIdSala(Optional.ofNullable(Integer.valueOf(salaReporteDTO.getId())));
            CalificacionReporteDTO[] calificacionesReporte = obtenerCalificacionesReporte(filtrosCalificacionSala);
            
            // Filtrar por fecha
            if(calificacionesReporte != null) {
                calificacionesReporte = filtrarPorFecha(Arrays.stream(calificacionesReporte), fechaInicialDate, fechaFinalDate).toArray(CalificacionReporteDTO[]::new);
                salaReporteDTO.setCalificaciones(Arrays.asList(calificacionesReporte));
            }
            
        }
        
        // Asignar salas con calificaciones no vacias
        ReporteSalasMasGustadasDTO reporte = new ReporteSalasMasGustadasDTO();
        reporte.setSalas(Arrays.stream(salasReporte)
            .filter(sala -> sala.getCalificaciones()!= null)
            .peek(sala -> sala.setPromedioCalificacion(obtenerCalificacionPromedioSala(sala)))
            .sorted((s1, s2) -> Double.compare(Double.parseDouble(s2.getPromedioCalificacion()), Double.parseDouble(s1.getPromedioCalificacion())))
            .limit(5)
            .toList());
        
        return reporte;
    }

    public ReporteBoletoVendidosDTO obtenerReporteBoletoVendidos(String fechaInicial, String fechaFinal, String idSala, Integer idCreador) 
            throws AccesoDeDatosException, DateTimeParseException, NumberFormatException {
        LocalDate fechaInicialDate = StringUtils.isBlank(fechaInicial) ? null : LocalDate.parse(fechaInicial);
        LocalDate fechaFinalDate = StringUtils.isBlank(fechaFinal) ? null : LocalDate.parse(fechaFinal);

        // Obtener salas
        FiltrosSala filtrosSala = new FiltrosSala();
        filtrosSala.setIdCreador(Optional.ofNullable(idCreador));
        if(!StringUtils.isBlank(idSala)){
            filtrosSala.setId(Optional.ofNullable(Integer.valueOf(idSala)));
        }
        SalaReporteDTO[] salasReporte = obtenerSalasReporte(filtrosSala);
        
        // Obtener boletos
        for (SalaReporteDTO salaReporteDTO : salasReporte) {
            FiltrosBoleto filtrosBoleto = new FiltrosBoleto();
            filtrosBoleto.setIdSala(Optional.ofNullable(Integer.valueOf(salaReporteDTO.getId())));
            BoletoReporteDTO[] boletosReporte = obtenerBoletosReporte(filtrosBoleto);
            
            // Filtrar por fecha
            if(boletosReporte != null) {
                boletosReporte = filtrarPorFecha(Arrays.stream(boletosReporte), fechaInicialDate, fechaFinalDate).toArray(BoletoReporteDTO[]::new);
                salaReporteDTO.setBoletos(Arrays.asList(boletosReporte));
            }
            
        }
        
        // Asignar salas con boletos no vacios
        ReporteBoletoVendidosDTO reporte = new ReporteBoletoVendidosDTO();
        reporte.setSalas(Arrays.stream(salasReporte)
                .filter(sala -> sala.getBoletos()!= null)
                .peek(sala -> sala.setTotalDineroBoletosVendidos(obtenerTotalDineroBoletosVendidos(sala)))
                .toList());
        
        return reporte;
    }

    // Admin sistema
    
    public ReporteGananciasDTO obtenerReporteGanancias(String fechaInicial, String fechaFinal) {
        ReporteGananciasDTO reporte = new ReporteGananciasDTO();
        Double costo = 0.0;
        Double ingreso = 0.0;
        Double ganancia = 0.0;
        
        LocalDate fechaInicialDate = StringUtils.isBlank(fechaInicial) ? null : LocalDate.parse(fechaInicial);
        LocalDate fechaFinalDate = StringUtils.isBlank(fechaFinal) ? null : LocalDate.parse(fechaFinal);

        // Obtener cines
        CineReporteDTO[] cines = obtenerCinesReporte();
        
        // Obtener anuncios
        AnuncioReporteDTO[] anuncios = obtenerAnunciosReporte();
        
        // Filtrar por fecha
        if(anuncios != null) {
            anuncios = filtrarPorFecha(Arrays.stream(anuncios), fechaInicialDate, fechaFinalDate).toArray(AnuncioReporteDTO[]::new);
            reporte.setAnuncios(Arrays.asList(anuncios));
            for (AnuncioReporteDTO anuncio : anuncios) {
                ingreso += Double.valueOf(anuncio.getCosto());
            }
        }
        
        // Obtener bloqueos de anuncios
        BloqueoAnunciosReporteDTO[] bloqueos = obtenerBloqueosReporte();
        
        // Asignar datos con valores no vacios
        if(cines != null){
            reporte.setCines(Arrays.asList(cines));
            for (CineReporteDTO cine : cines) {
                costo += Double.valueOf(cine.getMontoPagado());
            }
        }
        
        if(bloqueos != null){
            reporte.setBloqueos(Arrays.asList(bloqueos));
            for (BloqueoAnunciosReporteDTO bloqueo : bloqueos) {
                ingreso += Double.valueOf(bloqueo.getMontoPagado());
            }
        }
        
        reporte.setCosto(costo.toString());
        reporte.setIngreso(ingreso.toString());
        ganancia = ingreso - costo;
        reporte.setGanancia(ganancia.toString());
        
        return reporte;
    }

    public ReporteAnunciosCompradosDTO obtenerReporteAnunciosComprados(String fechaInicial, String fechaFinal, String tipoAnuncio, String periodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public ReporteGananciasAnuncianteDTO obtenerReporteGananciasAnunciante(String fechaInicial, String fechaFinal, String idAnunciante) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public ReporteSalasMasPopularesDTO obtenerReporteSalasMasPopulares(String fechaInicial, String fechaFinal) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public ReporteSalasMasComentadasDTO obtenerReporteSalasMasComentadas(String fechaInicial, String fechaFinal) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    // Metodos auxiliares
    
    private SalaReporteDTO[] obtenerSalasReporte(FiltrosSala filtrosSala) throws AccesoDeDatosException{

        try(Transaccion t = new Transaccion()){
            Sala[] salas = SALA_BD.leerCompleto(filtrosSala, t.obtenerConexion());
            if(salas.length < 1) throw new AccesoDeDatosException("Sin coincidencias al buscar salas");
            
            return Arrays.stream(salas).map(sala -> {
                SalaReporteDTO salaReporte = new SalaReporteDTO();
                salaReporte.setCalificacionesBloqueadas(sala.getCalificacionesBloqueadas().toString());
                salaReporte.setColumnas(sala.getColumnasAsientos().toString());
                salaReporte.setComentariosBloqueados(sala.getComentariosBloqueados().toString());
                salaReporte.setFilas(sala.getFilasAsientos().toString());
                salaReporte.setId(sala.getId().toString());
                salaReporte.setNombreCine(sala.getCine().getNombre());
                salaReporte.setVisible(sala.getVisible().toString());
                
                return salaReporte;
            }).toArray(SalaReporteDTO[]::new);
        }
    }
    
    private ComentarioReporteDTO[] obtenerComentariosReporte(FiltrosComentarioSala filtrosComentario) throws AccesoDeDatosException{
        try(Transaccion t = new Transaccion()){
            ComentarioSala[] comentariosSala = COMENTARIO_SALA_BD.leerCompleto(filtrosComentario, t.obtenerConexion());
            if(comentariosSala.length < 1) return null;
            
            return Arrays.stream(comentariosSala).map(comentario -> {
                ComentarioReporteDTO comentarioReporte = new ComentarioReporteDTO();
                
                comentarioReporte.setContenido(comentario.getContenido());
                comentarioReporte.setCorreoUsuario(comentario.getUsuario().getCorreo());
                comentarioReporte.setFecha(comentario.getFecha().toString());
                comentarioReporte.setId(comentario.getId().toString());
                
                return comentarioReporte;
            }).toArray(ComentarioReporteDTO[]::new);
        }
    }

    private ProyeccionReporteDTO[] obtenerProyeccionesReporte(FiltrosProyeccion filtrosProyeccion) throws AccesoDeDatosException {
        try(Transaccion t = new Transaccion()){
            Proyeccion[] proyecciones = PROYECCION_BD.leerCompleto(filtrosProyeccion, t.obtenerConexion());
            if(proyecciones.length < 1) return null;
            
            return Arrays.stream(proyecciones).map(proyeccion -> {
                ProyeccionReporteDTO proyeccionReporte = new ProyeccionReporteDTO();
                
                proyeccionReporte.setClasificacion(proyeccion.getPelicula().getClasificacion().getCodigo());
                proyeccionReporte.setDirector(proyeccion.getPelicula().getDirector());
                proyeccionReporte.setDuracion(proyeccion.getPelicula().getDuracion().toString());
                proyeccionReporte.setFecha(proyeccion.getFecha().toString());
                proyeccionReporte.setFechaEstreno(proyeccion.getPelicula().getFechaEstreno().toString());
                proyeccionReporte.setHora(proyeccion.getHora().toString());
                proyeccionReporte.setTitulo(proyeccion.getPelicula().getTitulo());
                
                return proyeccionReporte;
            }).toArray(ProyeccionReporteDTO[]::new);
        }
    }

    private CalificacionReporteDTO[] obtenerCalificacionesReporte(FiltrosCalificacionSala filtrosCalificacionSala) throws AccesoDeDatosException {
        try(Transaccion t = new Transaccion()){
            CalificacionSala[] calificaciones = CALIFICACION_SALA_BD.leerCompleto(filtrosCalificacionSala, t.obtenerConexion());
            if(calificaciones.length < 1) return null;
            
            return Arrays.stream(calificaciones).map((CalificacionSala calificacion) -> {
                CalificacionReporteDTO calificacionReporte = new CalificacionReporteDTO();
                
                calificacionReporte.setCorreoUsuario(calificacion.getUsuario().getCorreo());
                calificacionReporte.setFecha(calificacion.getFecha().toString());
                calificacionReporte.setValor(calificacion.getValor().toString());
                
                return calificacionReporte;
            }).toArray(CalificacionReporteDTO[]::new);
        }
    }

    private String obtenerCalificacionPromedioSala(SalaReporteDTO sala) {
        Double totalCalificaciones = 0.0;
        Double promedio;
        for (CalificacionReporteDTO c : sala.getCalificaciones()) {
            totalCalificaciones += Double.parseDouble(c.getValor());
        }
        promedio = (totalCalificaciones / sala.getCalificaciones().size());
        return promedio.toString();
    }

    private BoletoReporteDTO[] obtenerBoletosReporte(FiltrosBoleto filtrosBoleto) throws AccesoDeDatosException {
        try(Transaccion t = new Transaccion()){
            Boleto[] boletos = BOLETO_BD.leerCompleto(filtrosBoleto, t.obtenerConexion());
            if(boletos.length < 1) return null;
            
            return Arrays.stream(boletos).map(b -> {
                BoletoReporteDTO boletoReporte = new BoletoReporteDTO();
                
                boletoReporte.setCorreoUsuario(b.getUsuario().getCorreo());
                boletoReporte.setId(b.getId().toString());
                boletoReporte.setMontoPagado(b.getPago().getMonto().toString());
                boletoReporte.setTituloPelicula(b.getProyeccion().getPelicula().getTitulo());
                boletoReporte.setFecha(b.getPago().getFecha().toString());
                
                return boletoReporte;
            }).toArray(BoletoReporteDTO[]::new);
        }
    }

    private String obtenerTotalDineroBoletosVendidos(SalaReporteDTO sala) {
        Double total = 0.0;
        
        for (BoletoReporteDTO b : sala.getBoletos()) {
            total += Double.valueOf(b.getMontoPagado());
        }
        
        return total.toString();
    }

    private Stream<ReporteConFechaDTO> filtrarPorFecha(Stream<ReporteConFechaDTO> stream, LocalDate fechaInicialDate, LocalDate fechaFinalDate) {
        return stream.filter(v -> {
                    if (v.getFecha() == null) {
                        return false;
                    }
                    
                    try {
                        LocalDate fechaValor = LocalDate.parse(v.getFecha());
                        
                        boolean despuesDeFechaInicial = fechaInicialDate == null || 
                        !fechaValor.isBefore(fechaInicialDate);

                        boolean antesDeFechaFinal = fechaFinalDate == null || 
                            !fechaValor.isAfter(fechaFinalDate);

                        return despuesDeFechaInicial && antesDeFechaFinal;
                
                    } catch (DateTimeParseException e) {
                        return false;
                    }
                });
    }

    private CineReporteDTO[] obtenerCinesReporte() {
        CineServicio cineServicio = new CineServicio();
        try {
            Cine[] cines = cineServicio.leerTodosLosCines();
            return Arrays.stream(cines).map(cine -> {
                CineReporteDTO cineReporte = new CineReporteDTO();
                cineReporte.setIdCine(cine.getId().toString());
                cineReporte.setNombre(cine.getNombre());
                try {
                    cineReporte.setMontoAdeudado(cineServicio.calcularDeudaTotal(cine.getId()).toString());
                    cineReporte.setMontoPagado(cineServicio.calcularMontoPagado(cine.getId()).toString());
                } catch (AccesoDeDatosException e) {
                    System.out.println(e.getMessage());
                }
                
                return cineReporte;
            }).toArray(CineReporteDTO[]::new);
        } catch (AccesoDeDatosException ex) {
            return null;
        }
    }

    private AnuncioReporteDTO[] obtenerAnunciosReporte() {
        AnuncioServicio anuncioServicio = new AnuncioServicio();
        try {
            AnuncioSalidaDTO[] anuncios = anuncioServicio.obtenerTodos();
            return Arrays.stream(anuncios).map(a -> {
                AnuncioReporteDTO anuncioReporte = new AnuncioReporteDTO();
                anuncioReporte.setCosto(a.getPago().getMonto());
                anuncioReporte.setFecha(a.getPago().getFecha());
                anuncioReporte.setId(a.getId());
                anuncioReporte.setTexto(a.getTexto());
                anuncioReporte.setTipo(a.getTipo());
                anuncioReporte.setVigencia(a.getVigencia());
                return anuncioReporte;
            }).toArray(AnuncioReporteDTO[]::new);
        } catch (AccesoDeDatosException ex) {
            return null;
        }
    }

    private BloqueoAnunciosReporteDTO[] obtenerBloqueosReporte() {
        FiltrosBloqueoAnunciosCine filtros = new FiltrosBloqueoAnunciosCine();
        
        try(Transaccion t = new Transaccion()){
            BloqueoAnunciosCine[] bloqueos = BLOQUEO_BD.leerCompleto(filtros, t.obtenerConexion());
            t.commit();
            if(bloqueos.length < 1) return null;
            return Arrays.stream(bloqueos).map(b -> {
                BloqueoAnunciosReporteDTO bloqueoReporte = new BloqueoAnunciosReporteDTO();
                bloqueoReporte.setDias(b.getDias().toString());
                bloqueoReporte.setMontoPagado(b.getPago().getMonto().toString());
                bloqueoReporte.setAdministradorCine(b.getPago().getUsuario().getCorreo());
                return bloqueoReporte;
            }).toArray(BloqueoAnunciosReporteDTO[]::new);
        } catch (AccesoDeDatosException ex) {
            return null;
        }
    }
}