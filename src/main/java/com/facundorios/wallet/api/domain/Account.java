package com.facundorios.wallet.api.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import javax.naming.Name;
import java.math.BigDecimal;
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

    @Column(nullable = false)
    private BigDecimal balance;

    @Version
    private Long version;

    public Account() {}

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Account(UUID userId, String aliasCvu, String currency) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.aliasCvu = aliasCvu;
        this.currency = currency;
        this.balance = BigDecimal.ZERO;
        this.version = 0L; // Inicializamos la versión del bloqueo optimista
    }
}
