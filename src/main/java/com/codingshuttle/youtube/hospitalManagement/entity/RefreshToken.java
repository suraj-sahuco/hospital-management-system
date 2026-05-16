package com.codingshuttle.youtube.hospitalManagement.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private LocalDateTime expiryDate;

    private boolean revoked;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // No Args Constructor
    public RefreshToken() {
    }

    // All Args Constructor
    public RefreshToken(Long id, String token,
                        LocalDateTime expiryDate,
                        boolean revoked,
                        User user) {
        this.id = id;
        this.token = token;
        this.expiryDate = expiryDate;
        this.revoked = revoked;
        this.user = user;
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}