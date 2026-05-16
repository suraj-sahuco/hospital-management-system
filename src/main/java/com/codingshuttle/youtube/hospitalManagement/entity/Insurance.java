package com.codingshuttle.youtube.hospitalManagement.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

// @Getter ❌
// @Setter ❌
// @NoArgsConstructor ❌
// @AllArgsConstructor ❌
// @Builder ❌

@Entity
public class Insurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ❗ unique policy number (duplicate allowed nahi)
    @Column(nullable = false, unique = true, length = 50)
    private String policyNumber;

    // ❗ insurance company/provider name
    @Column(nullable = false, length = 100)
    private String provider;

    // ❗ expiry date
    @Column(nullable = false)
    private LocalDate validUntil;

    // 🔥 auto timestamp (insert time pe set hota hai)
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 🔥 OneToOne (inverse side)
    // 👉 owning side Patient me hoga
    @OneToOne(mappedBy = "insurance")
    private Patient patient;

    // ✅ NoArgsConstructor
    public Insurance() {}

    // ✅ AllArgsConstructor
    public Insurance(Long id, String policyNumber, String provider,
                     LocalDate validUntil, LocalDateTime createdAt, Patient patient) {
        this.id = id;
        this.policyNumber = policyNumber;
        this.provider = provider;
        this.validUntil = validUntil;
        this.createdAt = createdAt;
        this.patient = patient;
    }

    // ✅ Getters
    public Long getId() { return id; }
    public String getPolicyNumber() { return policyNumber; }
    public String getProvider() { return provider; }
    public LocalDate getValidUntil() { return validUntil; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Patient getPatient() { return patient; }

    // ✅ Setters
    public void setId(Long id) { this.id = id; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }
    public void setProvider(String provider) { this.provider = provider; }
    public void setValidUntil(LocalDate validUntil) { this.validUntil = validUntil; }
    public void setPatient(Patient patient) { this.patient = patient; }

    // ❌ createdAt setter nahi diya (auto generated hai)
}