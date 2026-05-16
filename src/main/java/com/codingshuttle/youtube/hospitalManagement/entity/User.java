package com.codingshuttle.youtube.hospitalManagement.entity;

import com.codingshuttle.youtube.hospitalManagement.entity.type.AuthProviderType;
import com.codingshuttle.youtube.hospitalManagement.entity.type.RoleType;
import com.codingshuttle.youtube.hospitalManagement.security.RolePermissionMapping;

import jakarta.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "app_user",
        indexes = {
                @Index(name = "idx_provider_id_provider_type",
                        columnList = "providerId, providerType")
        })
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ❗ username unique hona chahiye
    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    // 🔥 OAuth ke liye
    private String providerId;

    @Enumerated(EnumType.STRING)
    private AuthProviderType providerType;

    // 🔥 Roles (stored as separate table)
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<RoleType> roles = new HashSet<>();


    // 🔥 Refresh Token Mapping
    @OneToOne(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private RefreshToken refreshToken;


    // ✅ Constructors
    public User() {}

    public User(Long id, String username, String password,
                String providerId, AuthProviderType providerType,
                Set<RoleType> roles,
                RefreshToken refreshToken) {

        this.id = id;
        this.username = username;
        this.password = password;
        this.providerId = providerId;
        this.providerType = providerType;
        this.roles = roles;
        this.refreshToken = refreshToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public AuthProviderType getProviderType() {
        return providerType;
    }

    public void setProviderType(AuthProviderType providerType) {
        this.providerType = providerType;
    }

    public Set<RoleType> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleType> roles) {
        this.roles = roles;
    }

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    // 🔥 MOST IMPORTANT METHOD (Spring Security use karta hai)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        roles.forEach(role -> {

            // 🔥 Role → Permissions mapping
            Set<SimpleGrantedAuthority> permissions =
                    RolePermissionMapping.getAuthoritiesForRole(role);

            authorities.addAll(permissions);

            // 🔥 ROLE prefix (IMPORTANT)
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        });

        return authorities;
    }

    // 🔥 UserDetails ke required methods (default true)
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}