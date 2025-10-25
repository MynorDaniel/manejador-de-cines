/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.recursos;

import com.mynor.manejador.cines.api.excepciones.AccesoDeDatosException;
import com.mynor.manejador.cines.api.excepciones.AutorizacionException;
import com.mynor.manejador.cines.api.excepciones.ImagenInvalidaException;
import com.mynor.manejador.cines.api.modelo.Imagen;
import com.mynor.manejador.cines.api.seguridad.Autorizacion;
import com.mynor.manejador.cines.api.servicios.ImagenServicio;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 *
 * @author mynordma
 */

@Path("imagenes")
public class ImagenRecurso {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response subirImagen(
            @FormDataParam("file") InputStream archivoInputStream,
            @HeaderParam("Authorization") String authHeader) {

        ImagenServicio imagenServicio = new ImagenServicio();

        if (archivoInputStream == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Imagen inválida")
                           .build();
        }

        try {
            
            Autorizacion autorizacion = new Autorizacion(authHeader);
            autorizacion.validarSesion();
            
            Imagen imagenGuardada = imagenServicio.guardarImagen();

            String rutaGuardado = obtenerLinkImagenes() + imagenGuardada.getLink();
            File archivoDestino = new File(rutaGuardado);

            try (OutputStream out = new FileOutputStream(archivoDestino)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = archivoInputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            
            return Response.ok(imagenGuardada)
                           .status(Response.Status.CREATED)
                           .build();

        } catch (IOException | AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity(e.getMessage())
                           .build();
        } catch (AutorizacionException ex) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity(ex.getMessage())
                           .build();
        }
    }

    
    @GET
    @Path("{id}")
    @Produces({"image/png"})
    public Response obtenerImagen(@PathParam("id") String id, @HeaderParam("Authorization") String authHeader) {
        
        ImagenServicio imagenServicio = new ImagenServicio();
        
        try {
            
            Imagen imagen = imagenServicio.obtenerImagen(Integer.valueOf(id));
            StreamingOutput stream = cargarArchivo(imagen);
                        
            return Response.ok(stream)
                .header("Content-Disposition", "filename=" + imagen.getLink())
                .type("image/png")
                .build();
            
        } catch (AccesoDeDatosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (ImagenInvalidaException ex){
            return Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
        }catch (NumberFormatException ex){
            return Response.status(Response.Status.NOT_FOUND).entity("Id inválido").build();
        }
        
    }
    
    private StreamingOutput cargarArchivo(Imagen imagen) throws ImagenInvalidaException{
        File file = new File(obtenerLinkImagenes(), imagen.getLink());

        if (!file.exists()) {
            throw new ImagenInvalidaException("Imagen no encontrada");
        }

        StreamingOutput stream = output -> {
            try (FileInputStream in = new FileInputStream(file)) {
                in.transferTo(output); 
            }
        };
        
        return stream;
    }
    
    private String obtenerLinkImagenes(){
        Properties properties = new Properties();
        String rutaActual = System.getProperty("user.dir");
        System.out.println(rutaActual);
        String nombreArchivo = "config.properties";
        String link = "";
        
        try(FileReader fr = new FileReader(rutaActual + File.separator + nombreArchivo)){
            properties.load(fr);
            link = properties.getProperty("img.link");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        
        return link;
    }
}
