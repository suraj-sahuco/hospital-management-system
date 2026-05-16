package com.codingshuttle.youtube.hospitalManagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Objects;

// @Data ❌ (manual implementation below)
// @AllArgsConstructor ❌ (manual constructor below)
// @NoArgsConstructor ❌ (manual constructor below)

public class LoginRequestDto {

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String username;
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d).+$",
            message = "Password must contain one uppercase letter and one number"
    )
    private String password;

    // ✅ NoArgsConstructor (Lombok @NoArgsConstructor ka manual version)
    public LoginRequestDto() {
    }

    // ✅ AllArgsConstructor (Lombok @AllArgsConstructor ka manual version)
    public LoginRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // ✅ Getter methods (Lombok @Getter ka part)
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // ✅ Setter methods (Lombok @Setter ka part)
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // ✅ toString (Lombok @ToString ka part)
    @Override
    public String toString() {
        return "LoginRequestDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    // ✅ equals (Lombok @EqualsAndHashCode ka part)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // same object reference
        if (o == null || getClass() != o.getClass()) return false;
        LoginRequestDto that = (LoginRequestDto) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

    // ✅ hashCode (Lombok @EqualsAndHashCode ka part)
    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}