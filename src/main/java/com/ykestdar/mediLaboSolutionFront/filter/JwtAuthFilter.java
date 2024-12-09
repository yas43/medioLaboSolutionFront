package com.ykestdar.mediLaboSolutionFront.filter;

import com.ykestdar.mediLaboSolutionFront.service.*;
import com.ykestdar.mediLaboSolutionFront.session.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;
import org.springframework.web.client.*;
import org.springframework.web.filter.*;
import org.springframework.web.util.*;

import java.io.*;
import java.util.*;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final RestTemplate restTemplate;
    private final JwtService jwtService;
    private final CustomUserDetailService userDetailService;
    private final JwtSessionStore jwtSessionStore;

    private static final Set<String> EXCLUDED_PATHS = Set.of("/messages/signUp","/messages/login");

    public JwtAuthFilter(RestTemplate restTemplate, JwtService jwtService, CustomUserDetailService userDetailService, JwtSessionStore jwtSessionStore) {
        this.restTemplate = restTemplate;
        this.jwtService = jwtService;
        this.userDetailService = userDetailService;

        this.jwtSessionStore = jwtSessionStore;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String sessionId = resolveSessionId(request);

        if (sessionId != null) {
            String token = jwtSessionStore.getToken(sessionId);
            System.out.println("session id is not null token is " + token);
            if (token != null && jwtService.validateToken(token)) {
                String username = jwtService.getUsername(token);
                UserDetails userDetails = userDetailService.loadUserByUsername(username);
                System.out.println("token is valid and username is " + username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Optionally renew JWT and update the session store
//                String renewedToken = jwtService.generateToken(authentication);
//                jwtSessionStore.storeToken(sessionId, renewedToken);
            }

        }

        filterChain.doFilter(request, response);
    }


    private Boolean isValidToken(String token){
        String baseUrl = "http://localhost:8084/login/validate";
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("token", token)
                .toUriString();
        System.out.println("hello im here");
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (response.getBody().equals("Valid")) {
            System.out.println("inside validation of token , token is valid ");
            return true;
        }
        System.out.println("inside validation of token , token is  not valid ");
        return false;
    }


    private String resolveSessionId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("SESSIONID".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
