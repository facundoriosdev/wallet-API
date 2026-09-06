package domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "kyc_status")
    private String kycStatus = "PENDING";

    public User() {}
}