package com.poly.service;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class TokenStore {

    private final ConcurrentHashMap<String, String> tokenStore = new ConcurrentHashMap<>();

    public void storeToken(String username, String token) {
        tokenStore.put(username, token);
    }

    public void removeToken(String username) {
        tokenStore.remove(username);
    }

    public String getToken(String username) {
        return tokenStore.get(username);
    }

    public boolean isTokenValid(String username, String token) {
        String storedToken = tokenStore.get(username);
        return token.equals(storedToken);
    }
}
