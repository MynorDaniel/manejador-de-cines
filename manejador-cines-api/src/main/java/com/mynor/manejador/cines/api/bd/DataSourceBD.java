/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.bd;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

/**
 *
 * @author mynordma
 */
public class DataSourceBD {
    
    private String url;
    private String usuario;
    private String clave;

    private static DataSourceBD INSTANCIA_DATASOURCEBD;

    private DataSource datasource;

    private DataSourceBD() {
        
        obtenerCredenciales();
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            PoolProperties p = new PoolProperties();
            p.setUrl(url);
            p.setDriverClassName("com.mysql.cj.jdbc.Driver");
            p.setUsername(usuario);
            p.setPassword(clave);
            p.setJmxEnabled(true);
            p.setTestWhileIdle(false);
            p.setTestOnBorrow(true);
            p.setValidationQuery("SELECT 1");
            p.setTestOnReturn(false);
            p.setValidationInterval(30000);
            p.setTimeBetweenEvictionRunsMillis(30000);
            p.setMaxActive(100);
            p.setInitialSize(10);
            p.setMaxWait(10000);
            p.setRemoveAbandonedTimeout(60);
            p.setMinEvictableIdleTimeMillis(30000);
            p.setMinIdle(10);
            p.setLogAbandoned(true);
            p.setRemoveAbandoned(true);
            p.setJdbcInterceptors(
                    "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
                    + "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
            datasource = new DataSource(p);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static DataSourceBD obtenerInstancia() {
        if (INSTANCIA_DATASOURCEBD == null) {
            INSTANCIA_DATASOURCEBD = new DataSourceBD();
        }

        return INSTANCIA_DATASOURCEBD;
    }

    public DataSource getDatasource() {
        return datasource;
    }
    
    public Connection getConnection() throws SQLException {
        return datasource.getConnection();
    }
    
    private void obtenerCredenciales(){
        Properties properties = new Properties();
        String rutaActual = System.getProperty("user.dir");
        System.out.println(rutaActual);
        String nombreArchivo = "config.properties";
        
        try(FileReader fr = new FileReader(rutaActual + File.separator + nombreArchivo)){
            properties.load(fr);
            url = properties.getProperty("db.url");
            usuario = properties.getProperty("db.usuario");
            clave = properties.getProperty("db.clave");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
