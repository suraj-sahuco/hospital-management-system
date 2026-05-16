package com.codingshuttle.youtube.hospitalManagement.dto;

import java.util.Objects;

// @Data ❌ (manual implementation below)
// @AllArgsConstructor ❌ (manual constructor below)
// @NoArgsConstructor ❌ (manual constructor below)

public class LoginResponseDto {

    private String jwt;

    // 🔥 NEW FIELD
    private String refreshToken;

    private Long userId;

    // ✅ NoArgsConstructor
    public LoginResponseDto() {
    }

    // ✅ AllArgsConstructor
    public LoginResponseDto(String jwt,
                            String refreshToken,
                            Long userId) {

        this.jwt = jwt;
        this.refreshToken = refreshToken;
        this.userId = userId;
    }

    // ✅ Getter methods

    public String getJwt() {
        return jwt;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Long getUserId() {
        return userId;
    }

    // ✅ Setter methods

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // ✅ toString

    @Override
    public String toString() {
        return "LoginResponseDto{" +
                "jwt='" + jwt + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", userId=" + userId +
                '}';
    }

    // ✅ equals

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        LoginResponseDto that = (LoginResponseDto) o;

        return Objects.equals(jwt, that.jwt)
                &&
                Objects.equals(refreshToken,
                        that.refreshToken)
                &&
                Objects.equals(userId, that.userId);
    }

    // ✅ hashCode

    @Override
    public int hashCode() {
        return Objects.hash(jwt, refreshToken, userId);
    }

}