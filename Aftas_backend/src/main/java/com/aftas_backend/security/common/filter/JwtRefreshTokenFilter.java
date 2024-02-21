package com.aftas_backend.security.common.filter;

import com.aftas_backend.repositories.MemberRepository;
import com.aftas_backend.security.common.helper.RequestHelper;
import com.aftas_backend.security.common.jwt.JwtTokenService;
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
    private final MemberRepository memberRepository;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String jwtToken = requestHelper.getJwtTokenIfExist(request);

        if (requestHelper.getTokenType(request).equals(TokenType.REFRESH_TOKEN)) {

            if (jwtToken == null) {
                throw new RuntimeException("Refresh token is required");
            }

            if (!jwtTokenService.isTokenValid(jwtToken, requestHelper.getTokenType(request))) {
                throw new RuntimeException("Invalid token");
            }

            validateRefreshToken(jwtToken, request);
        }

        filterChain.doFilter(request, response);

    }

    private void validateRefreshToken(String jwtToken, HttpServletRequest request) {
        checkUserAgent(jwtToken, request);
        checkIpAddress(jwtToken, request);
        checkUser(jwtToken, request);


    }

    private void checkUser(String jwtToken, HttpServletRequest request) {
        if (!memberRepository.existsByNumber(Integer.valueOf(jwtTokenService.extractUsername(jwtToken)))) {
            throw new RuntimeException("Invalid user");
        }

    }

    private void checkIpAddress(String jwtToken, HttpServletRequest request) {
        if (!jwtTokenService.extractIpAddress(jwtToken).equals(requestHelper.getIpAddress(request))) {
            throw new RuntimeException("Invalid ip address");
        }
    }

    private void checkUserAgent(String jwtToken, HttpServletRequest request) {
        if (!jwtTokenService.extractUserAgent(jwtToken).equals(requestHelper.getUserAgent(request))) {
            throw new RuntimeException("Invalid user agent");
        }
    }
}
