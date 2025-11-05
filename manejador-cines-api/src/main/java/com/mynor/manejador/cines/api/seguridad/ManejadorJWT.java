/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.manejador.cines.api.seguridad;

import com.mynor.manejador.cines.api.dtos.UsuarioSalidaDTO;
import com.mynor.manejador.cines.api.excepciones.AutorizacionException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;

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

        long tiempoExpiracion = 5 * 1000 * 60 * 60;
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
    
    public CredencialesTokenDTO validarToken(String authHeader) throws AutorizacionException {
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new AutorizacionException("Sesión inválida");
            }
            
            String token = authHeader.substring(7);
            
        try {
            Claims claims = Jwts.parser()
                .verifyWith((SecretKey) LLAVE)
                .build()
                .parseSignedClaims(token)
                .getPayload();
            
            CredencialesTokenDTO credenciales = new CredencialesTokenDTO();
            credenciales.setId((Integer) claims.get("id"));
            credenciales.setCorreo((String) claims.get("correo"));

            return credenciales;
        } catch (ExpiredJwtException e) {
            throw new AutorizacionException("Token expirado");
        } catch (MalformedJwtException e) {
            throw new AutorizacionException("Token malformado");
        } catch (JwtException | IllegalArgumentException e) {
            throw new AutorizacionException("Error al validar el token");
        }
    }

}
