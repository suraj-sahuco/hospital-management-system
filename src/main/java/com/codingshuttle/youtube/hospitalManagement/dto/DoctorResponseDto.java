package com.codingshuttle.youtube.hospitalManagement.dto;

import java.util.Objects;

// @Data ❌ (manual implementation below)
// @AllArgsConstructor ❌ (manual constructor below)
// @NoArgsConstructor ❌ (manual constructor below)

public class DoctorResponseDto {

    private Long id;
    private String name;
    private String specialization;
    private String email;

    // ✅ NoArgsConstructor (Lombok @NoArgsConstructor ka manual version)
    public DoctorResponseDto() {
    }

    // ✅ AllArgsConstructor (Lombok @AllArgsConstructor ka manual version)
    public DoctorResponseDto(Long id, String name, String specialization, String email) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.email = email;
    }

    // ✅ Getter methods (Lombok @Getter ka part)
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getEmail() {
        return email;
    }

    // ✅ Setter methods (Lombok @Setter ka part)
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // ✅ toString (Lombok @ToString ka part)
    @Override
    public String toString() {
        return "DoctorResponseDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", specialization='" + specialization + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    // ✅ equals (Lombok @EqualsAndHashCode ka part)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // same object
        if (o == null || getClass() != o.getClass()) return false;
        DoctorResponseDto that = (DoctorResponseDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(specialization, that.specialization) &&
                Objects.equals(email, that.email);
    }

    // ✅ hashCode (Lombok @EqualsAndHashCode ka part)
    @Override
    public int hashCode() {
        return Objects.hash(id, name, specialization, email);
    }
}