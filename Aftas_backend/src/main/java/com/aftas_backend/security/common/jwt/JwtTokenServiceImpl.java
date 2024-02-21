package com.aftas_backend.security.common.jwt;

import com.aftas_backend.security.utils.enums.TokenType;
import com.aftas_backend.security.utils.env.SecurityEnvironment;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

    @Value("${jwt.signing.key}")
    private String jwtSigningKey;

    private final SecurityEnvironment securityEnvironment;

    @Override
    public Key getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String getJwtTokenIfExist(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.replace("Bearer ", "");
        }
        return null;
    }

    @Override
    public Boolean isTokenValid(String token, TokenType tokenType) {
        return isMachType(token, tokenType) &&
                isMachSignature(token) &&
                isTokenExpired(token);
    }

    @Override
    public Boolean isMachType(String token, TokenType tokenType) {
        return extractTokenType(token).equals(tokenType);
    }

    @Override
    public Boolean isMachSignature(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).after(new Date(System.currentTimeMillis()));
    }


    @Override
    public String extractUsername(String token) {
        Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token);
        return jws.getBody().getSubject();
    }

    @Override
    public TokenType extractTokenType(String token) {
        Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token);
        return TokenType.valueOf(jws.getBody().get("type", String.class));
    }

    @Override
    public Date extractExpiration(String token) {
        Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token);
        return jws.getBody().getExpiration();
    }

    @Override
    public String generateToken(String username, TokenType tokenType) {
        return Jwts.builder()
                .setSubject(username)
                .claim("type", tokenType.name())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(securityEnvironment.getTokenExpirationDates().get(tokenType))
                .signWith(getSecretKey()).compact();
    }
}
