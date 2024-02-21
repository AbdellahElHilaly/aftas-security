package com.aftas_backend.security.common.helper;

import com.aftas_backend.security.utils.enums.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class RequestHelper {
    public TokenType getTokenType(HttpServletRequest request) {
        String endpoint = request.getRequestURI();
        if (endpoint.equals("/api/v1/auth/refresh")) {
            System.out.println("REFRESH_TOKEN");
            return TokenType.REFRESH_TOKEN;
        };
        System.out.println("ACCESS_TOKEN");
        return TokenType.ACCESS_TOKEN;
    }
}
