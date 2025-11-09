/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.servicios;

import com.mynor.manejador.cines.api.bd.Transaccion;
import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.excepciones.ReporteInvalidoException;
import com.mynor.manejador.cines.api.filtros.FiltrosReporteAdminCines;
import com.mynor.manejador.cines.api.filtros.FiltrosReporteAdminSistema;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * @author mynordma
 */
public class ReporteServicio {
    
    private String ruta = "";
    
    public ReporteServicio(){
        obtenerRutaReportes();
    }
    
    public byte[] verReportePeliculasProyectadas(FiltrosReporteAdminCines filtros) throws AccesoDeDatosException, ReporteInvalidoException, JRException {
        Map<String, Object> parametros = filtros.obtenerParametros();
        return compilarReporte(ruta + "/r_peliculas_proyectadas.jrxml", parametros);
    }

    public byte[] verReporteSalasGustadas(FiltrosReporteAdminCines filtros) throws AccesoDeDatosException, ReporteInvalidoException, JRException {
        Map<String, Object> parametros = filtros.obtenerParametros();
        return compilarReporte(ruta + "/r_calificaciones_salas.jrxml", parametros);
    }

    public byte[] verReporteBoletos(FiltrosReporteAdminCines filtros) throws AccesoDeDatosException, ReporteInvalidoException, JRException {
        Map<String, Object> parametros = filtros.obtenerParametros();
        return compilarReporte(ruta + "/r_boletos_vendidos.jrxml", parametros);
    }
    
    public byte[] verReporteComentariosDeSalas(FiltrosReporteAdminCines filtros) throws AccesoDeDatosException, JRException, ReporteInvalidoException {
        Map<String, Object> parametros = filtros.obtenerParametros();
        return compilarReporte(ruta + "/r_comentarios_salas.jrxml", parametros);
    }
    
    private byte[] compilarReporte(String rutaParam, Map<String, Object> parametros) throws AccesoDeDatosException, ReporteInvalidoException, JRException{
        JasperReport jasperReport = JasperCompileManager.compileReport(rutaParam);
        try(Transaccion t = new Transaccion()){
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, t.obtenerConexion());
            t.commit();
            if(jasperPrint.getPages().isEmpty()) {
                throw new ReporteInvalidoException("El reporte no contiene datos");
            }
            return JasperExportManager.exportReportToPdf(jasperPrint);
        }
    }
    
    private void obtenerRutaReportes(){
        Properties properties = new Properties();
        String rutaActual = System.getProperty("user.dir");
        String nombreArchivo = "config.properties";
        
        try(FileReader fr = new FileReader(rutaActual + File.separator + nombreArchivo)){
            properties.load(fr);
            ruta = properties.getProperty("jr.link");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public byte[] verReporteGanancias(FiltrosReporteAdminSistema filtros) throws AccesoDeDatosException, ReporteInvalidoException, JRException{
        Map<String, Object> parametros = filtros.obtenerParametros();
        return compilarReporte(ruta + "/r_ganancias.jrxml", parametros);
    }

    public byte[] verReporteAnuncios(FiltrosReporteAdminSistema filtros) throws AccesoDeDatosException, ReporteInvalidoException, JRException{
        Map<String, Object> parametros = filtros.obtenerParametros();
        return compilarReporte(ruta + "/r_anuncios.jrxml", parametros);
    }

    public byte[] verReporteGananciasAnunciante(FiltrosReporteAdminSistema filtros) throws AccesoDeDatosException, ReporteInvalidoException, JRException{
        Map<String, Object> parametros = filtros.obtenerParametros();
        return compilarReporte(ruta + "/r_ganancias_anunciante.jrxml", parametros);
    }

    public byte[] verReporteSalasPopulares(FiltrosReporteAdminSistema filtros) throws AccesoDeDatosException, ReporteInvalidoException, JRException{
        Map<String, Object> parametros = filtros.obtenerParametros();
        return compilarReporte(ruta + "/r_salas_populares.jrxml", parametros);
    }

    public byte[] verReporteSalasComentadas(FiltrosReporteAdminSistema filtros) throws AccesoDeDatosException, ReporteInvalidoException, JRException{
        Map<String, Object> parametros = filtros.obtenerParametros();
        return compilarReporte(ruta + "/r_salas_comentadas.jrxml", parametros);
    }

    
    
}
