package com.codingshuttle.youtube.hospitalManagement.security;

import com.codingshuttle.youtube.hospitalManagement.entity.User;
import com.codingshuttle.youtube.hospitalManagement.entity.type.AuthProviderType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

// @Slf4j ❌ (manual logger below)

@Component
public class AuthUtil {

    // ✅ Manual logger (Lombok @Slf4j ka replacement)
    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(AuthUtil.class);

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    // 🔐 Secret key generate (JWT sign/verify ke liye)
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    // 🔥 JWT generate karna
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .subject(user.getUsername()) // username set
                .claim("userId", user.getId().toString()) // custom claim
                .issuedAt(new Date()) // token create time
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 3)) // 10 min expiry
                .signWith(getSecretKey()) // sign with secret key
                .compact(); // final token
    }

    // 🔥 Token se username nikalna
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey()) // verify signature
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject(); // username return
    }

    // 🔥 OAuth provider type nikalna
    public AuthProviderType getProviderTypeFromRegistrationId(String registrationId) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> AuthProviderType.GOOGLE;
            case "github" -> AuthProviderType.GITHUB;
            case "facebook" -> AuthProviderType.FACEBOOK;
            default -> throw new IllegalArgumentException("Unsupported OAuth2 provider: " + registrationId);
        };
    }

    // 🔥 OAuth2 se providerId nikalna
    public String determineProviderIdFromOAuth2User(OAuth2User oAuth2User, String registrationId) {

        String providerId = switch (registrationId.toLowerCase()) {

            case "google" -> oAuth2User.getAttribute("sub"); // Google unique id
            case "github" -> oAuth2User.getAttribute("id").toString(); // GitHub id

            default -> {
                log.error("Unsupported OAuth2 provider: {}", registrationId);
                throw new IllegalArgumentException("Unsupported OAuth2 provider: " + registrationId);
            }
        };

        // ❗ validation
        if (providerId == null || providerId.isBlank()) {
            log.error("Unable to determine providerId for provider: {}", registrationId);
            throw new IllegalArgumentException("Unable to determine providerId");
        }

        return providerId;
    }

    // 🔥 Username decide karna (OAuth user ke liye)
    public String determineUsernameFromOAuth2User(
            OAuth2User oAuth2User,
            String registrationId,
            String providerId
    ) {

        String email = oAuth2User.getAttribute("email");

        // ✅ agar email mil gaya → use karo
        if (email != null && !email.isBlank()) {
            return email;
        }

        // ❌ agar email nahi hai → fallback
        return switch (registrationId.toLowerCase()) {
            case "google" -> oAuth2User.getAttribute("sub");
            case "github" -> oAuth2User.getAttribute("login");
            default -> providerId;
        };
    }
}