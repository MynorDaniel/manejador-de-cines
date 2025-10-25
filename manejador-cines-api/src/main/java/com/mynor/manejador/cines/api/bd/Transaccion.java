/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.bd;

import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author mynordma
 */
public class Transaccion implements AutoCloseable {

    private final Connection CONN;
    private boolean seHizoCommit = false;

    public Transaccion() throws AccesoDeDatosException {
        try {
            CONN = DataSourceBD.obtenerInstancia().getConnection();
            CONN.setAutoCommit(false);
        } catch (SQLException ex) {
            throw new AccesoDeDatosException(ex.getMessage());
        }
    }

    public Connection obtenerConexion() {
        return CONN;
    }

    public void commit() throws AccesoDeDatosException {
        try {
            CONN.commit();
            seHizoCommit = true;
        } catch (SQLException ex) {
            throw new AccesoDeDatosException(ex.getMessage());
        }
    }

    @Override
    public void close() throws AccesoDeDatosException {
        try {
            if (!seHizoCommit) {
                CONN.rollback();
            }
        } catch (SQLException ex) {
            throw new AccesoDeDatosException(ex.getMessage());
        } finally {
            try {
                try (CONN) {
                    CONN.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                throw new AccesoDeDatosException(ex.getMessage());
            }
        }
    }
}
