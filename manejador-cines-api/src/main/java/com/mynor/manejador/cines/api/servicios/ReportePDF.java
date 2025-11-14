/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.servicios;

import com.mynor.manejador.cines.api.dtos.reportes.ReporteGananciasDTO;
import com.mynor.manejador.cines.api.dtos.reportes.SalaReporteDTO;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author mynordma
 */
public class ReportePDF {
    
    private String ruta = "";
    
    public ReportePDF(){
        obtenerRutaReportes();
    }
    
    public byte[] generarReporteComentariosSalas(List<SalaReporteDTO> salas) throws JRException {
        return generarBytesReporteAdminCines(ruta + "reporte-comentarios-salas.jasper", salas);
    }
    
    public byte[] generarReportePeliculasProyectadas(List<SalaReporteDTO> salas) throws JRException {
        return generarBytesReporteAdminCines(ruta + "reporte-peliculas-proyectadas.jasper", salas);
    }
    
    public byte[] generarReporteBoletosVendidos(List<SalaReporteDTO> salas) throws JRException {
        return generarBytesReporteAdminCines(ruta + "reporte-boletos-vendidos.jasper", salas);
    }

    public byte[] generarReporteSalasGustadas(List<SalaReporteDTO> salas) throws JRException {
        return generarBytesReporteAdminCines(ruta + "reporte-salas-gustadas.jasper", salas);
    }
    
    private byte[] generarBytesReporteAdminCines(String rutaParam, List<SalaReporteDTO> salas) throws JRException{
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(
            new File(rutaParam)
        );

        
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(salas);
        
        Map<String, Object> parameters = new HashMap<>();
        
        JasperPrint jasperPrint = JasperFillManager.fillReport(
            jasperReport, 
            parameters, 
            dataSource
        );
                
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public byte[] generarReporteGanancias(ReporteGananciasDTO reporte) throws JRException {
        return generarBytesReporteAdminSistema(ruta + "reporte-ganancias.jasper", reporte);
    }

    private byte[] generarBytesReporteAdminSistema(String rutaParam, ReporteGananciasDTO reporte) throws JRException {
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(
            new File(rutaParam)
        );
        
        List<ReporteGananciasDTO> list = Collections.singletonList(reporte);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
        
        Map<String, Object> parameters = new HashMap<>();
        
        JasperPrint jasperPrint = JasperFillManager.fillReport(
            jasperReport, 
            parameters, 
            dataSource
        );
                
        return JasperExportManager.exportReportToPdf(jasperPrint);
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
}
