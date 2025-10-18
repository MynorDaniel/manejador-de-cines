/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.seguridad;

import com.mynor.manejador.cines.api.dtos.UsuarioSalidaDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author mynordma
 */
public class ManejadorJWT {
    
    private final Key LLAVE = Keys.hmacShaKeyFor("9fG7#kL2v!XpQ4mZ8tR1yS0dW3eB6uHj".getBytes());
    
    public TokenDTO generarToken(UsuarioSalidaDTO usuario) {
        Map<String, Object> credenciales = Map.of(
            "id", usuario.getId(),
            "correo", usuario.getCorreo()
        );

        long tiempoExpiracion = 2 * 1000 * 60 * 60;
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + tiempoExpiracion);

        String jwt = Jwts.builder()
            .subject(usuario.getCorreo())
            .issuer("manejador-cines-api")
            .issuedAt(ahora)
            .expiration(expiracion)
            .claims(credenciales)
            .signWith(LLAVE)
            .compact();

        return new TokenDTO(jwt, "Bearer", expiracion.getTime(), usuario);
    }

}
