package com.facundorios.wallet.api.service;


import com.facundorios.wallet.api.domain.Account;
import com.facundorios.wallet.api.domain.Transaction;
import com.facundorios.wallet.api.repository.AccountRepository;
import com.facundorios.wallet.api.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

@Mock
private AccountRepository accountRepository;

@Mock
private TransactionRepository transactionRepository;

@InjectMocks
private AccountService accountService;

private UUID sourceId;
private UUID destinationId;
private Account source;
private Account destination;

@BeforeEach
public void setUp() {
    sourceId = UUID.randomUUID();
    destinationId = UUID.randomUUID();

    source = new Account( sourceId, "Source Account", "USD");
    source.setBalance(new BigDecimal("100.00"));

    destination = new Account( destinationId, "Destination Account", "USD");
    destination.setBalance(new BigDecimal("200.00"));
}
@Test
public void testExecuteTransfer_Success() {
    BigDecimal amount = new BigDecimal("100.00");

    when(accountRepository.findById(sourceId)).thenReturn(Optional.of(source));
    when(accountRepository.findById(destinationId)).thenReturn(Optional.of(destination));

    accountService.executeTransfer(sourceId, destinationId, amount);

    assertEquals(new BigDecimal("0.00"), source.getBalance());
    assertEquals(new BigDecimal("300.00"), destination.getBalance());

}
@Test
    void  testExecuteTransfer_Failure() {
    BigDecimal amount = new BigDecimal("10000.00");

    when(accountRepository.findById(sourceId)).thenReturn(Optional.of(source));
    when(accountRepository.findById(destinationId)).thenReturn(Optional.of(destination));

    IllegalArgumentException exception=
            assertThrows(IllegalArgumentException.class, () -> {
                accountService.executeTransfer(sourceId, destinationId, amount);
            });
    assertEquals("Insufficient funds", exception.getMessage());

    verify(accountRepository,never()).save(any(Account.class));
    }
    @Test
    void executeTransfer_InvalidDestinationAlias_ThrowsException() {
        destination.setAliasCvu("");
        BigDecimal transferAmount = new BigDecimal("10.00");

        when(accountRepository.findById(sourceId)).thenReturn(Optional.of(source));
        when(accountRepository.findById(destinationId)).thenReturn(Optional.of(destination));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.executeTransfer(sourceId, destinationId, transferAmount);
        });

        assertEquals("Destination account does not have a valid alias", exception.getMessage());

        verify(accountRepository, never()).save(any(Account.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }
    @Test
    void executeTransfer_SourceAccountNotFound_ThrowsException() {

        when(accountRepository.findById(sourceId)).thenReturn(Optional.empty());
        BigDecimal transferAmount = new BigDecimal("50.00");


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.executeTransfer(sourceId, destinationId, transferAmount);
        });

        assertEquals("Source account not found", exception.getMessage());


        verify(accountRepository, never()).findById(destinationId);
        verify(accountRepository, never()).save(any(Account.class));
    }
}
