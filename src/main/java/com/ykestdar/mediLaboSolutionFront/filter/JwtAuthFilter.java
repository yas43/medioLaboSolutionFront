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

    private static final Set<String> EXCLUDED_PATHS = Set.of("/patient/signUp","/patient/login","/favicon.ico");

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
        System.out.println("request.geturi is "+request.getRequestURI());
        if (!EXCLUDED_PATHS.contains(request.getRequestURI())) {

            String sessionId = resolveSessionId(request);
            if (sessionId != null) {
                String token = jwtSessionStore.getToken(sessionId);
                System.out.println("session id is not null token is " + token);
                if (token != null && jwtService.validateToken(token)) {
                    String username = jwtService.getUsername(token);
                    UserDetails userDetails = userDetailService.loadUserByUsername(username);

                    response.setHeader(HttpHeaders.AUTHORIZATION,"bearer "+token);

                    filterChain.doFilter(request, response);
                }

            }
        }else filterChain.doFilter(request,response);


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
