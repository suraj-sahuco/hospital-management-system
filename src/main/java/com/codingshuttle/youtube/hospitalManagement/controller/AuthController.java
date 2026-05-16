package com.codingshuttle.youtube.hospitalManagement.controller;

import com.codingshuttle.youtube.hospitalManagement.dto.*;
import com.codingshuttle.youtube.hospitalManagement.entity.RefreshToken;
import com.codingshuttle.youtube.hospitalManagement.entity.User;
import com.codingshuttle.youtube.hospitalManagement.security.AuthService;
import com.codingshuttle.youtube.hospitalManagement.service.RefreshTokenService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthService authService,
                          RefreshTokenService refreshTokenService) {

        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto loginRequestDto
    ) {

        return ResponseEntity.ok(
                authService.login(loginRequestDto)
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(
            @Valid @RequestBody SignUpRequestDto signupRequestDto
    ) {

        return ResponseEntity.ok(
                authService.signup(signupRequestDto)
        );
    }

    // 🔥 REFRESH TOKEN API

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refreshToken(
            @RequestBody RefreshRequestDto request
    ) {

        LoginResponseDto response =
                authService.refreshToken(
                        request.getRefreshToken()
                );

        return ResponseEntity.ok(response);
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestBody LogoutRequestDto request
    ) {

        authService.logout(
                request.getRefreshToken()
        );

        return ResponseEntity.ok(
                "Logged out successfully"
        );
    }
}