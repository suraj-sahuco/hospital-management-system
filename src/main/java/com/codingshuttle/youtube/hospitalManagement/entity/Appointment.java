package com.codingshuttle.youtube.hospitalManagement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// @Getter ❌
// @Setter ❌
// @Builder ❌
// @NoArgsConstructor ❌
// @AllArgsConstructor ❌
// @ToString ❌

@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ❗ Appointment time mandatory hai
    @Column(nullable = false)
    private LocalDateTime appointmentTime;

    // ❗ Reason optional hai (max 500 chars)
    @Column(length = 500)
    private String reason;

    // 🔥 MANY appointments → ONE patient
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false) // foreign key
    private Patient patient;

    // 🔥 MANY appointments → ONE doctor
    @ManyToOne(fetch = FetchType.LAZY) // lazy loading (performance)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    // ✅ NoArgsConstructor (Lombok replacement)
    public Appointment() {
    }

    // ✅ AllArgsConstructor (Lombok replacement)
    public Appointment(Long id, LocalDateTime appointmentTime, String reason,
                       Patient patient, Doctor doctor) {
        this.id = id;
        this.appointmentTime = appointmentTime;
        this.reason = reason;
        this.patient = patient;
        this.doctor = doctor;
    }

    // ✅ Getters
    public Long getId() {
        return id;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public String getReason() {
        return reason;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    // ✅ Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    // ⚠️ toString (careful with relationships → infinite loop avoid)
    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", appointmentTime=" + appointmentTime +
                ", reason='" + reason + '\'' +
                '}';
    }
}