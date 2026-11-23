/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Auth.model.AuthUser
 *  com.neel.Auth.service.JwtTokenService
 *  io.jsonwebtoken.Jwts
 *  io.jsonwebtoken.SignatureAlgorithm
 *  io.jsonwebtoken.security.Keys
 *  org.springframework.stereotype.Service
 */
package com.neel.Auth.service;

import com.neel.Auth.model.AuthUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {
    private static final Key SECRET_KEY = Keys.secretKeyFor((SignatureAlgorithm)SignatureAlgorithm.HS256);

    public String generateToken(AuthUser user) {
        return Jwts.builder().setSubject(user.getUsername()).claim("role", (Object)user.getRole()).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 6000L)).signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}

