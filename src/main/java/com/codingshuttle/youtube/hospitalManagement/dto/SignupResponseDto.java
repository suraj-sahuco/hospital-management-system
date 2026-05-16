package com.codingshuttle.youtube.hospitalManagement.dto;

import java.util.Objects;

// @Data ❌ (manual implementation below)
// @AllArgsConstructor ❌
// @NoArgsConstructor ❌

public class SignupResponseDto {

    private Long id;
    private String username;

    // ✅ NoArgsConstructor (Lombok @NoArgsConstructor ka manual version)
    public SignupResponseDto() {
    }

    // ✅ AllArgsConstructor (Lombok @AllArgsConstructor ka manual version)
    public SignupResponseDto(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    // ✅ Getter methods (Lombok @Getter ka part)
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    // ✅ Setter methods (Lombok @Setter ka part)
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // ✅ toString (Lombok @ToString ka part)
    @Override
    public String toString() {
        return "SignupResponseDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }

    // ✅ equals (Lombok @EqualsAndHashCode ka part)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // same reference
        if (o == null || getClass() != o.getClass()) return false;

        SignupResponseDto that = (SignupResponseDto) o;

        return Objects.equals(id, that.id) &&
                Objects.equals(username, that.username);
    }

    // ✅ hashCode
    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}