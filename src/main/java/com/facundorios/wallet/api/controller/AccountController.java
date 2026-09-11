package com.facundorios.wallet.api.controller;

import com.facundorios.wallet.api.controller.dto.TransferRequest;
import com.facundorios.wallet.api.domain.Account;
import com.facundorios.wallet.api.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    @PostMapping("/transfer")
    public ResponseEntity<String> TransferMoney(@RequestBody TransferRequest request) {
        accountService.executeTransfer(
                request.sourceAccountId(),
                request.destinationAccountId(),
                request.amount()
        );

    return ResponseEntity.ok(("Transfer successful $" + request.amount()));
    }
}
