package com.codingshuttle.youtube.hospitalManagement;

import com.codingshuttle.youtube.hospitalManagement.dto.LoginRequestDto;
import com.codingshuttle.youtube.hospitalManagement.dto.LoginResponseDto;
import com.codingshuttle.youtube.hospitalManagement.entity.RefreshToken;
import com.codingshuttle.youtube.hospitalManagement.entity.User;
import com.codingshuttle.youtube.hospitalManagement.repository.PatientRepository;
import com.codingshuttle.youtube.hospitalManagement.repository.UserRepository;
import com.codingshuttle.youtube.hospitalManagement.security.AuthService;
import com.codingshuttle.youtube.hospitalManagement.security.AuthUtil;
import com.codingshuttle.youtube.hospitalManagement.service.RefreshTokenService;

import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AuthUtil authUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldLoginSuccessfully() {

        // Arrange

        LoginRequestDto requestDto =
                new LoginRequestDto();

        requestDto.setUsername("suraj@gmail.com");
        requestDto.setPassword("password123");

        User user = new User();

        user.setId(1L);
        user.setUsername("suraj@gmail.com");

        RefreshToken refreshToken =
                new RefreshToken();

        refreshToken.setToken("refresh-token");

        // Mock AuthenticationManager

        when(authenticationManager.authenticate(any(
                UsernamePasswordAuthenticationToken.class
        ))).thenReturn(authentication);

        // Mock authenticated user

        when(authentication.getPrincipal())
                .thenReturn(user);

        // Mock JWT generation

        when(authUtil.generateAccessToken(user))
                .thenReturn("access-token");

        // Mock refresh token generation

        when(refreshTokenService.createRefreshToken(user))
                .thenReturn(refreshToken);

        // Act

        LoginResponseDto response =
                authService.login(requestDto);

        // Assert

        assertNotNull(response);

        assertEquals(
                "access-token",
                response.getJwt()
        );

        assertEquals(
                "refresh-token",
                response.getRefreshToken()
        );

        assertEquals(
                1L,
                response.getUserId()
        );
    }
}
