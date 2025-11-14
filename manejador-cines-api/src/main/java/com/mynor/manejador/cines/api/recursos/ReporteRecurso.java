/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.recursos;

import com.mynor.manejador.cines.api.dtos.reportes.*;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.excepciones.AutorizacionException;
import com.mynor.manejador.cines.api.seguridad.Autorizacion;
import com.mynor.manejador.cines.api.servicios.ReportePDF;
import com.mynor.manejador.cines.api.servicios.ReporteServicio;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.time.format.DateTimeParseException;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author mynordma
 */
@Path("reportes")
public class ReporteRecurso {
    
    // Admin cines
    
    @GET
    @Path("comentarios-salas/{pdf}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verReporteComentariosSalas(
            @HeaderParam("Authorization") String authHeader, 
            @QueryParam("fecha-inicial") String fechaInicial,
            @QueryParam("fecha-final") String fechaFinal, 
            @QueryParam("id-sala") String idSala,
            @PathParam("pdf") String pdf) {
        
        ReporteServicio servicio = new ReporteServicio();
        
        try {
            Autorizacion autorizacion = new Autorizacion(authHeader);
            Integer idCreador = autorizacion.validarSesion().getId();
            
            ReporteComentariosSalasDTO reporte = servicio.obtenerReporteComentariosSalas(fechaInicial, fechaFinal, idSala, idCreador);
            
            if(pdf.equals("true")){
                ReportePDF reportePDF = new ReportePDF();
                byte[] pdfBytes = reportePDF.generarReporteComentariosSalas(reporte.getSalas());
        
                return Response.ok(pdfBytes)
                    .header("Content-Disposition", "attachment; filename=reporte_salas.pdf")
                    .build();
            }
            
            return Response.ok(reporte.getSalas()).build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (DateTimeParseException | NumberFormatException e){
            return Response.status(Response.Status.BAD_REQUEST).entity("Filtro invalido").build();
        } catch (JRException ex) {
            System.out.println(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al cargar el reporte").build();
        }
    }
    
    @GET
    @Path("peliculas-proyectadas/{pdf}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verReportePeliculasProyectadas(
            @HeaderParam("Authorization") String authHeader,
            @QueryParam("fecha-inicial") String fechaInicial,
            @QueryParam("fecha-final") String fechaFinal,
            @QueryParam("id-sala") String idSala,
            @PathParam("pdf") String pdf) {

        ReporteServicio servicio = new ReporteServicio();

        try {
            Autorizacion autorizacion = new Autorizacion(authHeader);
            Integer idCreador = autorizacion.validarSesion().getId();

            ReportePeliculasProyectadasDTO reporte = servicio.obtenerReportePeliculasProyectadas(
                fechaInicial, fechaFinal, idSala, idCreador);
            
            if(pdf.equals("true")){
                ReportePDF reportePDF = new ReportePDF();
                byte[] pdfBytes = reportePDF.generarReportePeliculasProyectadas(reporte.getSalas());
        
                return Response.ok(pdfBytes)
                    .header("Content-Disposition", "attachment; filename=reporte_salas.pdf")
                    .build();
            }

            return Response.ok(reporte.getSalas()).build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(ex.getMessage()).build();
        }catch (DateTimeParseException | NumberFormatException e){
            return Response.status(Response.Status.BAD_REQUEST).entity("Filtro invalido").build();
        }catch (JRException ex) {
            System.out.println(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al cargar el reporte").build();
        }
    }

    @GET
    @Path("salas-mas-gustadas/{pdf}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verReporteSalasMasGustadas(
            @HeaderParam("Authorization") String authHeader,
            @QueryParam("fecha-inicial") String fechaInicial,
            @QueryParam("fecha-final") String fechaFinal,
            @QueryParam("id-sala") String idSala,
            @PathParam("pdf") String pdf) {

        ReporteServicio servicio = new ReporteServicio();

        try {
            Autorizacion autorizacion = new Autorizacion(authHeader);
            Integer idCreador = autorizacion.validarSesion().getId();

            ReporteSalasMasGustadasDTO reporte = servicio.obtenerReporteSalasMasGustadas(
                fechaInicial, fechaFinal, idSala, idCreador);
            
            if(pdf.equals("true")){
                ReportePDF reportePDF = new ReportePDF();
                byte[] pdfBytes = reportePDF.generarReporteSalasGustadas(reporte.getSalas());
        
                return Response.ok(pdfBytes)
                    .header("Content-Disposition", "attachment; filename=reporte_salas.pdf")
                    .build();
            }

            return Response.ok(reporte.getSalas()).build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(ex.getMessage()).build();
        }catch (DateTimeParseException | NumberFormatException e){
            return Response.status(Response.Status.BAD_REQUEST).entity("Filtro invalido").build();
        }catch (JRException ex) {
            System.out.println(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al cargar el reporte").build();
        }
    }

    @GET
    @Path("boletos-vendidos/{pdf}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verReporteBoletoVendidos(
            @HeaderParam("Authorization") String authHeader,
            @QueryParam("fecha-inicial") String fechaInicial,
            @QueryParam("fecha-final") String fechaFinal,
            @QueryParam("id-sala") String idSala,
            @PathParam("pdf") String pdf) {

        ReporteServicio servicio = new ReporteServicio();

        try {
            Autorizacion autorizacion = new Autorizacion(authHeader);
            Integer idCreador = autorizacion.validarSesion().getId();

            ReporteBoletoVendidosDTO reporte = servicio.obtenerReporteBoletoVendidos(
                fechaInicial, fechaFinal, idSala, idCreador);
            
            if(pdf.equals("true")){
                ReportePDF reportePDF = new ReportePDF();
                byte[] pdfBytes = reportePDF.generarReporteBoletosVendidos(reporte.getSalas());
        
                return Response.ok(pdfBytes)
                    .header("Content-Disposition", "attachment; filename=reporte_salas.pdf")
                    .build();
            }

            return Response.ok(reporte.getSalas()).build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(ex.getMessage()).build();
        }catch (DateTimeParseException | NumberFormatException e){
            return Response.status(Response.Status.BAD_REQUEST).entity("Filtro invalido").build();
        }catch (JRException ex) {
            System.out.println(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al cargar el reporte").build();
        }
    }

    // Admin sistema

    @GET
    @Path("ganancias/{pdf}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verReporteGanancias(
            @HeaderParam("Authorization") String authHeader,
            @QueryParam("fecha-inicial") String fechaInicial,
            @QueryParam("fecha-final") String fechaFinal,
            @PathParam("pdf") String pdf) {

        ReporteServicio servicio = new ReporteServicio();

        try {
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarAdminSistema();

            ReporteGananciasDTO reporte = servicio.obtenerReporteGanancias(
                fechaInicial, fechaFinal);
            
            if(pdf.equals("true")){
                ReportePDF reportePDF = new ReportePDF();
                byte[] pdfBytes = reportePDF.generarReporteGanancias(reporte);
        
                return Response.ok(pdfBytes)
                    .header("Content-Disposition", "attachment; filename=reporte_salas.pdf")
                    .build();
            }

            return Response.ok(reporte).build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(ex.getMessage()).build();
        }catch (JRException ex) {
            System.out.println(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al cargar el reporte").build();
        }
    }

    @GET
    @Path("anuncios-comprados")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verReporteAnunciosComprados(
            @HeaderParam("Authorization") String authHeader,
            @QueryParam("fecha-inicial") String fechaInicial,
            @QueryParam("fecha-final") String fechaFinal,
            @QueryParam("tipo-anuncio") String tipoAnuncio,
            @QueryParam("periodo") String periodo) {

        ReporteServicio servicio = new ReporteServicio();

        try {
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarAdminSistema();

            ReporteAnunciosCompradosDTO reporte = servicio.obtenerReporteAnunciosComprados(
                fechaInicial, fechaFinal, tipoAnuncio, periodo);

            return Response.ok(reporte).build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("ganancias-anunciante")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verReporteGananciasAnunciante(
            @HeaderParam("Authorization") String authHeader,
            @QueryParam("fecha-inicial") String fechaInicial,
            @QueryParam("fecha-final") String fechaFinal,
            @QueryParam("id-anunciante") String idAnunciante) {

        ReporteServicio servicio = new ReporteServicio();

        try {
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarAdminSistema();

            ReporteGananciasAnuncianteDTO reporte = servicio.obtenerReporteGananciasAnunciante(
                fechaInicial, fechaFinal, idAnunciante);

            return Response.ok(reporte).build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("salas-mas-populares")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verReporteSalasMasPopulares(
            @HeaderParam("Authorization") String authHeader,
            @QueryParam("fecha-inicial") String fechaInicial,
            @QueryParam("fecha-final") String fechaFinal) {

        ReporteServicio servicio = new ReporteServicio();

        try {
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarAdminSistema();

            ReporteSalasMasPopularesDTO reporte = servicio.obtenerReporteSalasMasPopulares(
                fechaInicial, fechaFinal);

            return Response.ok(reporte).build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("salas-mas-comentadas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verReporteSalasMasComentadas(
            @HeaderParam("Authorization") String authHeader,
            @QueryParam("fecha-inicial") String fechaInicial,
            @QueryParam("fecha-final") String fechaFinal) {

        ReporteServicio servicio = new ReporteServicio();

        try {
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarAdminSistema();

            ReporteSalasMasComentadasDTO reporte = servicio.obtenerReporteSalasMasComentadas(
                fechaInicial, fechaFinal);

            return Response.ok(reporte).build();
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(e.getMessage()).build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(ex.getMessage()).build();
        }
    }
}
