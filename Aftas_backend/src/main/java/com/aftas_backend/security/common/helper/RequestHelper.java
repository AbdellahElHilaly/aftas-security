package com.aftas_backend.security.common.helper;

import com.aftas_backend.security.utils.enums.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class RequestHelper {
    public TokenType getTokenType(HttpServletRequest request) {
        String endpoint = request.getRequestURI();
        if (endpoint.equals("/api/v1/auth/refresh")) {
            return TokenType.REFRESH_TOKEN;
        }
        return TokenType.ACCESS_TOKEN;
    }

    public String getUserAgent(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader("User-Agent");
    }

    public String getIpAddress(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getRemoteAddr();
    }

    public String getJwtTokenIfExist(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            System.out.println(authHeader.replace("Bearer ", ""));
            return authHeader.replace("Bearer ", "");
        }
        return null;
    }


}
