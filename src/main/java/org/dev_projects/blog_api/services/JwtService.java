package org.dev_projects.blog_api.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.dev_projects.blog_api.configurations.JwtConfig;
import org.dev_projects.blog_api.entities.Role;
import org.dev_projects.blog_api.entities.User;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
public class JwtService {
    private final JwtConfig jwtConfig;

    public String generateAccessToken(User user) {

        return generateToken(user, jwtConfig.getAccessTokenExpiration());
    }

    public String generateRefreshToken(User user) {

        return generateToken(user, jwtConfig.getRefreshTokenExpiration());
    }

    private String generateToken(User user, long tokenExpiration) {
        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("email", user.getEmail())
                .claim("name", user.getUsername())
                .claim("role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(jwtConfig.getSecretKey())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            var claims = getClaims(token, jwtConfig.getSecret());

            return claims.getExpiration().after(new Date());

        } catch (JwtException ex){
            return false;
        }
    }


    private static Claims getClaims(String token, String secret) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getUserIdFromToken(String token) {
        return Long.valueOf(getClaims(token, jwtConfig.getSecret()).getSubject());
    }

    public String getEmailFromToken(String token) {
        return getClaims(token, jwtConfig.getSecret()).get("email", String.class);
    }

    public Role getRoleFromToken(String token) {
        return Role.valueOf(getClaims(token, jwtConfig.getSecret()).get("role", String.class));
    }
}
