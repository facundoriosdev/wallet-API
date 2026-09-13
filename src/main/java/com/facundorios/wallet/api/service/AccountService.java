package com.facundorios.wallet.api.service;

import com.facundorios.wallet.api.domain.Account;
import com.facundorios.wallet.api.domain.Transaction;
import com.facundorios.wallet.api.repository.AccountRepository;
import com.facundorios.wallet.api.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository= transactionRepository;
    }

    @Transactional
    public void executeTransfer(UUID sourceAccountId, UUID destinationAccountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }
        if (sourceAccountId.equals(destinationAccountId)) {
            throw new IllegalArgumentException("Transfer source account and destination account must not be the same");
        }
        Account sourceAccount = accountRepository.findById(sourceAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Source account not found"));
        Account destinationAccount = accountRepository.findById(destinationAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Destination account not found"));

    if(!sourceAccount.getCurrency().equals(destinationAccount.getCurrency())) {
        throw new IllegalArgumentException("Source and destination accounts must have the same currency");
    }
    if (sourceAccount.getBalance().compareTo(amount) < 0) {
        throw new IllegalArgumentException("Insufficient funds"); //jaja sos pobre lero lero
    }
    String destAlias = destinationAccount.getAliasCvu();
    if (destAlias==null || destAlias.isEmpty()) {
        throw new IllegalArgumentException("Destination account does not have a valid alias");
    }
    sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
    destinationAccount.setBalance(destinationAccount.getBalance().add(amount));

    Transaction transaction = new Transaction(
            sourceAccountId,
            destinationAccountId,
            amount,
            "TRANSFER",
            "COMPLETED"
    );
    //guarda los cambios en la base de datos
    accountRepository.save(sourceAccount);
    accountRepository.save(destinationAccount);
    transactionRepository.save(transaction);

}
}
