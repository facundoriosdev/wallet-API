package com.facundorios.wallet.api.repository;

import com.facundorios.wallet.api.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.Optional;
@SuppressWarnings("NullableProblems")
@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByAliasCvu(String aliasCvu);
}