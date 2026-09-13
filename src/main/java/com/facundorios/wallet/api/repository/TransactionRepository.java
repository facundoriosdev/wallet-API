package com.facundorios.wallet.api.repository;

import com.facundorios.wallet.api.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.List;
@SuppressWarnings("NullableProblems")
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findBySourceAccountIdOrDestinationAccountId(UUID sourceId, UUID destinationId);
}