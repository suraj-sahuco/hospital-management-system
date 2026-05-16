package com.codingshuttle.youtube.hospitalManagement;

import com.codingshuttle.youtube.hospitalManagement.controller.AuthController;
import com.codingshuttle.youtube.hospitalManagement.dto.LoginResponseDto;
import com.codingshuttle.youtube.hospitalManagement.security.AuthService;
import com.codingshuttle.youtube.hospitalManagement.security.JwtAuthFilter;
import com.codingshuttle.youtube.hospitalManagement.security.RateLimitFilter;
import com.codingshuttle.youtube.hospitalManagement.service.RefreshTokenService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private RefreshTokenService refreshTokenService;

    // 🔥 IMPORTANT FIX
    @MockBean
    private JwtAuthFilter jwtAuthFilter;
    @MockBean
    private RateLimitFilter rateLimitFilter;

    @Test
    void shouldLoginSuccessfully() throws Exception {

        String requestBody = """
            {
                "username": "suraj@gmail.com",
                "password": "Pass@123"
            }
            """;

        LoginResponseDto responseDto =
                new LoginResponseDto();

        responseDto.setJwt("access-token");
        responseDto.setRefreshToken("refresh-token");

        when(authService.login(any()))
                .thenReturn(responseDto);

        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
    }
}