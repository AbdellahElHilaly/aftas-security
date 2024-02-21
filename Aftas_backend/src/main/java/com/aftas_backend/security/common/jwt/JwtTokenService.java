package com.aftas_backend.security.common.jwt;

import com.aftas_backend.security.utils.enums.TokenType;
import jakarta.servlet.http.HttpServletRequest;

import java.security.Key;
import java.util.Date;

public interface JwtTokenService {
    public Key getSecretKey();


    public String getJwtTokenIfExist(HttpServletRequest request);

    public Boolean isTokenValid(String token,TokenType tokenType);

    public Boolean isMachType(String token, TokenType tokenType);
    public Boolean isMachSignature(String token);
    public Boolean isTokenExpired(String token);


    public Date extractExpiration(String token);

    public String extractUsername(String token);
    public TokenType extractTokenType(String token);

    public String generateToken(String username, TokenType tokenType);

}
