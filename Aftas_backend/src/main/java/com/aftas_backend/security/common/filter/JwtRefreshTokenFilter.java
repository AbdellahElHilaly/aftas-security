package com.aftas_backend.security.common.filter;

import com.aftas_backend.security.common.helper.RequestHelper;
import com.aftas_backend.security.common.jwt.JwtTokenService;
import com.aftas_backend.security.common.principal.UserPrincipalService;
import com.aftas_backend.security.utils.enums.TokenType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtRefreshTokenFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final RequestHelper requestHelper;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String jwtToken = jwtTokenService.getJwtTokenIfExist(request);

        if (requestHelper.getTokenType(request).equals(TokenType.REFRESH_TOKEN)) {

            if (jwtToken == null) {
                throw new RuntimeException("Refresh token is required");
            }

            if (!jwtTokenService.isTokenValid(jwtToken, requestHelper.getTokenType(request))) {
                throw new RuntimeException("Invalid token");
            }
        }


        filterChain.doFilter(request, response);

    }
}
