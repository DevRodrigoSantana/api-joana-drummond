package com.drummond.IA.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Slf4j
public class JwtUtils {
    public static final String JWT_BEARER = "Bearer ";
    public static final String JWT_AUTHORIZATION = "Authorization";
    public static final String SECRET_KEY = "0123456789-0123456789-0123456789";
    public static final long EXPIRE_DAYS = 0;
    public static final long EXPIRE_HOURS = 1;
    public static final long EXPIRE_MINUTES = 0;

    // Refresh token expiration
    private static final long REFRESH_EXPIRE_DAYS = 15;

    private JwtUtils() {
    }

    private static Key generateKey() {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        return key;
    }

    private static Date toExpireDateAcess(Date start) {
        LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = dateTime.plusDays(EXPIRE_DAYS).plusHours(EXPIRE_HOURS).plusMinutes(EXPIRE_MINUTES);
        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static JwtToken createToken(String id, String email, String role,String username) {
        String aud = "https://johannagpt.netlify.app/";
        String iss = "https://joana-ai-drummond.up.railway.app/auth";
        Date issuedAt = new Date();
        Date limit = toExpireDateAcess(issuedAt);

        String token = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(email)
                .claim("id", id)
                .claim("aud", aud)
                .claim("iss", iss)
                .claim("username",username)
                .setIssuedAt(issuedAt)
                .setExpiration(limit)
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .claim("role", role)
                .compact();

        return new JwtToken(token);
    }

    // Create refresh token
    public static RefreshToken createRefreshToken(String id, String email) {
        String aud = "https://johannagpt.netlify.app/";
        String iss = "https://joana-ai-drummond.up.railway.app/refresh";
        Date issuedAt = new Date();
        Date limit = toExpireDate(issuedAt, REFRESH_EXPIRE_DAYS, 0, 0);

        String token = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(email)
                .claim("id", id)
                .claim("aud", aud)
                .claim("iss", iss)
                .setIssuedAt(issuedAt)
                .setExpiration(limit)
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .compact();

        return new RefreshToken(token);
    }

    private static Date toExpireDate(Date start, long days, long hours, long minutes) {
        LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = dateTime.plusDays(days).plusHours(hours).plusMinutes(minutes);
        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
    }

    private static Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(generateKey()).build()
                    .parseClaimsJws(refactorToken(token)).getBody();
        } catch (JwtException ex) {
            log.error(String.format("Token invalido %s", ex.getMessage()));
        }
        return null;
    }

    public static String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public static boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(generateKey()).build()
                    .parseClaimsJws(refactorToken(token));
            return true;
        } catch (JwtException ex) {
            log.error(String.format("Token invalido %s", ex.getMessage()));
        }
        return false;
    }

    private static String refactorToken(String token) {
        if (token.contains(JWT_BEARER)) {
            return token.substring(JWT_BEARER.length());
        }
        return token;
    }
}
