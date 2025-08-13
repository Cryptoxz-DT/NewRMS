//package com.DevanshNewRMS.NewRMS.Security;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import java.security.Key;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//
//@Service
//@Slf4j
//public class JwtService {
//
//    @Value("${app.security.jwt.secret}")
//    private String jwtSecret;
//
//    @Value("${app.security.jwt.expiration}")
//    private long jwtExpiration;
//
//    @Value("${app.security.jwt.refresh-expiration:604800000}") // 7 days default
//    private long refreshExpiration;
//
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    public Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts
//                .parserBuilder()
//                .setSigningKey(getSignKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    private Boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    public Boolean validateToken(String token, UserDetails userDetails) {
//        final String username = extractUsername(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
//
//    public String generateToken(String username) {
//        Map<String, Object> claims = new HashMap<>();
//        return createToken(claims, username, jwtExpiration);
//    }
//
//    public String generateToken(UserDetails userDetails) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("authorities", userDetails.getAuthorities());
//        return createToken(claims, userDetails.getUsername(), jwtExpiration);
//    }
//
//    public String generateRefreshToken(String username) {
//        Map<String, Object> claims = new HashMap<>();
//        return createToken(claims, username, refreshExpiration);
//    }
//
//    private String createToken(Map<String, Object> claims, String subject, long expiration) {
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + expiration);
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(subject)
//                .setIssuedAt(now)
//                .setExpiration(expiryDate)
//                .signWith(getSignKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    private Key getSignKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    public boolean validateToken(String authToken) {
//        try {
//            Jwts.parserBuilder()
//                    .setSigningKey(getSignKey())
//                    .build()
//                    .parseClaimsJws(authToken);
//            return true;
//        } catch (Exception ex) {
//            log.error("Invalid JWT token: {}", ex.getMessage());
//        }
//        return false;
//    }
//}
