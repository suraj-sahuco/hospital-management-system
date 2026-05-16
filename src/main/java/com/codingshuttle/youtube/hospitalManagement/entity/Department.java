package com.codingshuttle.youtube.hospitalManagement.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

// @Getter ❌
// @Setter ❌
// @NoArgsConstructor ❌
// @AllArgsConstructor ❌

@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ❗ department name unique hona chahiye
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    // 🔥 ONE department → ONE head doctor
    @OneToOne
    @JoinColumn(name = "head_doctor_id") // foreign key
    private Doctor headDoctor;

    // 🔥 MANY departments ↔ MANY doctors
    @ManyToMany
    @JoinTable(
            name = "my_dpt_doctors", // junction table
            joinColumns = @JoinColumn(name = "dpt_id"), // department id
            inverseJoinColumns = @JoinColumn(name = "doctor_id") // doctor id
    )
    private Set<Doctor> doctors = new HashSet<>();

    // ✅ NoArgsConstructor
    public Department() {
    }

    // ✅ AllArgsConstructor
    public Department(Long id, String name, Doctor headDoctor, Set<Doctor> doctors) {
        this.id = id;
        this.name = name;
        this.headDoctor = headDoctor;
        this.doctors = doctors;
    }

    // ✅ Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Doctor getHeadDoctor() {
        return headDoctor;
    }

    public Set<Doctor> getDoctors() {
        return doctors;
    }

    // ✅ Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHeadDoctor(Doctor headDoctor) {
        this.headDoctor = headDoctor;
    }

    public void setDoctors(Set<Doctor> doctors) {
        this.doctors = doctors;
    }
}