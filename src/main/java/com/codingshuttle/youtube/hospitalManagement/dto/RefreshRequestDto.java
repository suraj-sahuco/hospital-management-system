package com.codingshuttle.youtube.hospitalManagement.dto;

public class RefreshRequestDto {

    private String refreshToken;

    public RefreshRequestDto() {
    }

    public RefreshRequestDto(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}