package com.facundorios.wallet.api.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    public UUID getId() {
        return id;
    }

    @Id
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "kyc_status")
    private String kycStatus = "PENDING";

    public String getKycStatus() {
        return kycStatus;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public User() {}

    public User(String email, String passwordHash) {
        this.id = UUID.randomUUID();
        this.email = email;
        this.passwordHash = passwordHash;
        this.kycStatus = "PENDING";
    }
}