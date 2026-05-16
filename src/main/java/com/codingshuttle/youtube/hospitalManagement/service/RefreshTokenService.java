package com.codingshuttle.youtube.hospitalManagement.service;

import com.codingshuttle.youtube.hospitalManagement.entity.RefreshToken;
import com.codingshuttle.youtube.hospitalManagement.entity.User;
import com.codingshuttle.youtube.hospitalManagement.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // 🔥 CREATE REFRESH TOKEN
//    public RefreshToken createRefreshToken(User user) {
//
//        // 🔥 CHECK:
//        // user ka old refresh token already exist karta hai kya
//
//        refreshTokenRepository.findByUser(user)
//
//                // 🔥 Agar old token mila
//                .ifPresent(oldToken -> {
//
//                    // 🔥 OLD TOKEN DELETE KAR DO
//                    // taaki same user ka duplicate
//                    // refresh token DB mai na rahe
//
//                    refreshTokenRepository.delete(oldToken);
//                });
//
//        // 🔥 NEW REFRESH TOKEN CREATE KARO
//
//        RefreshToken refreshToken =
//                new RefreshToken();
//
//        // 🔥 token kis user ka hai
//
//        refreshToken.setUser(user);
//
//        // 🔥 random unique refresh token generate
//
//        refreshToken.setToken(
//                UUID.randomUUID().toString()
//        );
//
//        // 🔥 token expiry set (7 days)
//
//        refreshToken.setExpiryDate(
//                LocalDateTime.now().plusDays(7)
//        );
//
//        // 🔥 token active hai
//
//        refreshToken.setRevoked(false);
//
//        // 🔥 DB mai save karke return
//
//        return refreshTokenRepository.save(refreshToken);
//    }
    @Transactional
    public RefreshToken createRefreshToken(User user) {

        // delete old refresh token
        refreshTokenRepository.deleteByUser(user);

        // create new token
        RefreshToken refreshToken =
                new RefreshToken();

        refreshToken.setUser(user);

        refreshToken.setToken(
                UUID.randomUUID().toString()
        );

        refreshToken.setExpiryDate(
                LocalDateTime.now().plusDays(7)
        );

        refreshToken.setRevoked(false);

        return refreshTokenRepository.save(refreshToken);
    }
    // 🔥 VERIFY REFRESH TOKEN
    public RefreshToken verifyRefreshToken(String token) {

        // 🔥 DB mai token search karo

        RefreshToken refreshToken =

                refreshTokenRepository.findByToken(token)

                        // ❌ token exist nahi karta
                        .orElseThrow(() ->

                                new RuntimeException(
                                        "Invalid refresh token"
                                )
                        );

        // ❌ token manually revoke ho chuka

        if (refreshToken.isRevoked()) {

            throw new RuntimeException(
                    "Refresh token revoked"
            );
        }

        // ❌ token expiry check

        if (refreshToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            throw new RuntimeException(
                    "Refresh token expired"
            );
        }

        // ✅ valid token return

        return refreshToken;
    }
}