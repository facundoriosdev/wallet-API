package com.facundorios.wallet.api.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import javax.naming.Name;
import java.util.UUID;

@Entity
@Table(name = "accounts")

public class Account {
    @Id
    private UUID id;

    @Column (name = "user_id")
    private UUID userId;

    @Column(name = "alias_cvu", unique = true, nullable = false)
    private String aliasCvu;

    @Column(nullable = false)
    private String currency;

    @Version
    private Long version;

    public Account() {}
    public Account(UUID userId, String aliasCvu, String currency) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.aliasCvu = aliasCvu;
        this.currency = currency;
        this.version = 0L; // Inicializamos la versión del bloqueo optimista
    }
}
