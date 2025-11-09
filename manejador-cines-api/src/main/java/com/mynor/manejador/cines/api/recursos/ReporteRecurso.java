/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.recursos;

import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.excepciones.AutorizacionException;
import com.mynor.manejador.cines.api.excepciones.EntidadInvalidaException;
import com.mynor.manejador.cines.api.excepciones.ReporteInvalidoException;
import com.mynor.manejador.cines.api.filtros.Filtros;
import com.mynor.manejador.cines.api.filtros.FiltrosReporteAdminCines;
import com.mynor.manejador.cines.api.filtros.FiltrosReporteAdminSistema;
import com.mynor.manejador.cines.api.filtros.FiltrosReportes;
import com.mynor.manejador.cines.api.seguridad.Autorizacion;
import com.mynor.manejador.cines.api.servicios.ReporteServicio;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author mynordma
 */
@Path("reportes")
public class ReporteRecurso {
    
    @POST
    @Path("admin-cines/{tipo}") // comentarios/peliculas/salas/boletos
    @Produces("application/pdf")
    public Response verReporteAdminCines(@HeaderParam("Authorization") String authHeader, FiltrosReporteAdminCines filtros, @PathParam("tipo") String tipo) {
        
        ReporteServicio servicio = new ReporteServicio();
        
        try {
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarAdminCines();
            
            filtros.validarEntrada();
            
            byte[] pdfBytes = obtenerReporte(servicio, filtros, tipo);
            
            return Response
                .ok(pdfBytes)
                .header("Content-Disposition", "inline; filename=\"reporte.pdf\"")
                .type("application/pdf")
                .build();
                
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(ex.getMessage()).build();
        } catch (JRException ex) {
            System.err.println("Error JRException: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("No se pudo cargar el reporte: " + ex.getMessage()).build();
        } catch (EntidadInvalidaException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @POST
    @Path("admin-sistema/{tipo}") // ganancias/anuncios/ganancias-anunciante/salas-populares/salas-comentadas
    @Produces("application/pdf")
    public Response verReporteAdminSistema(@HeaderParam("Authorization") String authHeader, FiltrosReporteAdminSistema filtros, @PathParam("tipo") String tipo) {
        
        ReporteServicio servicio = new ReporteServicio();
        
        try {
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarAdminSistema();
            
            filtros.validarEntrada();
            
            byte[] pdfBytes = obtenerReporte(servicio, filtros, tipo);
            
            return Response
                .ok(pdfBytes)
                .header("Content-Disposition", "inline; filename=\"reporte.pdf\"")
                .type("application/pdf")
                .build();
                
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(ex.getMessage()).build();
        } catch (JRException ex) {
            System.err.println("Error JRException: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("No se pudo cargar el reporte: " + ex.getMessage()).build();
        } catch (EntidadInvalidaException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    private byte[] obtenerReporte(ReporteServicio servicio, FiltrosReportes filtros, String tipo) throws AccesoDeDatosException, JRException, ReporteInvalidoException {
        if(tipo.equals("comentarios")){
            return servicio.verReporteComentariosDeSalas((FiltrosReporteAdminCines) filtros);
        } else if(tipo.equals("peliculas")){
            return servicio.verReportePeliculasProyectadas((FiltrosReporteAdminCines) filtros);
        } else if(tipo.equals("salas")){
            return servicio.verReporteSalasGustadas((FiltrosReporteAdminCines) filtros);
        } else if(tipo.equals("boletos")){
            return servicio.verReporteBoletos((FiltrosReporteAdminCines) filtros);
        } else if(tipo.equals("ganancias")){
            return servicio.verReporteGanancias((FiltrosReporteAdminSistema) filtros);
        } else if(tipo.equals("anuncios")){
            return servicio.verReporteAnuncios((FiltrosReporteAdminSistema) filtros);
        } else if(tipo.equals("ganancias-anunciante")){
            return servicio.verReporteGananciasAnunciante((FiltrosReporteAdminSistema) filtros);
        } else if(tipo.equals("salas-populares")){
            return servicio.verReporteSalasPopulares((FiltrosReporteAdminSistema) filtros);
        } else if(tipo.equals("salas-comentadas")){
            return servicio.verReporteSalasComentadas((FiltrosReporteAdminSistema) filtros);
        } else {
            throw new AccesoDeDatosException("Tipo de reporte no válido: " + tipo);
        }
    }
}
