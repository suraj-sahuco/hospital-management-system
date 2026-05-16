package com.codingshuttle.youtube.hospitalManagement.dto;

import com.codingshuttle.youtube.hospitalManagement.entity.type.RoleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SignUpRequestDto {

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String username;

    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d).+$",
            message = "Password must contain one uppercase letter and one number"
    )
    private String password;

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    // ✅ Default value (empty roles set)
    private Set<RoleType> roles = new HashSet<>();

    // ✅ NoArgsConstructor
    public SignUpRequestDto() {
    }

    // ✅ AllArgsConstructor
    public SignUpRequestDto(String username, String password, String name, Set<RoleType> roles) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.roles = roles;
    }

    // ✅ Getter methods
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public Set<RoleType> getRoles() {
        return roles;
    }

    // ✅ Setter methods
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoles(Set<RoleType> roles) {
        this.roles = roles;
    }

    // ⚠️ Security tip: password ko toString me expose mat karo
    @Override
    public String toString() {
        return "SignUpRequestDto{" +
                "username='" + username + '\'' +
                ", password='***'" +
                ", name='" + name + '\'' +
                ", roles=" + roles +
                '}';
    }

    // ✅ equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SignUpRequestDto that = (SignUpRequestDto) o;

        return Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(name, that.name) &&
                Objects.equals(roles, that.roles);
    }

    // ✅ hashCode
    @Override
    public int hashCode() {
        return Objects.hash(username, password, name, roles);
    }
}