package com.ykestdar.mediLaboSolutionFront.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;
import org.springframework.web.client.*;
import org.springframework.web.util.*;

import java.security.*;
import java.util.*;

import static io.jsonwebtoken.Jwts.parser;
import static java.lang.String.format;

@Component
public class JwtService {
    private final RestTemplate restTemplate;

    @Value("${service.url.patientAuthorizationBase}")
    private String patientAuthBase;

    public JwtService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }



    public String getUsername(String token) {

        String getUsernameUrl = String.format("%s/getUsernameByToken",patientAuthBase);
        String url = UriComponentsBuilder.fromHttpUrl(getUsernameUrl)
                .queryParam("token",token)
                .toUriString();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url,String.class);
            String username = response.getBody();
            return username;
        }catch (Exception e){
            return "errorr occurred" +e.getMessage();
        }


    }



    public boolean validateToken(String token) {
        System.out.println("inside jwt service in front end");
        String validationUrl = String.format("%s/validate",patientAuthBase);
        String url = UriComponentsBuilder.fromHttpUrl(validationUrl)
                .queryParam("token",token)
                .toUriString();

            ResponseEntity<String> response = restTemplate.getForEntity(url,String.class);


               if (response.getBody().equals("Valid")) {
                   System.out.println("inside jwt service in front end token is valid");
                   return true;
               }else {
                   System.out.println("inside jwt service in front end token is not valid");
                   return false;}


    }

}

