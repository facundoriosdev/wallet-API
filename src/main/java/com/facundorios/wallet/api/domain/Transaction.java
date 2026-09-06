package com.facundorios.wallet.api.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    private UUID id;

    @Column(name = "source_account_id")
    private UUID sourceAccountId;

    @Column(name = "destination_account_id", nullable = false)
    private UUID destinationAccountId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String type; // DEPOSIT, TRANSFER

    @Column(nullable = false)
    private String status; // PENDING, COMPLETED, FAILED

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Transaction() {}

    public Transaction(UUID sourceAccountId, UUID destinationAccountId, BigDecimal amount, String type, String status) {
        this.id = UUID.randomUUID();
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
        this.type = type;
        this.status = status;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
