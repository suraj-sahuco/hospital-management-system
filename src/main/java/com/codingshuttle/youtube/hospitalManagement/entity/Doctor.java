package com.codingshuttle.youtube.hospitalManagement.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// @Getter ❌
// @Setter ❌
// @Builder ❌
// @NoArgsConstructor ❌
// @AllArgsConstructor ❌

@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔥 ONE-TO-ONE with User + shared primary key
    // 👉 Doctor.id == User.id
    @OneToOne
    @MapsId
    @JoinColumn(name = "id") // FK bhi yehi id hoga
    private User user;

    // ❗ mandatory
    @Column(nullable = false, length = 100)
    private String name;

    // optional
    @Column(length = 100)
    private String specialization;

    // unique email
    @Column(unique = true, length = 100)
    private String email;

    // 🔥 ManyToMany (inverse side)
    // 👉 mappedBy = "doctors" (Department me owning side defined hai)
    @ManyToMany(mappedBy = "doctors")
    private Set<Department> departments = new HashSet<>();


    // 🔥 One doctor → many appointments
    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments = new ArrayList<>();

    // ✅ NoArgsConstructor
    public Doctor() {}

    // ✅ AllArgsConstructor
    public Doctor(Long id, User user, String name, String specialization,
                  String email, Set<Department> departments,
                  List<Appointment> appointments) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.specialization = specialization;
        this.email = email;
        this.departments = departments;
        this.appointments = appointments;
    }

    // ✅ Getters
    public Long getId() { return id; }
    public User getUser() { return user; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public String getEmail() { return email; }
    public Set<Department> getDepartments() { return departments; }
    public List<Appointment> getAppointments() { return appointments; }

    // ✅ Setters
    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setName(String name) { this.name = name; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public void setEmail(String email) { this.email = email; }
    public void setDepartments(Set<Department> departments) { this.departments = departments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }
}