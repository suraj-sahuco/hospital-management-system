package com.codingshuttle.youtube.hospitalManagement.entity;

import com.codingshuttle.youtube.hospitalManagement.entity.type.BloodGroupType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "patient",

        // 🔥 Unique constraint (same name + birthDate duplicate nahi hoga)
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_patient_name_birthdate",
                        columnNames = {"name", "birthDate"}
                )
        },

        // 🔍 Index (fast search ke liye)
        indexes = {
                @Index(name = "idx_patient_birth_date", columnList = "birthDate")
        }
)
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ❗ mandatory
    @Column(nullable = false, length = 40)
    private String name;

    private LocalDate birthDate;

    // ❗ unique email
    @Column(unique = true, nullable = false)
    private String email;

    private String gender;

    // 🔥 OneToOne with User (shared primary key)
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    // 🔥 auto timestamp
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // 🔥 enum as string
    @Enumerated(EnumType.STRING)
    private BloodGroupType bloodGroup;

    // 🔥 OneToOne Insurance (owning side)
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "patient_insurance_id")
    private Insurance insurance;

    // 🔥 OneToMany (patient → appointments)
    @OneToMany(
            mappedBy = "patient",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<Appointment> appointments = new ArrayList<>();

    // =========================================================
    // ✅ NoArgsConstructor
    // =========================================================
    public Patient() {}

    // =========================================================
    // ✅ AllArgsConstructor
    // =========================================================
    public Patient(Long id, String name, LocalDate birthDate, String email,
                   String gender, User user, LocalDateTime createdAt,
                   BloodGroupType bloodGroup, Insurance insurance,
                   List<Appointment> appointments) {

        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
        this.gender = gender;
        this.user = user;
        this.createdAt = createdAt;
        this.bloodGroup = bloodGroup;
        this.insurance = insurance;
        this.appointments = appointments;
    }

    // =========================================================
    // ✅ GETTERS
    // =========================================================

    public Long getId() { return id; }

    public String getName() { return name; }

    public LocalDate getBirthDate() { return birthDate; }

    public String getEmail() { return email; }

    public String getGender() { return gender; }

    public User getUser() { return user; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public BloodGroupType getBloodGroup() { return bloodGroup; }

    public Insurance getInsurance() { return insurance; }

    public List<Appointment> getAppointments() { return appointments; }

    // =========================================================
    // ✅ SETTERS
    // =========================================================

    public void setId(Long id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public void setEmail(String email) { this.email = email; }

    public void setGender(String gender) { this.gender = gender; }

    public void setUser(User user) { this.user = user; }

    public void setBloodGroup(BloodGroupType bloodGroup) { this.bloodGroup = bloodGroup; }

    public void setInsurance(Insurance insurance) { this.insurance = insurance; }

    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }

    // ❌ createdAt ka setter nahi diya (auto generate hota hai)
}