package com.aftas_backend.security.rest.service.impl;

import com.aftas_backend.security.common.jwt.JwtTokenService;
import com.aftas_backend.security.rest.dto.request.LoginRequest;
import com.aftas_backend.security.rest.dto.response.JwtAuthenticationResponse;
import com.aftas_backend.security.rest.dto.response.JwtRefreshTokenResponse;
import com.aftas_backend.security.rest.service.AuthenticationService;
import com.aftas_backend.security.utils.enums.TokenType;
import com.aftas_backend.services.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthenticationResponse login(LoginRequest request, HttpServletRequest httpServletRequest) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        String access_token = jwtTokenService.generateToken(request.getUsername(), TokenType.ACCESS_TOKEN);
        String refresh_token = jwtTokenService.generateToken(request.getUsername(), TokenType.REFRESH_TOKEN);

        return JwtAuthenticationResponse.builder()
                .accessToken(access_token)
                .refreshToken(refresh_token)
                .build();
    }

    @Override
    public JwtRefreshTokenResponse refresh(HttpServletRequest httpServletRequest) {
        return null;
//        String token = jwtTokenService.getJwtTokenIfExist(httpServletRequest);
//        if (token == null) {
//            throw new RuntimeException("Refresh token is required");
//        }
//        String username = jwtTokenService.extractUsername(token);
//        return JwtRefreshTokenResponse.builder()
//
//                .accessToken(jwtTokenService.generateToken(username, TokenType.ACCESS_TOKEN))
//                .build();
    }

    @Override
    public void logout(HttpServletRequest httpServletRequest) {

    }

    @Override
    public JwtAuthenticationResponse getTestToken() {
        return JwtAuthenticationResponse.builder()
                .accessToken(jwtTokenService.generateToken("1", TokenType.ACCESS_TOKEN))
                .refreshToken(jwtTokenService.generateToken("1", TokenType.REFRESH_TOKEN))
                .build();
    }
}
