package com.codingshuttle.youtube.hospitalManagement.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final RedisTemplate<String, Object> redisTemplate;

    // constructor injection
    public RateLimitFilter(
            RedisTemplate<String, Object> redisTemplate
    ) {

        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("RateLimitFilter Running");
        System.out.println(request.getRequestURI());

        String path = request.getRequestURI();

        // apply only on login API
        if (!path.contains("/auth/login")) {

            filterChain.doFilter(request, response);
            return;
        }

        // get client IP
        String ip = request.getRemoteAddr();

        System.out.println("IP: " + ip);

        // redis key
        String key = "rate_limit:" + ip;

        // increment request count
        Long requestCount =
                redisTemplate.opsForValue()
                        .increment(key);

        // first request pe expiry set
        if (requestCount != null
                &&
                requestCount == 1) {

            redisTemplate.expire(
                    key,
                    Duration.ofMinutes(5)
            );
        }

        System.out.println(
                "Request Count: " + requestCount
        );

        // limit exceeded
        if (requestCount != null
                &&
                requestCount > 2) {

            System.out.println(
                    "RATE LIMIT EXCEEDED"
            );

            response.setStatus(429);

            response.setContentType(
                    "application/json"
            );

            response.getWriter().write(
                    "{\"message\":\"Too many requests. Try again later.\"}"
            );

            return;
        }

        System.out.println("Request Allowed");

        filterChain.doFilter(
                request,
                response
        );
    }
}