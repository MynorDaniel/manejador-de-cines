package com.mynor.manejador.cines.api.recursos;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

/**
 *
 * @author 
 */
@Path("test")
public class JakartaEE11Resource {
    
    @GET
    public Response ping(){
        return Response
                .ok("todo funcionando")
                .build();
    }
}