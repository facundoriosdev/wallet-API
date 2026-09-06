package domain;

import jakarta.persistence.*;

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
}
