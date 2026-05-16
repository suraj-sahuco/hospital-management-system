package com.codingshuttle.youtube.hospitalManagement.security;

import jakarta.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.servlet.HandlerExceptionResolver;

import static com.codingshuttle.youtube.hospitalManagement.entity.type.PermissionType.*;
import static com.codingshuttle.youtube.hospitalManagement.entity.type.RoleType.*;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

//    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    private final HandlerExceptionResolver handlerExceptionResolver;

    private final RateLimitFilter rateLimitFilter;

    private final CustomUserDetailsService customUserDetailsService;

    private final PasswordEncoder passwordEncoder;

    // logger
    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(WebSecurityConfig.class);

    // constructor
    public WebSecurityConfig(
            JwtAuthFilter jwtAuthFilter,
//            OAuth2SuccessHandler oAuth2SuccessHandler,
            HandlerExceptionResolver handlerExceptionResolver,
            RateLimitFilter rateLimitFilter,
            CustomUserDetailsService customUserDetailsService,
            PasswordEncoder passwordEncoder
    ) {

        this.jwtAuthFilter = jwtAuthFilter;

//        this.oAuth2SuccessHandler = oAuth2SuccessHandler;

        this.handlerExceptionResolver = handlerExceptionResolver;

        this.rateLimitFilter = rateLimitFilter;

        this.customUserDetailsService = customUserDetailsService;

        this.passwordEncoder = passwordEncoder;
    }

    // =========================================================
    // AUTHENTICATION PROVIDER
    // =========================================================

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider =
                new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(
                customUserDetailsService
        );

        authProvider.setPasswordEncoder(
                passwordEncoder
        );

        return authProvider;
    }

    // =========================================================
    // AUTHENTICATION MANAGER
    // =========================================================

//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration config
//    ) throws Exception {
//
//        return config.getAuthenticationManager();
//    }

    // =========================================================
    // SECURITY FILTER CHAIN
    // =========================================================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity
    ) throws Exception {

        httpSecurity

                // disable csrf
                .csrf(csrfConfig -> csrfConfig.disable())

                // stateless session
                .sessionManagement(sessionConfig ->
                        sessionConfig.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        ))

                // 🔥 register auth provider
                .authenticationProvider(authenticationProvider())

                // authorization
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/public/**",
                                "/auth/login",
                                "/auth/signup",
                                "/auth/refresh",
                                "/auth/logout"
                        ).permitAll()

                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/admin/**"
                        )
                        .hasAnyAuthority(
                                APPOINTMENT_DELETE.name(),
                                USER_MANAGE.name()
                        )

                        .requestMatchers("/admin/**")
                        .hasRole(ADMIN.name())

                        .requestMatchers("/doctors/**")
                        .hasAnyRole(
                                DOCTOR.name(),
                                ADMIN.name()
                        )

                        .anyRequest()
                        .authenticated()
                )

                // rate limiter filter
                // Uncomment after Redis stable


                .addFilterBefore(
                        rateLimitFilter,
                        UsernamePasswordAuthenticationFilter.class
                )


                // jwt filter
                .addFilterBefore(
                        jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                // exception handling
                .exceptionHandling(exceptionHandlingConfigurer ->
                        exceptionHandlingConfigurer
                                .accessDeniedHandler((request, response, ex) -> {

                                    handlerExceptionResolver.resolveException(
                                            request,
                                            response,
                                            null,
                                            ex
                                    );
                                }));

        return httpSecurity.build();
    }

    @PostConstruct
    public void init() {
        System.out.println("SECURITY CONFIG LOADED");
    }
}