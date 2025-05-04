package com.poly.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.poly.dto.UserDTO;
import com.poly.model.User;
import com.poly.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenStore tokenStore;

    public static final String SECRET = "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && tokenStore.isTokenValid(username, token));
    }

//    public String generateToken(UserDetails userDetails) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("username", userDetails.getUsername());
//        claims.put("password", userDetails.getPassword());
//        claims.put("Role", userDetails.getAuthorities());
//        
//        User user = userRepository.findByUsername(userDetails.getUsername());
//        claims.put("id_user", user.getId());
//        claims.put("image", user.getImage_user());
//        String token = createToken(claims, userDetails.getUsername());
//        tokenStore.storeToken(userDetails.getUsername(), token);
//        return token;
//    }
//    private String createToken(Map<String, Object> claims, String username) {
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(username)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60)) // 1 minute expiration
//                .signWith(getSignKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }


    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 *60)) // 1 hour expiration
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public void deleteToken(String username) {
        tokenStore.removeToken(username);
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("id_user", user.getId());
        claims.put("email", user.getEmail());
        claims.put("image", user.getImage_user());
        claims.put("Role", user.getRole() != null ? user.getRole().getName_role() : "ROLE_USER");
        String token = createToken(claims, user.getUsername());
        tokenStore.storeToken(user.getUsername(), token);

        return token;
    }
    
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());
        claims.put("password", userDetails.getPassword());
        claims.put("Role", userDetails.getAuthorities());
        
        User user = userRepository.findByUsername(userDetails.getUsername());
        claims.put("id_user", user.getId());
        claims.put("image", user.getImage_user());
        claims.put("hoten", user.getFullName());
        String token = createToken(claims, userDetails.getUsername());
        tokenStore.storeToken(userDetails.getUsername(), token);
        return token;
    }
    
}
