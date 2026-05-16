package com.codingshuttle.youtube.hospitalManagement.security;

import com.codingshuttle.youtube.hospitalManagement.entity.User;
import com.codingshuttle.youtube.hospitalManagement.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

// @Slf4j ❌ (manual logger below)
// @RequiredArgsConstructor ❌ (manual constructor below)

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final AuthUtil authUtil;
    private final HandlerExceptionResolver handlerExceptionResolver;

    // ✅ Manual logger (Lombok @Slf4j ka replacement)
    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(JwtAuthFilter.class);

    // ✅ Manual constructor (Lombok @RequiredArgsConstructor ka replacement)
    public JwtAuthFilter(UserRepository userRepository,
                         AuthUtil authUtil,
                         HandlerExceptionResolver handlerExceptionResolver) {
        this.userRepository = userRepository;
        this.authUtil = authUtil;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // 🔥 Step 1: Request log karo
            log.info("incoming request: {}", request.getRequestURI());

            // 🔥 Step 2: Authorization header check karo
            final String requestTokenHeader = request.getHeader("Authorization");

            // ❌ Agar header nahi ya invalid hai → skip
            if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            // 🔐 Step 3: Token extract karo
            String token = requestTokenHeader.substring(7);

            // 🔥 Step 4: Username nikaalo token se
            String username = authUtil.getUsernameFromToken(token);

            // 🔥 Step 5: Agar user authenticated nahi hai
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // DB se user fetch karo
                User user = userRepository.findByUsername(username).orElseThrow();

                // 🔥 Authentication object banao
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                user.getAuthorities()
                        );

                // 🔥 SecurityContext me set karo (VERY IMPORTANT)
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            // 🔥 Step 6: next filter ko pass karo
            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            // ❌ error aaya → handle karo
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }
}