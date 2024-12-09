package com.ykestdar.mediLaboSolutionFront.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.*;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

import java.security.*;
import java.util.*;

import static io.jsonwebtoken.Jwts.parser;
import static java.lang.String.format;

@Component
public class JwtService {
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
    private final long validityInMilliseconds = 1000*30;


    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(format("%s",userDetails.getUsername()))
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }




//    public String getUsername(String token) {
//
//        try {
//            String username = Jwts.parser()
//                    .setSigningKey(SECRET)
//                    .parseClaimsJws(token)
//                    .getBody()
//                    .getSubject();
//            return username;
//        } catch (Exception e) {
//            throw new IllegalArgumentException("username did not extracted", e);
//        }
//
//    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject();
    }



    public boolean validateToken(String token) {
        try {
            parser().setSigningKey(SECRET).parseClaimsJws(token);
            System.out.println("token is valid");
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("token is not valid");
            return false;
        }
    }

}

