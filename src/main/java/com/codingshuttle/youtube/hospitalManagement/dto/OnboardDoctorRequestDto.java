package com.codingshuttle.youtube.hospitalManagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Objects;

//@Data
public class OnboardDoctorRequestDto {
    @NotNull(message = "User id cannot be null")
    private Long userId;

    @NotBlank(message = "Specialization cannot be empty")
    @Size(min = 2, max = 50, message = "Specialization must be between 2 and 50 characters")
    private String specialization;

    @NotBlank(message = "Doctor name cannot be empty")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    public OnboardDoctorRequestDto(Long userId, String specialization, String name) {
        this.userId = userId;
        this.specialization = specialization;
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "OnboardDoctorRequestDto{" +
                "userId=" + userId +
                ", specialization='" + specialization + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OnboardDoctorRequestDto that = (OnboardDoctorRequestDto) o;
        return Objects.equals(userId, that.userId) && Objects.equals(specialization, that.specialization) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, specialization, name);
    }
}
