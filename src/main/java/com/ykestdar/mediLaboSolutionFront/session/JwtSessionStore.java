package com.ykestdar.mediLaboSolutionFront.session;

import org.springframework.stereotype.*;

import java.util.*;
import java.util.concurrent.*;

@Component
public class JwtSessionStore {

    private final Map<String, String> jwtStore = new ConcurrentHashMap<>();

    public void storeToken(String sessionId, String jwtToken) {
        jwtStore.put(sessionId, jwtToken);
    }

    public String getToken(String sessionId) {
        return jwtStore.get(sessionId);
    }

    public void removeToken(String sessionId) {
        jwtStore.remove(sessionId);
    }
}
