package com.facundorios.wallet.api.controller.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferRequest(
        UUID sourceAccountId,
        UUID destinationAccountId,
        BigDecimal amount)
{}
